package omtteam.ompd.block;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.config.Config;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import omtteam.omlib.api.render.camo.ICamoSupport;
import omtteam.omlib.block.BlockAbstractCamoTileEntity;
import omtteam.omlib.compat.theoneprobe.TOPInfoModifier;
import omtteam.omlib.network.MessageOpenCamoMenu;
import omtteam.omlib.util.camo.EnumTool;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityCamo;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider.showStandardBlockInfo;

/**
 * Created by Keridos on 31/01/17.
 * This Class
 */
@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
public class BlockCamoTrap extends BlockAbstractCamoTileEntity implements TOPInfoModifier {
    public static final IProperty<EnumTool> TOOL = PropertyEnum.create("tools", EnumTool.class);

    public BlockCamoTrap() {
        super(Material.WOOD);
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setSoundType(SoundType.GROUND);
        this.setHarvestLevel("axe", 2);
        this.setUnlocalizedName(OMPDNames.Blocks.camoTrap);
        this.setRegistryName(Reference.MOD_ID, OMPDNames.Blocks.camoTrap);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TOOL, EnumTool.SHOVEL));
    }

    @Override
    @Nonnull
    public BlockStateContainer createBlockState_OM() {
        return new ExtendedBlockState(this, new IProperty[]{TOOL}, new IUnlistedProperty[]{RENDERBLOCKSTATE});
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public TileEntity createTileEntity_OM(World world, IBlockState state) {
        return new TileEntityCamo(state);
    }

    /**
     * The camouflage is changed in OMLib's camouflage menu: whoever may change it (see {@link TileEntityCamo}) opens it
     * with the main hand; everyone else gets nothing, so the trap does not reveal itself. Sneaking skips the menu, so
     * blocks can still be placed against the trap.
     */
    @Override
    @ParametersAreNonnullByDefault
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (hand != EnumHand.MAIN_HAND || player.isSneaking() || !(tileEntity instanceof TileEntityCamo)) {
            return false;
        }
        TileEntityCamo trap = (TileEntityCamo) tileEntity;
        if (!trap.canSetCamo(player) && !trap.canChangeLight(player)) {
            return false;
        }
        if (player instanceof EntityPlayerMP) {
            MessageOpenCamoMenu.open((EntityPlayerMP) player, pos);
        }
        return true;
    }

    /** Mines with the tool of the camouflage (the stored tool is kept for camouflage without a tool). */
    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCamo) {
            IBlockState camo = ((TileEntityCamo) tileEntity).getCamoState();
            EnumTool tool = EnumTool.byName(camo.getBlock().getHarvestTool(camo));
            if (tool != null) {
                return state.withProperty(TOOL, tool);
            }
        }
        return state;
    }

    /** Light of the camouflage settings (OMLib camouflage menu). */
    @Override
    @ParametersAreNonnullByDefault
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof TileEntityCamo ? ((TileEntityCamo) tileEntity).getCamoSettings().getLightValue() : 0;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof TileEntityCamo ? ((TileEntityCamo) tileEntity).getCamoSettings().getLightOpacity() : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TOOL, EnumTool.values()[meta]);
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TOOL).ordinal();
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean causesSuffocation_OM(IBlockState state) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isFullBlock_OM(IBlockState state) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isFullCube_OM(IBlockState state) {
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        // solid only for the owner and trusted players (and the owner's team)
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCamo && entityIn instanceof EntityPlayer
                && ((TileEntityCamo) tileEntity).isTrusted((EntityPlayer) entityIn)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, Block.FULL_BLOCK_AABB);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onBlockPlacedBy_OM(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        BlockHelper.onBlockPlacedBy(worldIn, pos, state, placer, stack, this);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public String getHarvestTool(IBlockState state) {
        EnumTool tool = state.getValue(TOOL);
        return tool.getName();
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        ICamoSupport te = null;
        if (world.getTileEntity(pos) instanceof ICamoSupport) {
            te = (ICamoSupport) world.getTileEntity(pos);
        }
        if (te != null) {
            return new ItemStack(te.getCamoState().getBlock(), 1);
        }
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        ICamoSupport te = (ICamoSupport) world.getTileEntity(data.getPos());
        if (te != null) {
            BlockPos pos = data.getPos();
            IProbeConfig config = Config.getRealConfig();
            if (!te.canSetCamo(player)) {
                showStandardBlockInfo(config, mode, probeInfo, te.getCamoState(), te.getCamoState().getBlock(), world, pos, player, data);
            } else {
                showStandardBlockInfo(config, mode, probeInfo, blockState, blockState.getBlock(), world, pos, player, data);
            }
        }
    }
}
