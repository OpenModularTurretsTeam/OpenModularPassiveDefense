package omtteam.ompd.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;
import omtteam.omlib.util.InitHelper;
import omtteam.ompd.block.BlockCamoTrap;
import omtteam.ompd.block.BlockFence;
import omtteam.ompd.block.BlockHardened;
import omtteam.ompd.block.BlockHardenedDoor;
import omtteam.ompd.block.BlockHardenedLamp;
import omtteam.ompd.block.BlockHeavyDoor;
import omtteam.ompd.block.BlockSpikes;
import omtteam.ompd.block.BlockTallGate;
import omtteam.ompd.block.BlockTieredGate;
import omtteam.ompd.block.BlockWall;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityCamo;
import omtteam.ompd.tileentity.TileEntityHeavyDoor;
import omtteam.ompd.tileentity.TileEntityPassiveTiered;

public class ModBlocks {
    public static Block hardened;
    public static Block fence;
    public static Block wall;
    public static Block camoTrap;
    public static Block hardenedLamp;
    public static Block hardenedDoor;
    public static Block heavyDoor;
    public static Block spikes;
    public static Block fenceGate;
    public static Block wallGate;
    public static Block tallFenceGate;
    public static Block tallWallGate;

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

        hardenedLamp = new BlockHardenedLamp();
        InitHelper.registerBlock(hardenedLamp, registry, ModItems.subBlocks);

        hardenedDoor = new BlockHardenedDoor();
        InitHelper.registerBlock(hardenedDoor, registry, ModItems.subBlocks);

        heavyDoor = new BlockHeavyDoor();
        InitHelper.registerBlock(heavyDoor, registry, ModItems.subBlocks);

        spikes = new BlockSpikes();
        InitHelper.registerBlock(spikes, registry, ModItems.subBlocks);

        fenceGate = new BlockTieredGate(OMPDNames.Blocks.fenceGate);
        InitHelper.registerBlock(fenceGate, registry, ModItems.subBlocks);

        wallGate = new BlockTieredGate(OMPDNames.Blocks.wallGate);
        InitHelper.registerBlock(wallGate, registry, ModItems.subBlocks);

        tallFenceGate = new BlockTallGate(OMPDNames.Blocks.tallFenceGate);
        InitHelper.registerBlock(tallFenceGate, registry, ModItems.subBlocks);

        tallWallGate = new BlockTallGate(OMPDNames.Blocks.tallWallGate);
        InitHelper.registerBlock(tallWallGate, registry, ModItems.subBlocks);
    }

    public static void initTileEntities() {
        InitHelper.registerTileEntity(TileEntityPassiveTiered.class, Reference.MOD_ID, OMPDNames.Blocks.ownedBlock);
        InitHelper.registerTileEntity(TileEntityCamo.class, Reference.MOD_ID, OMPDNames.Blocks.camoTrap);
        InitHelper.registerTileEntity(TileEntityHeavyDoor.class, Reference.MOD_ID, OMPDNames.Blocks.heavyDoor);
    }
}
