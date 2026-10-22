package omtteam.ompd;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import omtteam.ompd.handler.EventHandler;
import omtteam.ompd.network.NetworkingHandler;
import omtteam.ompd.registry.ModBlocks;

public class CommonProxy {
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(EventHandler.getInstance());
        ModBlocks.initTileEntities();
        initHandlers();
    }

    public void init() {
        //RecipeHandler.initRecipes();
    }

    public void initModelLoaders() {

    }

    protected void initHandlers() {
        NetworkingHandler.initNetworking();
    }

    public World getWorld() {
        return null;
    }
}