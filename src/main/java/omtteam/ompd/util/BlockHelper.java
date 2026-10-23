package omtteam.ompd.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.omlib.util.player.Player;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;

import static omtteam.omlib.util.player.PlayerUtil.isPlayerOwner;

/**
 * Created by Keridos on 28/11/16.
 * This Class
 */
public class BlockHelper {

    public static void onBlockPlacedByTiered(World worldIn, BlockPos pos, EntityLivingBase placer, ItemStack stack) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof TileEntityPassiveTiered) {
            onBlockPlacedBy(worldIn, pos, placer);
            // hardness and resistance come from the block state (DefenseTier)
            ((TileEntityPassiveTiered) worldIn.getTileEntity(pos)).setTier(stack.getItemDamage());
        }
    }

    /** Makes the placing player the owner; blocks placed by other entities (deployers, other mods) stay unowned. */
    public static void onBlockPlacedBy(World worldIn, BlockPos pos, EntityLivingBase placer) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (placer instanceof EntityPlayer && tileEntity instanceof TileEntityOwnedBlock) {
            ((TileEntityOwnedBlock) tileEntity).setOwner(new Player((EntityPlayer) placer));
        }
    }

    public static boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && player.isSneaking() && (hand == EnumHand.MAIN_HAND && player.getHeldItemMainhand().isEmpty())) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (!(tileEntity instanceof TileEntityPassiveTiered) || isPlayerOwner(player, (TileEntityPassiveTiered) tileEntity)) {
                world.destroyBlock(pos, true);
            } else {
                player.sendMessage(new TextComponentTranslation("status.ownership"));
            }
            return true;
        } else return world.isRemote;
    }
}
