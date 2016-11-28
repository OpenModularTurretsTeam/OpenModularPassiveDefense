package omtteam.ompd.proxy;

import omtteam.ompd.handler.NetworkingHandler;
import omtteam.ompd.handler.recipes.RecipeHandler;
import omtteam.ompd.init.ModBlocks;
import omtteam.ompd.init.ModItems;
import omtteam.ompd.init.ModSounds;

public class CommonProxy {
    public void preInit(){
        ModItems.init();
        ModBlocks.initTileEntities();
        ModBlocks.initBlocks();
        ModSounds.init();
        RecipeHandler.initRecipes();
    }
    public void initRenderers() {

    }

    public void initHandlers() {
        NetworkingHandler.initNetworking();
    }
}