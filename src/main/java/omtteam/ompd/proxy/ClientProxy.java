package omtteam.ompd.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.render.CamoBlockColor;
import omtteam.ompd.blocks.BlockWall;
import omtteam.ompd.client.render.models.BasicCamoTrapBakedModel;
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
    public void init() {
        super.init();
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new CamoBlockColor(), ModBlocks.camoTrap);
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
        }
        ModelLoader.setCustomStateMapper(ModBlocks.camoTrap, new BasicCamoTrapBakedModel.Statemapper());
        registerBlockModelAsItem(ModBlocks.camoTrap, 0, OMPDNames.Blocks.camoTrap + "_normal");
        ModelLoaderRegistry.registerLoader(new BasicCamoTrapBakedModel.ModelLoader());
    }

    @Override
    public void initHandlers() {
        super.initHandlers();
    }

    @Override
    public World getWorld() {
        return Minecraft.getMinecraft().world;
    }
}