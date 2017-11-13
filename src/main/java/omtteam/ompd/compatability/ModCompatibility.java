package omtteam.ompd.compatability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import omtteam.ompd.reference.Reference;

import java.util.logging.Logger;

/**
 * Created by Keridos on 23/01/2015. This Class
 */
public class ModCompatibility {
    private static Logger logger;

    public static void checkForMods() {
        logger = Logger.getLogger("OpenModularPassiveDefense");
    }

    private static void addVersionCheckerInfo() {
        NBTTagCompound versionchecker = new NBTTagCompound();
        versionchecker.setString("curseProjectName", "opmd");
        versionchecker.setString("curseFilenameParser", "ompd-1.12.2-[].jar");
        versionchecker.setString("modDisplayName", "Open Modular Passive Defense");
        versionchecker.setString("oldVersion", Reference.VERSION);
        FMLInterModComms.sendRuntimeMessage("omtteam/ompd", "VersionChecker", "addCurseCheck", versionchecker);
    }

    public static void performModCompat() {
        FMLInterModComms.sendMessage("Waila", "register",
                                     "omtteam.ompd.compatability.WailaTileHandler.callbackRegister");

    }
}
