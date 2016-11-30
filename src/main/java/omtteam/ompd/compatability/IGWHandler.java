package omtteam.ompd.compatability;

import igwmod.api.WikiRegistry;
import omtteam.ompd.init.ModBlocks;

/**
 * Created by Keridos on 23/01/2015.
 * This Class
 */
public class IGWHandler {
    private static IGWHandler instance = null;

    private IGWHandler() {
        initTab();
    }

    public static IGWHandler getInstance() {
        if (instance == null) {
            instance = new IGWHandler();
        }
        return instance;
    }

    private void initTab() {
        WikiRegistry.registerWikiTab(new OMPDWikiTab());
        WikiRegistry.registerBlockAndItemPageEntry(ModBlocks.hardened, "block/hardened");
        WikiRegistry.registerBlockAndItemPageEntry(ModBlocks.wall, "block/wall");
        WikiRegistry.registerBlockAndItemPageEntry(ModBlocks.fence, "block/fence");
    }
}
