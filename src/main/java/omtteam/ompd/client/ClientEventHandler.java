package omtteam.ompd.client;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.client.render.HeavyDoorRenderer;

/** OMPD's client-only events, registered by the client proxy. */
@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public void renderRegisterEvent(ModelRegistryEvent event) {
        OpenModularPassiveDefense.proxy.initModelLoaders();
    }

    /** The heavy door's wing textures are drawn by its renderer, no block model references them. */
    @SubscribeEvent
    public void textureStitchEvent(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(HeavyDoorRenderer.LOWER);
        event.getMap().registerSprite(HeavyDoorRenderer.UPPER);
    }
}
