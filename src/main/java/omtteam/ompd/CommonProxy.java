package omtteam.ompd;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import omtteam.omlib.config.ConfigSync;
import omtteam.ompd.config.ConfigHandler;
import omtteam.ompd.core.DefenseStats;
import omtteam.ompd.handler.EventHandler;
import omtteam.ompd.reference.Reference;
import omtteam.ompd.registry.ModBlocks;

public class CommonProxy {
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(EventHandler.getInstance());
        ModBlocks.initTileEntities();
        initHandlers();
        // clients need the server's hardness for mining progress
        ConfigSync.register(Reference.MOD_ID);
        ConfigSync.registerValue(Reference.MOD_ID, "tiers", DefenseStats.class, ConfigHandler::getDefenseStats,
                ConfigHandler::setDefenseStats);
    }

    public void init() {
        //RecipeHandler.initRecipes();
    }

    public void initModelLoaders() {

    }

    /** Common handlers; the client proxy adds its own. OMPD has no network channel: the camouflage menu uses OMLib's. */
    protected void initHandlers() {
    }

    public World getWorld() {
        return null;
    }
}