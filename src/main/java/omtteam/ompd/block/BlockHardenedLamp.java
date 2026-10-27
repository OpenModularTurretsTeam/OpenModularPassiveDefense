package omtteam.ompd.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
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
import omtteam.ompd.item.ItemBlockTiered;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * A hardened block that always gives off light level 15 and points away from the face it was placed on (the lamp
 * face there, hardened block sides elsewhere). Facing in the metadata, tier (1–5, strength as the hardened block) and
 * owner in the {@link TileEntityPassiveTiered}.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BlockHardenedLamp extends BlockAbstractTileEntity implements IHasItemBlock {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyInteger TIER = PropertyInteger.create("tier", DefenseTier.MIN, DefenseTier.MAX);

    public BlockHardenedLamp() {
        super(Material.ROCK);
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2);
        this.setLightLevel(1.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setTranslationKey(OMPDNames.Blocks.hardenedLamp);
        this.setRegistryName(Reference.MOD_ID, OMPDNames.Blocks.hardenedLamp);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockTiered(block, OMPDNames.Blocks.hardenedLamp);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity_OM(World world, IBlockState state) {
        return new TileEntityPassiveTiered();
    }

    @Override
    public boolean isOpaqueCube_OM(IBlockState blockState) {
        return true;
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState_OM() {
        return new BlockStateContainer(this, FACING, TIER);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    @Nonnull
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(TIER, BlockHelper.tileEntityTier(world, pos));
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
                                            float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, facing);
    }

    @Override
    @Nonnull
    public IBlockState withRotation(IBlockState state, Rotation rotation) {
        return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    public boolean onBlockActivated_OM(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                       EnumFacing side, float hitX, float hitY, float hitZ) {
        return BlockHelper.onBlockActivated(world, pos, player, hand);
    }

    @Override
    public void onBlockPlacedBy_OM(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        BlockHelper.initPlacedPart(world, pos, placer instanceof EntityPlayer ? (EntityPlayer) placer : null, stack.getMetadata());
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, DefenseTier.toMeta(BlockHelper.tileEntityTier(world, pos)));
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos) {
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
