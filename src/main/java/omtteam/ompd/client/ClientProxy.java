package omtteam.ompd.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.client.render.camo.CamoBlockColor;
import omtteam.ompd.CommonProxy;
import omtteam.ompd.block.BlockHardenedDoor;
import omtteam.ompd.block.BlockHeavyDoor;
import omtteam.ompd.block.BlockTieredGate;
import omtteam.ompd.block.BlockWall;
import omtteam.ompd.client.model.BasicCamoTrapBakedModel;
import omtteam.ompd.client.render.HeavyDoorRenderer;
import omtteam.ompd.item.ItemTiered;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.registry.ModBlocks;
import omtteam.ompd.registry.ModItems;
import omtteam.ompd.tileentity.TileEntityHeavyDoor;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private void registerBlockModelAsItem(final Block block, int meta, final String blockName) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + blockName, "inventory"));
    }

    private void registerBlockModelAsItem(final Block block, int meta, final String blockName, String variantName) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + blockName, variantName));
    }

    private void registerItemModel(final Item item, int meta, final String modelName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Reference.MOD_ID + ":" + modelName, "inventory"));
    }

    @Override
    public void init() {
        super.init();
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new CamoBlockColor(), ModBlocks.camoTrap);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeavyDoor.class, new HeavyDoorRenderer());
    }

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void initModelLoaders() {
        super.initModelLoaders();
        ModelLoader.setCustomStateMapper(ModBlocks.wall, new StateMap.Builder().ignore(BlockWall.VARIANT).build());
        for (int i = 0; i < 5; i++) {
            registerBlockModelAsItem(ModBlocks.hardened, i, OMPDNames.Blocks.hardened, "tier=" + (i + 1));
            registerBlockModelAsItem(ModBlocks.fence, i, OMPDNames.Blocks.fence + "_inventory", "tier=" + (i + 1));
            registerBlockModelAsItem(ModBlocks.wall, i, OMPDNames.Blocks.wall + "_inventory", "tier=" + (i + 1));
            registerBlockModelAsItem(ModBlocks.spikes, i, OMPDNames.Blocks.spikes, "tier=" + (i + 1));
        }
        ModelLoader.setCustomStateMapper(ModBlocks.hardenedDoor, new StateMap.Builder().ignore(BlockHardenedDoor.POWERED).build());
        for (Block gate : new Block[]{ModBlocks.fenceGate, ModBlocks.wallGate, ModBlocks.tallFenceGate, ModBlocks.tallWallGate}) {
            ModelLoader.setCustomStateMapper(gate, new StateMap.Builder().ignore(BlockTieredGate.POWERED, BlockTieredGate.IN_WALL).build());
        }
        // blocks with the tier in the tile entity and the tiered items: one item model per tier, <name>_tier_<one…five>
        for (int i = 0; i < ItemTiered.TIER_NAMES.length; i++) {
            String tier = "_tier_" + ItemTiered.TIER_NAMES[i];
            registerItemModel(ModItems.hardenedPlating, i, OMPDNames.Items.hardenedPlating + tier);
            registerItemModel(Item.getItemFromBlock(ModBlocks.hardenedLamp), i, OMPDNames.Blocks.hardenedLamp + tier);
            registerItemModel(Item.getItemFromBlock(ModBlocks.hardenedDoor), i, OMPDNames.Blocks.hardenedDoor + tier);
            registerItemModel(Item.getItemFromBlock(ModBlocks.fenceGate), i, OMPDNames.Blocks.fenceGate + tier);
            registerItemModel(Item.getItemFromBlock(ModBlocks.wallGate), i, OMPDNames.Blocks.wallGate + tier);
            registerItemModel(Item.getItemFromBlock(ModBlocks.tallFenceGate), i, OMPDNames.Blocks.tallFenceGate + tier);
            registerItemModel(Item.getItemFromBlock(ModBlocks.tallWallGate), i, OMPDNames.Blocks.tallWallGate + tier);
        }
        registerItemModel(ModItems.hardenedMix, 0, OMPDNames.Items.hardenedMix);
        ModelLoader.setCustomStateMapper(ModBlocks.heavyDoor, new StateMap.Builder().ignore(BlockHeavyDoor.FACING, BlockHeavyDoor.CELL).build());
        registerItemModel(Item.getItemFromBlock(ModBlocks.heavyDoor), 0, OMPDNames.Blocks.heavyDoor);
        ModelLoader.setCustomStateMapper(ModBlocks.camoTrap, new BasicCamoTrapBakedModel.Statemapper());
        registerBlockModelAsItem(ModBlocks.camoTrap, 0, OMPDNames.Blocks.camoTrap + "_normal");
        ModelLoaderRegistry.registerLoader(new BasicCamoTrapBakedModel.ModelLoader());
    }

    @Override
    public void initHandlers() {
        super.initHandlers();
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

    @Override
    public World getWorld() {
        return Minecraft.getMinecraft().world;
    }
}