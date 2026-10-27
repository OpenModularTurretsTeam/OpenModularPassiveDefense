package omtteam.ompd.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.api.block.IHasItemBlock;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.config.ConfigHandler;
import omtteam.ompd.core.DefenseTier;
import omtteam.ompd.item.ItemBlockHardenedDoor;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

/**
 * A tiered, owned door. Both halves have a {@link TileEntityPassiveTiered} with owner and tier (the tier is not in the
 * metadata, vanilla's door metadata is full); the owner and trusted players open it by hand, redstone opens it for
 * everyone (vanilla {@link BlockDoor} handling).
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BlockHardenedDoor extends BlockDoor implements IHasItemBlock {
    public static final PropertyInteger TIER = PropertyInteger.create("tier", DefenseTier.MIN, DefenseTier.MAX);

    public BlockHardenedDoor() {
        super(Material.IRON);
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2);
        this.setTranslationKey(OMPDNames.Blocks.hardenedDoor);
        this.setRegistryName(Reference.MOD_ID, OMPDNames.Blocks.hardenedDoor);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockHardenedDoor(block);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF, FACING, OPEN, HINGE, POWERED, TIER);
    }

    @Override
    @Nonnull
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return super.getActualState(state, world, pos).withProperty(TIER, BlockHelper.tileEntityTier(world, pos));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityPassiveTiered();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if (BlockHelper.isRemovalClick(player, hand)) {
            return BlockHelper.onBlockActivated(world, pos, player, hand);
        }
        if (world.isRemote) {
            return true;
        }
        BlockPos lower = state.getValue(HALF) == EnumDoorHalf.LOWER ? pos : pos.down();
        IBlockState lowerState = world.getBlockState(lower);
        if (lowerState.getBlock() != this) {
            return false;
        }
        if (!BlockHelper.canOpenByHand(world, lower, player)) {
            player.sendStatusMessage(new TextComponentTranslation("status.ownership"), true);
            return true;
        }
        boolean open = !lowerState.getValue(OPEN);
        world.setBlockState(lower, lowerState.withProperty(OPEN, open), 10);
        world.markBlockRangeForRenderUpdate(lower, lower.up());
        world.playEvent(null, open ? 1005 : 1011, pos, 0);
        return true;
    }

    /**
     * The lower half drops the door when the upper half or its support is gone; breaking either half ends here
     * ({@link BlockDoor#onBlockHarvested} removes the upper half first). Vanilla drops after removing the block (tile
     * entity and tier lost); this one reads the tier first.
     */
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (state.getValue(HALF) == EnumDoorHalf.LOWER && !world.isRemote) {
            boolean upperGone = world.getBlockState(pos.up()).getBlock() != this;
            boolean unsupported = !world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP);
            if (upperGone || unsupported) {
                ItemStack drop = new ItemStack(this, 1, DefenseTier.toMeta(BlockHelper.tileEntityTier(world, pos)));
                world.setBlockToAir(pos);
                if (!upperGone) {
                    world.setBlockToAir(pos.up());
                }
                spawnAsEntity(world, pos, drop);
                return;
            }
        }
        super.neighborChanged(state, world, pos, block, fromPos);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        // the item places both halves and sets owner and tier, see ItemBlockHardenedDoor
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HALF) == EnumDoorHalf.UPPER ? Items.AIR : Item.getItemFromBlock(this);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (state.getValue(HALF) == EnumDoorHalf.LOWER) {
            drops.add(new ItemStack(this, 1, DefenseTier.toMeta(BlockHelper.tileEntityTier(world, pos))));
        }
    }

    @Override
    @Nonnull
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, DefenseTier.toMeta(BlockHelper.tileEntityTier(world, pos)));
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return getItem(world, pos, state);
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return ConfigHandler.getDefenseStats().hardness(BlockHelper.tileEntityTier(world, pos));
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return ConfigHandler.getDefenseStats().explosionResistance(BlockHelper.tileEntityTier(world, pos));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < DefenseTier.MAX; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }
}
