package omtteam.ompd;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.CompoundDataFixer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import omtteam.ompd.client.gui.OpenModularPassiveDefenseTab;
import omtteam.ompd.compatability.ModCompatibility;
import omtteam.ompd.fixes.TENameFix;
import omtteam.ompd.handler.ConfigHandler;
import omtteam.ompd.handler.GuiHandler;
import omtteam.ompd.proxy.CommonProxy;
import omtteam.ompd.reference.Reference;
import org.apache.logging.log4j.Logger;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, acceptedMinecraftVersions = "1.7.10", dependencies = Reference.DEPENDENCIES)
public class OpenModularPassiveDefense {
    @Mod.Instance(Reference.MOD_ID)
    public static OpenModularPassiveDefense instance;

    public final static int DATA_VERSION = 1;

    @SidedProxy(clientSide = "omtteam.ompd.proxy.ClientProxy", serverSide = "omtteam.ompd.proxy.CommonProxy")
    public static CommonProxy proxy;

    private static Logger logger;

    public static CreativeTabs modularPassiveDefenseTab;

    public static Logger getLogger() {
        return logger;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
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
        // Fixers
        CompoundDataFixer fixer = FMLCommonHandler.instance().getDataFixer();
        ModFixs modFixer = fixer.init(Reference.MOD_ID, DATA_VERSION);
        modFixer.registerFix(FixTypes.BLOCK_ENTITY, new TENameFix());
    }
}