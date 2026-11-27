package omtteam.ompd.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import omtteam.ompd.config.DefenseTier;
import omtteam.ompd.item.ItemBlockTallGate;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * A 2 block high {@link BlockTieredGate} for stacked fences and walls. Split like a door: the lower half keeps facing and
 * open, the upper half powered; {@link #getActualState} merges them. Only the lower half drops the gate.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BlockTallGate extends BlockTieredGate {
    public static final PropertyEnum<EnumDoorHalf> HALF = PropertyEnum.create("half", EnumDoorHalf.class);

    private final String name;

    public BlockTallGate(String name) {
        super(name);
        this.name = name;
        this.setDefaultState(this.getDefaultState().withProperty(HALF, EnumDoorHalf.LOWER));
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockTallGate(block, name);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, OPEN, POWERED, IN_WALL, TIER, HALF);
    }

    /** Lower half: bits 0–1 facing, bit 2 open; upper half: bit 2 powered; bit 3 is the half. */
    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        if ((meta & 8) != 0) {
            return getDefaultState().withProperty(HALF, EnumDoorHalf.UPPER).withProperty(POWERED, (meta & 4) != 0);
        }
        return getDefaultState().withProperty(HALF, EnumDoorHalf.LOWER)
                .withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(OPEN, (meta & 4) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(HALF) == EnumDoorHalf.UPPER) {
            return 8 | (state.getValue(POWERED) ? 4 : 0);
        }
        return state.getValue(FACING).getHorizontalIndex() | (state.getValue(OPEN) ? 4 : 0);
    }

    @Override
    @Nonnull
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state.getValue(HALF) == EnumDoorHalf.UPPER) {
            IBlockState lower = world.getBlockState(pos.down());
            if (lower.getBlock() == this) {
                state = state.withProperty(FACING, lower.getValue(FACING)).withProperty(OPEN, lower.getValue(OPEN));
            }
        } else {
            IBlockState upper = world.getBlockState(pos.up());
            if (upper.getBlock() == this) {
                state = state.withProperty(POWERED, upper.getValue(POWERED));
            }
        }
        return super.getActualState(state, world, pos);
    }

    @Override
    protected BlockPos controlPos(IBlockState state, BlockPos pos) {
        return state.getValue(HALF) == EnumDoorHalf.UPPER ? pos.down() : pos;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return super.getCollisionBoundingBox(getActualState(state, world, pos), world, pos);
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return super.getBlockFaceShape(world, getActualState(state, world, pos), pos, face);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return pos.getY() < world.getHeight() - 1 && super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.up());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // the item places both halves and sets owner and tier, see ItemBlockTallGate
    }

    /**
     * Removes the other half when one is gone (the lower half then drops the gate, reading the tier before the tile
     * entity is gone) and opens or closes the gate by redstone at either half.
     */
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (world.isRemote) {
            return;
        }
        boolean upper = state.getValue(HALF) == EnumDoorHalf.UPPER;
        BlockPos other = upper ? pos.down() : pos.up();
        if (world.getBlockState(other).getBlock() != this) {
            ItemStack drop = new ItemStack(this, 1, DefenseTier.toMeta(BlockHelper.tileEntityTier(world, pos)));
            world.setBlockToAir(pos);
            if (!upper) {
                spawnAsEntity(world, pos, drop);
            }
            return;
        }
        if (block != this) {
            updatePower(world, upper ? other : pos, block);
        }
    }

    /** Vanilla gate redstone handling over both halves: powered (kept in the upper half) opens, unpowered closes. */
    public void updatePower(World world, BlockPos lower, @Nullable Block source) {
        BlockPos upper = lower.up();
        IBlockState lowerState = world.getBlockState(lower);
        IBlockState upperState = world.getBlockState(upper);
        boolean powered = world.isBlockPowered(lower) || world.isBlockPowered(upper);
        if ((powered || source == null || source.getDefaultState().canProvidePower()) && upperState.getValue(POWERED) != powered) {
            world.setBlockState(upper, upperState.withProperty(POWERED, powered), 2);
            if (lowerState.getValue(OPEN) != powered) {
                world.setBlockState(lower, lowerState.withProperty(OPEN, powered), 2);
                world.markBlockRangeForRenderUpdate(lower, upper);
                world.playEvent(null, powered ? 1008 : 1014, lower, 0);
            }
        }
    }

    /** In creative mode breaking the upper half removes the lower one without a drop, as vanilla doors do. */
    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (player.capabilities.isCreativeMode && state.getValue(HALF) == EnumDoorHalf.UPPER
                && world.getBlockState(pos.down()).getBlock() == this) {
            world.setBlockToAir(pos.down());
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == EnumDoorHalf.UPPER ? Items.AIR : super.getItemDropped(state, rand, fortune);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (state.getValue(HALF) == EnumDoorHalf.LOWER) {
            super.getDrops(drops, world, pos, state, fortune);
        }
    }
}
