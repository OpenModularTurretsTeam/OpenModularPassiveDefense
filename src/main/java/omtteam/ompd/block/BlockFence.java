package omtteam.ompd.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.api.block.IHasItemBlock;
import omtteam.omlib.block.BlockAbstractMiscPane;
import omtteam.omlib.block.UnownedMarker;
import omtteam.omlib.core.security.PlayerIdentity;
import omtteam.omlib.util.player.PlayerUtil;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.config.ConfigHandler;
import omtteam.ompd.core.DefenseTier;
import omtteam.ompd.core.FenceRules;
import omtteam.ompd.item.ItemBlockFence;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.registry.ModBlocks;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("deprecation")
public class BlockFence extends BlockAbstractMiscPane implements IHasItemBlock {
    private static final PropertyInteger TIER = PropertyInteger.create("tier", DefenseTier.MIN, DefenseTier.MAX);

    public BlockFence() {
        super("", "", Material.ROCK, true);
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TIER, 1));
        this.setTranslationKey(OMPDNames.Blocks.fence);
        this.setRegistryName(Reference.MOD_ID, OMPDNames.Blocks.fence);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockFence(block);
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityPassiveTiered();
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TIER, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TIER, DefenseTier.fromMeta(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return DefenseTier.toMeta(state.getValue(TIER));
    }

    @Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (worldIn.isRemote || entityIn instanceof EntityItem) {
            return;
        }
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityPassiveTiered) || FenceRules.hurts(
                ((TileEntityPassiveTiered) tileEntity).getTrustManager().core(), player(entityIn), petOwner(entityIn))) {
            float damage = ConfigHandler.getDefenseStats().fenceDamage(state.getValue(TIER));
            entityIn.attackEntityFrom(DamageSource.CACTUS, damage);
        }
    }

    private static Optional<PlayerIdentity> player(Entity entity) {
        return entity instanceof EntityPlayer
                ? Optional.of(PlayerUtil.getPlayerInfo((EntityPlayer) entity).identity()) : Optional.empty();
    }

    private static Optional<PlayerIdentity> petOwner(Entity entity) {
        if (!(entity instanceof IEntityOwnable)) {
            return Optional.empty();
        }
        UUID owner = ((IEntityOwnable) entity).getOwnerId();
        if (owner == null) {
            return Optional.empty();
        }
        String name = UsernameCache.getLastKnownUsername(owner);
        return Optional.of(new PlayerIdentity(owner, name == null ? "" : name));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return BlockHelper.onBlockActivated(world, pos, player, hand);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        BlockHelper.onBlockPlacedByTiered(worldIn, pos, placer, stack);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return DefenseTier.toMeta(state.getValue(TIER));
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModBlocks.fence, 1, DefenseTier.toMeta(state.getValue(TIER)));
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return ConfigHandler.getDefenseStats().hardness(blockState.getValue(TIER));
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return ConfigHandler.getDefenseStats().explosionResistance(world.getBlockState(pos).getValue(TIER));
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    @ParametersAreNonnullByDefault
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int i = 0; i < 5; i++) {
            items.add(new ItemStack(ModBlocks.fence, 1, i));
        }
    }

    /** Blocks without an owner sparkle green, so players see they can claim them. */
    @Override
    @SideOnly(Side.CLIENT)
    @ParametersAreNonnullByDefault
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
        UnownedMarker.randomDisplayTick(world, pos, random);
    }
}
