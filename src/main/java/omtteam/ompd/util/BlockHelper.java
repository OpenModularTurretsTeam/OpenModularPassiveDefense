package omtteam.ompd.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
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
@SuppressWarnings("deprecation")
public class BlockHelper {

    public static void onBlockPlacedByTiered(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, Block block) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) instanceof TileEntityPassiveTiered) {
            onBlockPlacedBy(worldIn, pos, state, placer, stack, block);
            TileEntityPassiveTiered ownedBlock = (TileEntityPassiveTiered) worldIn.getTileEntity(pos);
            if (ownedBlock != null) {
                // hardness and resistance come from the block state (DefenseTier)
                ownedBlock.setTier(stack.getItemDamage());
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
        if (!world.isRemote && player.isSneaking() && (hand == EnumHand.MAIN_HAND && player.getHeldItemMainhand().isEmpty())) {
            TileEntityPassiveTiered base = (TileEntityPassiveTiered) world.getTileEntity(pos);
            if (base == null || isPlayerOwner(player, base)) {
                world.destroyBlock(pos, true);
            } else {
                player.sendMessage(new TextComponentTranslation("status.ownership"));
            }
            return true;
        } else return world.isRemote;
    }
}
