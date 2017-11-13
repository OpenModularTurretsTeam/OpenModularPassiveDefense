package omtteam.ompd.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.api.IHasItemBlock;
import omtteam.omlib.blocks.BlockAbstractMiscPane;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.init.ModBlocks;
import omtteam.ompd.items.blocks.ItemBlockFence;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityPassiveOwnedBlock;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static omtteam.omlib.util.PlayerUtil.isPlayerOwner;

@SuppressWarnings("deprecation")
public class BlockFence extends BlockAbstractMiscPane implements IHasItemBlock {
    private static final PropertyInteger TIER = PropertyInteger.create("tier", 1, 5);

    public BlockFence() {
        super("","",Material.ROCK, true);
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 2);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TIER, 1));
        this.setUnlocalizedName(OMPDNames.Blocks.fence);
        this.setRegistryName(Reference.MOD_ID,OMPDNames.Blocks.fence);
    }

    @Override
    public ItemBlock getItemBlock(Block block) {
        return new ItemBlockFence(block);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityPassiveOwnedBlock();
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
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
        return this.getDefaultState().withProperty(TIER, meta + 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TIER) - 1;
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        TileEntityPassiveOwnedBlock te = (TileEntityPassiveOwnedBlock)worldIn.getTileEntity(pos);
        if (!(te != null && entityIn instanceof EntityPlayer && isPlayerOwner((EntityPlayer)entityIn, te)) && !(entityIn instanceof EntityItem)) entityIn.attackEntityFrom(DamageSource.CACTUS, 5);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return BlockHelper.onBlockActivated(world,pos,state,player,hand,side, hitX,hitY,hitZ);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        BlockHelper.onBlockPlacedBy(worldIn,pos,state,placer,stack,this);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TIER) - 1;
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModBlocks.fence, 1, state.getValue(TIER) - 1);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        switch (blockState.getValue(TIER)) {
            case 1:
                return 10.0F;
            case 2:
                return 20.0F;
            case 3:
                return 30.0F;
            case 4:
                return 50.0F;
            case 5:
                return 80.0F;
        }
        return 10.0F;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        switch (world.getBlockState(pos).getValue(TIER)) {
            case 1:
                return 10.0F;
            case 2:
                return 20.0F;
            case 3:
                return 30.0F;
            case 4:
                return 50.0F;
            case 5:
                return 80.0F;
        }
        return 10.0F;
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
}
