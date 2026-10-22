package omtteam.ompd.client;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import omtteam.ompd.OpenModularPassiveDefense;

/** OMPD's client-only events, registered by the client proxy. */
@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public void renderRegisterEvent(ModelRegistryEvent event) {
        OpenModularPassiveDefense.proxy.initModelLoaders();
    }
}
