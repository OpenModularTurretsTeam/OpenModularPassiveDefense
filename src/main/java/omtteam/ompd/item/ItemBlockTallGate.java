package omtteam.ompd.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import omtteam.ompd.block.BlockTallGate;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Places both halves of a {@link BlockTallGate} facing the player's direction and gives them the placing player as
 * owner and the tier of the item.
 */
public class ItemBlockTallGate extends ItemBlockTiered {
    public ItemBlockTallGate(Block block, String name) {
        super(block, name);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {
        if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
            pos = pos.offset(facing);
        }
        ItemStack stack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, stack) || !player.canPlayerEdit(pos.up(), facing, stack)
                || !block.canPlaceBlockAt(world, pos)) {
            return EnumActionResult.FAIL;
        }
        IBlockState lower = block.getDefaultState().withProperty(BlockTallGate.HALF, EnumDoorHalf.LOWER)
                .withProperty(BlockFenceGate.FACING, player.getHorizontalFacing());
        world.setBlockState(pos, lower, 2);
        world.setBlockState(pos.up(), block.getDefaultState().withProperty(BlockTallGate.HALF, EnumDoorHalf.UPPER), 2);
        world.notifyNeighborsOfStateChange(pos, block, false);
        world.notifyNeighborsOfStateChange(pos.up(), block, false);
        BlockHelper.initPlacedPart(world, pos, player, stack.getMetadata());
        BlockHelper.initPlacedPart(world, pos.up(), player, stack.getMetadata());
        if (!world.isRemote) {
            ((BlockTallGate) block).updatePower(world, pos, null);
        }
        SoundType sound = block.getSoundType(lower, world, pos, player);
        world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
        stack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}
