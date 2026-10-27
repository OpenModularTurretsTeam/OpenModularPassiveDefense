package omtteam.ompd.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Places a hardened door like {@link ItemDoor} (both halves, hinge side from the click position) and gives both
 * halves the placing player as owner and the tier of the item.
 */
public class ItemBlockHardenedDoor extends ItemBlockTiered {
    public ItemBlockHardenedDoor(Block block) {
        super(block, OMPDNames.Blocks.hardenedDoor);
    }

    @Override
    @Nonnull
    @ParametersAreNonnullByDefault
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,
                                      float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }
        if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
            pos = pos.offset(facing);
        }
        ItemStack stack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, stack) || !block.canPlaceBlockAt(world, pos)) {
            return EnumActionResult.FAIL;
        }
        EnumFacing doorFacing = EnumFacing.fromAngle(player.rotationYaw);
        int x = doorFacing.getXOffset();
        int z = doorFacing.getZOffset();
        boolean rightHinge = x < 0 && hitZ < 0.5F || x > 0 && hitZ > 0.5F || z < 0 && hitX > 0.5F || z > 0 && hitX < 0.5F;
        ItemDoor.placeDoor(world, pos, doorFacing, block, rightHinge);
        BlockHelper.initPlacedPart(world, pos, player, stack.getMetadata());
        BlockHelper.initPlacedPart(world, pos.up(), player, stack.getMetadata());
        IBlockState placed = world.getBlockState(pos);
        SoundType sound = placed.getBlock().getSoundType(placed, world, pos, player);
        world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
        stack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}
