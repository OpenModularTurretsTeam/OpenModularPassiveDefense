package omtteam.ompd.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import omtteam.ompd.blocks.BlockCamoTrap;
import omtteam.ompd.blocks.BlockFence;
import omtteam.ompd.blocks.BlockHardened;
import omtteam.ompd.blocks.BlockWall;
import omtteam.ompd.items.blocks.ItemBlockFence;
import omtteam.ompd.items.blocks.ItemBlockHardened;
import omtteam.ompd.items.blocks.ItemBlockWall;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.tileentity.TileEntityPassiveOwnedBlock;

public class ModBlocks {
    public static Block hardened;
    public static Block fence;
    public static Block wall;
    public static Block camoTrap;

    private static Item hardenedItem;
    private static Item fenceItem;
    public static Item wallItem;

    public static void initBlocks() {
        hardened = new BlockHardened();
        hardenedItem = new ItemBlockHardened(hardened);
        GameRegistry.register(hardened);
        GameRegistry.register(hardenedItem);

        fence = new BlockFence();
        fenceItem = new ItemBlockFence(fence);
        GameRegistry.register(fence);
        GameRegistry.register(fenceItem);

        wall = new BlockWall();
        wallItem = new ItemBlockWall(wall);
        GameRegistry.register(wall);
        GameRegistry.register(wallItem);

        camoTrap = new BlockCamoTrap();
        GameRegistry.register(camoTrap);
    }

    public static void initTileEntities() {
        GameRegistry.registerTileEntity(TileEntityPassiveOwnedBlock.class, OMPDNames.Blocks.ownedBlock);
    }
}
