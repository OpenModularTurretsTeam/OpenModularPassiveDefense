package omtteam.ompd.client.render.models;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.omlib.render.CamoBakedModel;
import omtteam.ompd.reference.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Created by Keridos on 29/01/17.
 * This Class
 */

@SideOnly(Side.CLIENT)
public class BasicCamoTrapBakedModel extends CamoBakedModel {
    private static final ResourceLocation FAKE_LOCATION = new ResourceLocation("ompd", "models/block/custom/camo_trap");

    private final TextureAtlasSprite particle;


    private BasicCamoTrapBakedModel(List<IBakedModel> list, TextureAtlasSprite part) {
        super(list);
        particle = part;
    }

    @Override
    protected IBakedModel getModel(List<IBakedModel> list, @Nullable IBlockState state) {
        return list.get(0);
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particle;
    }

    @Nonnull
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }

    public static class Model implements IModel {
        public Model() {

        }

        @Override
        @Nonnull
        public Collection<ResourceLocation> getDependencies() {
            List<ResourceLocation> list = new ArrayList<>();
            list.add(new ModelResourceLocation("ompd:camo_trap_normal"));
            return list;
        }

        @Override
        @Nonnull
        public Collection<ResourceLocation> getTextures() {
            return Collections.emptyList();
        }

        @Override
        @Nonnull
        @ParametersAreNonnullByDefault
        public IBakedModel bake(IModelState state, VertexFormat format, java.util.function.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            List<IBakedModel> list = new ArrayList<>();
            try {
                list.add(ModelLoaderRegistry.getModel(new ModelResourceLocation("ompd:camo_trap_normal")).bake(state, format, bakedTextureGetter));
            } catch (Exception e) {
                e.printStackTrace();
            }

            TextureAtlasSprite part = bakedTextureGetter.apply(new ResourceLocation("ompd", "blocks/camo_trap"));
            return new BasicCamoTrapBakedModel(list, part);
        }
    }

    public static class ModelLoader implements ICustomModelLoader {

        @Override
        @ParametersAreNonnullByDefault
        public boolean accepts(ResourceLocation modelLocation) {
            return (modelLocation.getResourceDomain().equals(Reference.MOD_ID) && modelLocation.equals(FAKE_LOCATION));
        }

        @Override
        @Nonnull
        @ParametersAreNonnullByDefault
        public IModel loadModel(ResourceLocation modelLocation) throws Exception {
            return new Model();
        }

        @Override
        @ParametersAreNonnullByDefault
        public void onResourceManagerReload(IResourceManager resourceManager) {
            // Nothing to do
        }
    }

    public static class Statemapper extends StateMapperBase {
        static final ModelResourceLocation LOCATION = new ModelResourceLocation("ompd:camo_trap", "normal");

        @Override
        @Nonnull
        protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
            return LOCATION;
        }
    }
}
