package omtteam.ompd.compat;

import omtteam.omlib.compat.hwyla.OMLibWailaPlugin;

/**
 * Registers OMPD's Hwyla handlers with OMLib's plugin. A class of its own, called only when Hwyla is loaded: the
 * handlers implement Hwyla's interfaces, so a class creating them cannot even be loaded without Hwyla.
 */
public final class OMPDWailaWrapper {
    private OMPDWailaWrapper() {
    }

    public static void register() {
        OMLibWailaPlugin.addDataProvider(new WailaTileHandler());
        OMLibWailaPlugin.addDataProvider(new WailaCamoTrapHandler());
    }
}
