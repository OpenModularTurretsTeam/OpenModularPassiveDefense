package omtteam.ompd.item;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import omtteam.ompd.block.BlockHeavyDoor;
import omtteam.ompd.config.DefenseTier;
import omtteam.ompd.tileentity.HeavyDoorMotion;
import omtteam.ompd.util.BlockHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Places a {@link BlockHeavyDoor} across the player's view: cell 0 at the clicked spot, the second column to the
 * player's right, both one block up. Fails unless all four cells are free.
 */
public class ItemBlockHeavyDoor extends ItemBlock {
    public ItemBlockHeavyDoor(Block block) {
        super(block);
        this.setRegistryName(block.getRegistryName());
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
        EnumFacing doorFacing = player.getHorizontalFacing();
        for (int cell = 0; cell < HeavyDoorMotion.CELLS; cell++) {
            BlockPos cellPos = BlockHeavyDoor.cellPos(pos, doorFacing, cell);
            if (cellPos.getY() >= world.getHeight() || !player.canPlayerEdit(cellPos, facing, stack)
                    || !world.mayPlace(block, cellPos, false, facing, player)) {
                return EnumActionResult.FAIL;
            }
        }
        for (int cell = 0; cell < HeavyDoorMotion.CELLS; cell++) {
            IBlockState state = block.getDefaultState().withProperty(BlockHeavyDoor.FACING, doorFacing)
                    .withProperty(BlockHeavyDoor.CELL, cell);
            world.setBlockState(BlockHeavyDoor.cellPos(pos, doorFacing, cell), state, 2);
        }
        for (int cell = 0; cell < HeavyDoorMotion.CELLS; cell++) {
            world.notifyNeighborsOfStateChange(BlockHeavyDoor.cellPos(pos, doorFacing, cell), block, false);
        }
        BlockHelper.initPlacedPart(world, pos, player, DefenseTier.toMeta(BlockHeavyDoor.TIER));
        IBlockState placed = world.getBlockState(pos);
        SoundType sound = block.getSoundType(placed, world, pos, player);
        world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
        stack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}
