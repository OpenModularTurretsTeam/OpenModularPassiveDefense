package omtteam.ompd.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.omlib.util.player.Player;
import omtteam.ompd.tileentity.TileEntityTiered;

/**
 * Created by Keridos on 28/11/16.
 * This Class
 */
@SuppressWarnings("deprecation")
public class BlockHelper {

    public static void onBlockPlacedByTiered(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, Block block) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof TileEntityTiered) {
            onBlockPlacedBy(worldIn, pos, state, placer, stack, block);
            TileEntityTiered ownedBlock = (TileEntityTiered) worldIn.getTileEntity(pos);
            if (ownedBlock != null) {
                ownedBlock.setTier(stack.getItemDamage());
                switch (stack.getItemDamage()) {
                    case 0:
                        block.setResistance(10.0F);
                        block.setHardness(10.0F);
                        break;
                    case 1:
                        block.setResistance(20.0F);
                        block.setHardness(20.0F);
                        break;
                    case 2:
                        block.setResistance(30.0F);
                        block.setHardness(30.0F);
                        break;
                    case 3:
                        block.setResistance(50.0F);
                        block.setHardness(50.0F);
                        break;
                    case 4:
                        block.setResistance(80.0F);
                        block.setHardness(80.0F);
                }
            } else {
                worldIn.destroyBlock(pos, true);
            }
        }
    }

    public static void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, Block block) {
        EntityPlayer player = (EntityPlayer) placer;
        TileEntityOwnedBlock ownedBlock = (TileEntityOwnedBlock) worldIn.getTileEntity(pos);
        if (ownedBlock != null) {
            ownedBlock.setOwner(new Player(player));
        }
    }

    public static boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && player.isSneaking() && (hand == EnumHand.MAIN_HAND && player.getHeldItemMainhand() == ItemStack.EMPTY)) {
            TileEntityTiered base = (TileEntityTiered) world.getTileEntity(pos);
            if (base != null && player.getUniqueID().toString().equals(base.getOwner())) {
                world.destroyBlock(base.getPos(), true);
            } else if (base != null) {
                player.sendMessage(new TextComponentString(I18n.translateToLocal("status.ownership")));
            }
            return true;
        } else return world.isRemote;
    }
}
