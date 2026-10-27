package omtteam.ompd.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.api.block.IHasItemBlock;
import omtteam.omlib.block.BlockAbstractTileEntity;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.config.ConfigHandler;
import omtteam.ompd.core.DefenseTier;
import omtteam.ompd.core.SpikeRules;
import omtteam.ompd.item.ItemBlockTiered;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Tiered, owned spikes: standing on them hurts by the tier once per second, landing on them by that times 1–10 by the
 * fall distance (on top of the fall damage), see {@link SpikeRules}; the fence rules spare the owner, trusted players,
 * the team and their pets. Tier in the metadata as the hardened blocks.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BlockSpikes extends BlockAbstractTileEntity implements IHasItemBlock {
    public static final PropertyInteger TIER = PropertyInteger.create("tier", DefenseTier.MIN, DefenseTier.MAX);
    public static final DamageSource SPIKES = new DamageSource("ompd.spikes");
    private static final AxisAlignedBB SPIKES_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public BlockSpikes() {
        super(Material.IRON);
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setSoundType(SoundType.METAL);
        this.setHarvestLevel("pickaxe", 2);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TIER, DefenseTier.MIN));
        this.setTranslationKey(OMPDNames.Blocks.spikes);
        this.setRegistryName(Reference.MOD_ID, OMPDNames.Blocks.spikes);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockTiered(block, OMPDNames.Blocks.spikes);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity_OM(World world, IBlockState state) {
        return new TileEntityPassiveTiered();
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState_OM() {
        return new BlockStateContainer(this, TIER);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TIER, DefenseTier.fromMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return DefenseTier.toMeta(state.getValue(TIER));
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox_OM(IBlockState state, IBlockAccess source, BlockPos pos) {
        return SPIKES_AABB;
    }

    @Override
    public boolean isOpaqueCube_OM(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube_OM(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock_OM(IBlockState state) {
        return false;
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /** Mobs walk around spikes as around cactus. */
    @Override
    @Nullable
    public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos) {
        return PathNodeType.DAMAGE_OTHER;
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        if (!world.isRemote && entity instanceof EntityLivingBase && BlockHelper.hurts(world, pos, entity)) {
            entity.attackEntityFrom(SPIKES, SpikeRules.landingDamage(world.getBlockState(pos).getValue(TIER), fallDistance));
        }
        super.onFallenUpon(world, pos, entity, fallDistance);
    }

    /** Entities standing on the spikes are inside the block space (the spikes are 12 px high). */
    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && entity instanceof EntityLivingBase && SpikeRules.isStandingHitTick(world.getTotalWorldTime())
                && BlockHelper.hurts(world, pos, entity)) {
            entity.attackEntityFrom(SPIKES, SpikeRules.standingDamage(state.getValue(TIER)));
        }
    }

    @Override
    public boolean onBlockActivated_OM(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                       EnumFacing side, float hitX, float hitY, float hitZ) {
        return BlockHelper.onBlockActivated(world, pos, player, hand);
    }

    @Override
    public void onBlockPlacedBy_OM(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        BlockHelper.onBlockPlacedByTiered(world, pos, placer, stack);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return DefenseTier.toMeta(state.getValue(TIER));
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, DefenseTier.toMeta(state.getValue(TIER)));
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return ConfigHandler.getDefenseStats().hardness(state.getValue(TIER));
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return ConfigHandler.getDefenseStats().explosionResistance(world.getBlockState(pos).getValue(TIER));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < DefenseTier.MAX; i++) {
            items.add(new ItemStack(this, 1, i));
        }
    }
}
