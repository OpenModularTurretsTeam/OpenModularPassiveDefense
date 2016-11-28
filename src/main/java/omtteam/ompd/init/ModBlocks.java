package omtteam.ompd.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;
import omtteam.ompd.blocks.BlockFence;
import omtteam.ompd.blocks.BlockHardened;
import omtteam.ompd.blocks.BlockWall;
import omtteam.ompd.items.blocks.ItemBlockFence;
import omtteam.ompd.items.blocks.ItemBlockHarded;
import omtteam.ompd.items.blocks.ItemBlockWall;
import omtteam.ompd.reference.Names;
import omtteam.ompd.tileentity.TileEntityPassiveOwnedBlock;

import static omtteam.ompd.reference.Names.Blocks.ownedBlock;

public class ModBlocks {
    public static Block hardened;
    public static Block fence;
    public static Block wall;

    public static void initBlocks() {
        hardened = new BlockHardened();
        GameRegistry.registerBlock(hardened, ItemBlockHarded.class, Names.Blocks.hardened);

        fence = new BlockFence();
        GameRegistry.registerBlock(fence, ItemBlockFence.class, Names.Blocks.fence);

        wall = new BlockWall();
        GameRegistry.registerBlock(wall, ItemBlockWall.class, Names.Blocks.wall);
    }

    public static void initTileEntities() {
        GameRegistry.registerTileEntity(TileEntityPassiveOwnedBlock.class, ownedBlock);
    }
}
