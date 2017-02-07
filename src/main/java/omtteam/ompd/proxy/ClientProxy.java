package omtteam.ompd.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.ompd.blocks.BlockWall;
import omtteam.ompd.compatability.IGWHandler;
import omtteam.ompd.compatability.ModCompatibility;
import omtteam.ompd.init.ModBlocks;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private void registerItemModel(final Item item, int meta) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName().toString().toLowerCase()));
    }

    private void registerItemModel(final Item item, int meta, final String variantName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(item.getRegistryName().toString().toLowerCase()), variantName));
    }

    private void registerItemModel(final Item item, int meta, final String customName, boolean useCustomName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + customName.toLowerCase()));
    }

    private void registerBlockModelAsItem(final Block block, int meta, final String blockName) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + blockName, "inventory"));
    }

    private void registerBlockModelAsItem(final Block block, int meta, final String blockName, String variantName) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + blockName, variantName));
    }

    @Override
    public void preInit() {
        super.preInit();
        ModelLoader.setCustomStateMapper(ModBlocks.wall, new StateMap.Builder().ignore(BlockWall.VARIANT).build());
        for (int i = 0; i < 5; i++) {
            registerBlockModelAsItem(ModBlocks.hardened, i, OMPDNames.Blocks.hardened, "tier=" + (i + 1));
            registerBlockModelAsItem(ModBlocks.fence , i , OMPDNames.Blocks.fence + "_inventory","tier="+ (i + 1));
            registerBlockModelAsItem(ModBlocks.wall , i , OMPDNames.Blocks.wall+ "_inventory","tier="+ (i + 1));
        }
        //registerBlockModelAsItem(ModBlocks.camoTrap,0 , OMPDNames.Blocks.camoTrap);

        //ModelLoaderRegistry.registerLoader(new CamoTrapBakedModel.ModelLoader());
        //ModelLoader.setCustomStateMapper(ModBlocks.camoTrap, new CamoTrapBakedModel.Statemapper());
    }

    @Override
    public void initRenderers() {
        super.initRenderers();

        //ToolTips tooltips = new ToolTips();
        //MinecraftForge.EVENT_BUS.register(tooltips);
    }

    @Override
    public void initHandlers() {
        super.initHandlers();
        if (ModCompatibility.IGWModLoaded) {
            ModCompatibility.igwHandler = IGWHandler.getInstance();
        }
    }
}