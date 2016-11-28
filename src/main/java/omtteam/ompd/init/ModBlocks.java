package omtteam.ompd.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import omtteam.omlib.tileentity.TileEntityOwnedBlock;
import omtteam.ompd.blocks.BlockFence;
import omtteam.ompd.blocks.BlockHardWall;
import omtteam.ompd.items.blocks.ItemBlockFence;
import omtteam.ompd.items.blocks.ItemBlockHardWall;

import static omtteam.ompd.reference.Names.Blocks.ownedBlock;

public class ModBlocks {


    public static Block hardWall;


    public static Block fence;
;

    public static void initBlocks() {
        hardWall = new BlockHardWall();
        GameRegistry.registerBlock(hardWall, ItemBlockHardWall.class, hardWall);

        fence = new BlockFence();
        GameRegistry.registerBlock(fence, ItemBlockFence.class, fence);
    }

    public static void initTileEntities() {
        GameRegistry.registerTileEntity(TileEntityOwnedBlock.class, ownedBlock);
    }
}
