package omtteam.ompd.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
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
import omtteam.ompd.config.DefenseTier;
import omtteam.ompd.item.ItemBlockTiered;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A tiered, owned gate (fence gate or wall gate, they differ by texture). Facing, open and powered in the metadata as
 * vanilla's gate, tier and owner in the {@link TileEntityPassiveTiered}; the owner and trusted players open it by hand,
 * redstone opens it for everyone (vanilla {@link BlockFenceGate} handling). Hardened fences (panes) connect to its post
 * sides.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BlockTieredGate extends BlockFenceGate implements IHasItemBlock {
    public static final PropertyInteger TIER = PropertyInteger.create("tier", DefenseTier.MIN, DefenseTier.MAX);

    private final String name;

    public BlockTieredGate(String name) {
        super(BlockPlanks.EnumType.OAK);
        this.name = name;
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2);
        this.setTranslationKey(name);
        this.setRegistryName(Reference.MOD_ID, name);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockTiered(block, name);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, OPEN, POWERED, IN_WALL, TIER);
    }

    @Override
    @Nonnull
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return super.getActualState(state, world, pos).withProperty(TIER, BlockHelper.tileEntityTier(world, pos));
    }

    /** Iron instead of vanilla's wood: needs a pickaxe to drop, not flammable, not opened by villagers. */
    @Override
    @Nonnull
    public Material getMaterial(IBlockState state) {
        return Material.IRON;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
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
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        BlockHelper.initPlacedPart(world, pos, placer instanceof EntityPlayer ? (EntityPlayer) placer : null, stack.getMetadata());
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
        BlockPos gate = controlPos(state, pos);
        IBlockState gateState = world.getBlockState(gate);
        if (gateState.getBlock() != this) {
            return false;
        }
        if (!BlockHelper.canOpenByHand(world, gate, player)) {
            player.sendStatusMessage(new TextComponentTranslation("status.ownership"), true);
            return true;
        }
        // as vanilla: opens away from the player
        if (gateState.getValue(OPEN)) {
            gateState = gateState.withProperty(OPEN, false);
        } else {
            EnumFacing playerFacing = EnumFacing.fromAngle(player.rotationYaw);
            if (gateState.getValue(FACING) == playerFacing.getOpposite()) {
                gateState = gateState.withProperty(FACING, playerFacing);
            }
            gateState = gateState.withProperty(OPEN, true);
        }
        world.setBlockState(gate, gateState, 10);
        world.markBlockRangeForRenderUpdate(gate, gate.up());
        world.playEvent(null, gateState.getValue(OPEN) ? 1008 : 1014, pos, 0);
        return true;
    }

    /** The position whose state holds facing and open (the gate itself; the lower half of a tall gate). */
    protected BlockPos controlPos(IBlockState state, BlockPos pos) {
        return pos;
    }

    /** Panes (hardened fences) connect to the post sides, i.e. along the gate's axis. */
    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        IBlockState state = getActualState(world.getBlockState(pos), world, pos);
        return facing.getAxis() != state.getValue(FACING).getAxis();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(this, 1, DefenseTier.toMeta(BlockHelper.tileEntityTier(world, pos))));
    }

    /** Keeps the tile entity (tier) until the drops are made, see {@link #harvestBlock}. */
    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        super.harvestBlock(world, player, pos, state, te, stack);
        world.setBlockToAir(pos);
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
