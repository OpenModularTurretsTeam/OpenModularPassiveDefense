package omtteam.ompd;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import omtteam.ompd.client.gui.OpenModularPassiveDefenseTab;
import omtteam.ompd.compatability.ModCompatibility;
import omtteam.ompd.handler.ConfigHandler;
import omtteam.ompd.handler.GuiHandler;
import omtteam.ompd.proxy.CommonProxy;
import omtteam.ompd.reference.Reference;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = "1.7.10", dependencies = Reference.DEPENDENCIES)
public class OpenModularPassiveDefense {
    @Mod.Instance(Reference.MOD_ID)
    public static OpenModularPassiveDefense instance;

    @SidedProxy(clientSide = "omtteam.ompd.proxy.ClientProxy", serverSide = "omtteam.ompd.proxy.CommonProxy")
    public static CommonProxy proxy;

    public static CreativeTabs modularPassiveDefenseTab;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        ModCompatibility.checkForMods();
        modularPassiveDefenseTab = OpenModularPassiveDefenseTab.getInstance();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        ModCompatibility.performModCompat();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, GuiHandler.getInstance());
    }
}