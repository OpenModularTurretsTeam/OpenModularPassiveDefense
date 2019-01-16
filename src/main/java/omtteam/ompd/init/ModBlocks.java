package omtteam.ompd.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import omtteam.omlib.util.InitHelper;
import omtteam.ompd.blocks.BlockCamoTrap;
import omtteam.ompd.blocks.BlockFence;
import omtteam.ompd.blocks.BlockHardened;
import omtteam.ompd.blocks.BlockWall;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityPassiveOwnedBlock;

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
        GameRegistry.registerTileEntity(TileEntityPassiveOwnedBlock.class, Reference.MOD_ID + ":" + OMPDNames.Blocks.ownedBlock);
    }
}
