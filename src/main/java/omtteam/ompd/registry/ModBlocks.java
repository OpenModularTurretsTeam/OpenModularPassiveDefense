package omtteam.ompd.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import omtteam.omlib.util.InitHelper;
import omtteam.ompd.block.BlockCamoTrap;
import omtteam.ompd.block.BlockFence;
import omtteam.ompd.block.BlockHardened;
import omtteam.ompd.block.BlockWall;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityCamo;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;

public class ModBlocks {
    public static Block hardened;
    public static Block fence;
    public static Block wall;
    public static Block camoTrap;

    public static Item wallItem;

    public static void initBlocks(IForgeRegistry<Block> registry) {
        hardened = new BlockHardened();
        InitHelper.registerBlock(hardened, registry, ModItems.subBlocks);

        fence = new BlockFence();
        InitHelper.registerBlock(fence, registry, ModItems.subBlocks);

        wall = new BlockWall();
        InitHelper.registerBlock(wall, registry, ModItems.subBlocks);

        camoTrap = new BlockCamoTrap();
        InitHelper.registerBlock(camoTrap, registry, ModItems.subBlocks);
    }

    public static void initTileEntities() {
        InitHelper.registerTileEntity(TileEntityPassiveTiered.class, Reference.MOD_ID, OMPDNames.Blocks.ownedBlock);
        InitHelper.registerTileEntity(TileEntityCamo.class, Reference.MOD_ID, OMPDNames.Blocks.camoTrap);
    }
}
