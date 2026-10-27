package omtteam.ompd.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.ompd.block.BlockHeavyDoor;
import omtteam.ompd.core.HeavyDoorMotion;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.tileentity.TileEntityHeavyDoor;
import org.lwjgl.opengl.GL11;

/**
 * Draws the two wings of a heavy door at their current slide offset ({@link HeavyDoorMotion#wingOffset}), each as a
 * lower and an upper box with the {@link #LOWER} and {@link #UPPER} textures, shaded per face like a block.
 */
@SideOnly(Side.CLIENT)
public class HeavyDoorRenderer extends TileEntitySpecialRenderer<TileEntityHeavyDoor> {
    public static final ResourceLocation LOWER = new ResourceLocation(Reference.MOD_ID, "blocks/heavy_door_lower");
    public static final ResourceLocation UPPER = new ResourceLocation(Reference.MOD_ID, "blocks/heavy_door_upper");
    private static final double DEPTH_FROM = 6.0D / 16.0D;
    private static final double DEPTH_TO = 10.0D / 16.0D;

    @Override
    public void render(TileEntityHeavyDoor door, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        IBlockState state = door.getWorld().getBlockState(door.getPos());
        if (!(state.getBlock() instanceof BlockHeavyDoor)) {
            return;
        }
        EnumFacing facing = state.getValue(BlockHeavyDoor.FACING);
        float progress = door.getProgress(partialTicks);
        TextureMap textures = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite lower = textures.getAtlasSprite(LOWER.toString());
        TextureAtlasSprite upper = textures.getAtlasSprite(UPPER.toString());

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        int light = door.getWorld().getCombinedLight(door.getPos(), 0);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, light & 0xFFFF, light >> 16);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        for (int column = 0; column < 2; column++) {
            float[] offset = HeavyDoorMotion.wingOffset(column, progress);
            box(buffer, facing, offset[0], offset[1], 0.0D, 1.0D, lower);
            box(buffer, facing, offset[0], offset[1], 1.0D, 2.0D, upper);
        }
        tessellator.draw();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    /** A box from {@code from} to {@code to} along the door (to the right of the facing, from cell 0's left edge). */
    private static void box(BufferBuilder buffer, EnumFacing facing, double from, double to, double minY, double maxY,
                            TextureAtlasSprite sprite) {
        EnumFacing right = facing.rotateY();
        boolean positive = right.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE;
        double alongMin = positive ? from : 1.0D - to;
        double alongMax = positive ? to : 1.0D - from;
        boolean alongX = right.getAxis() == EnumFacing.Axis.X;
        double minX = alongX ? alongMin : DEPTH_FROM;
        double maxX = alongX ? alongMax : DEPTH_TO;
        double minZ = alongX ? DEPTH_FROM : alongMin;
        double maxZ = alongX ? DEPTH_TO : alongMax;
        float u0 = sprite.getMinU();
        float u1 = sprite.getMaxU();
        float v0 = sprite.getMinV();
        float v1 = sprite.getMaxV();
        quad(buffer, 0.5F, u0, u1, v0, v1, minX, minY, maxZ, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ);
        quad(buffer, 1.0F, u0, u1, v0, v1, minX, maxY, minZ, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ);
        quad(buffer, 0.8F, u0, u1, v0, v1, maxX, maxY, minZ, maxX, minY, minZ, minX, minY, minZ, minX, maxY, minZ);
        quad(buffer, 0.8F, u0, u1, v0, v1, minX, maxY, maxZ, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ);
        quad(buffer, 0.6F, u0, u1, v0, v1, minX, maxY, minZ, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ);
        quad(buffer, 0.6F, u0, u1, v0, v1, maxX, maxY, maxZ, maxX, minY, maxZ, maxX, minY, minZ, maxX, maxY, minZ);
    }

    /** One face, corners top left, bottom left, bottom right, top right seen from outside. */
    private static void quad(BufferBuilder buffer, float shade, float u0, float u1, float v0, float v1,
                             double x0, double y0, double z0, double x1, double y1, double z1,
                             double x2, double y2, double z2, double x3, double y3, double z3) {
        buffer.pos(x0, y0, z0).tex(u0, v0).color(shade, shade, shade, 1.0F).endVertex();
        buffer.pos(x1, y1, z1).tex(u0, v1).color(shade, shade, shade, 1.0F).endVertex();
        buffer.pos(x2, y2, z2).tex(u1, v1).color(shade, shade, shade, 1.0F).endVertex();
        buffer.pos(x3, y3, z3).tex(u1, v0).color(shade, shade, shade, 1.0F).endVertex();
    }
}
