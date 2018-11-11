package omtteam.ompd.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import omtteam.omlib.blocks.BlockAbstractCamoTileEntity;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityCamo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Created by Keridos on 31/01/17.
 * This Class
 */
@SuppressWarnings("deprecation")
public class BlockCamoTrap extends BlockAbstractCamoTileEntity {
    public BlockCamoTrap() {
        super(Material.ROCK);
        this.setCreativeTab(OpenModularPassiveDefense.modularPassiveDefenseTab);
        this.setSoundType(SoundType.GROUND);
        this.setHarvestLevel("pickaxe", 2);
        this.setUnlocalizedName(OMPDNames.Blocks.camoTrap);
        this.setRegistryName(Reference.MOD_ID, OMPDNames.Blocks.camoTrap);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{}, new IUnlistedProperty[]{RENDERBLOCKSTATE});
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityCamo(state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
            ItemStack heldItem = player.getHeldItemMainhand();
            TileEntityCamo te = (TileEntityCamo) world.getTileEntity(pos);
            if (player.isSneaking() && heldItem == ItemStack.EMPTY) {
                if (te != null) {
                    if (player.getUniqueID().toString().equals(te.getOwner())) {
                        te.setCamoState(state);
                        world.notifyBlockUpdate(pos, state, state, 3);
                    } else {
                        player.sendMessage(
                                new TextComponentString(I18n.translateToLocal("status.ownership")));
                    }
                }
            }

            Block heldItemBlock = null;

            if (heldItem != ItemStack.EMPTY) {
                heldItemBlock = Block.getBlockFromItem(heldItem.getItem());
            }

            if (!player.isSneaking() && heldItem != ItemStack.EMPTY && heldItem.getItem() instanceof ItemBlock &&
                    heldItemBlock.isNormalCube(heldItemBlock.getStateFromMeta(heldItem.getMetadata())) && Block.getBlockFromItem(
                    heldItem.getItem()).isOpaqueCube(heldItemBlock.getStateFromMeta(heldItem.getMetadata())) && !(Block.getBlockFromItem(
                    heldItem.getItem()) instanceof BlockCamoTrap)) {
                if (te != null) {
                    if (player.getUniqueID().toString().equals(te.getOwner())) {
                        te.setCamoState(heldItemBlock.getStateFromMeta(heldItem.getItemDamage()));
                        world.notifyBlockUpdate(pos, state, state, 3);
                    } else {
                        player.sendMessage(
                                new TextComponentString(I18n.translateToLocal("status.ownership")));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        AxisAlignedBB alignedBB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
        collidingBoxes.add(alignedBB);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }
}
