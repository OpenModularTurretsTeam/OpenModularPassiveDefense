package omtteam.ompd.proxy;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import omtteam.ompd.handler.EventHandler;
import omtteam.ompd.handler.NetworkingHandler;
import omtteam.ompd.handler.recipes.RecipeHandler;
import omtteam.ompd.init.ModBlocks;

public class CommonProxy {
    public void preInit(){
        MinecraftForge.EVENT_BUS.register(EventHandler.getInstance());
        ModBlocks.initTileEntities();
        initHandlers();
    }

    public void init(){
        RecipeHandler.initRecipes();
    }

    public void initModelLoaders() {

    }

    protected void initHandlers() {
        NetworkingHandler.initNetworking();
    }

    public World getWorld(){
        return null;
    }
}