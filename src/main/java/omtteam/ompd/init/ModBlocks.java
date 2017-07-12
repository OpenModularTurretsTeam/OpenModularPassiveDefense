package omtteam.ompd.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import omtteam.omlib.util.InitHelper;
import omtteam.ompd.blocks.BlockCamoTrap;
import omtteam.ompd.blocks.BlockFence;
import omtteam.ompd.blocks.BlockHardened;
import omtteam.ompd.blocks.BlockWall;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.tileentity.TileEntityPassiveOwnedBlock;

public class ModBlocks {
    public static Block hardened;
    public static Block fence;
    public static Block wall;
    public static Block camoTrap;

    public static Item wallItem;

    public static void initBlocks() {
        hardened = new BlockHardened();
        InitHelper.registerBlock(hardened);

        fence = new BlockFence();
        InitHelper.registerBlock(fence);

        wall = new BlockWall();
        InitHelper.registerBlock(wall);

        camoTrap = new BlockCamoTrap();
        GameRegistry.register(camoTrap);
    }

    public static void initTileEntities() {
        GameRegistry.registerTileEntity(TileEntityPassiveOwnedBlock.class, OMPDNames.Blocks.ownedBlock);
    }
}
