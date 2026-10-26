package omtteam.ompd.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;
    /** Whether the camouflage trap accepts glass, ice and leaves as a disguise (see {@code ICamoSupport}). */
    public static boolean allowTransparentDisguises = true;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        config.load();
        allowTransparentDisguises = config.getBoolean("allowTransparentDisguises", "camo trap", true,
                "The camouflage trap also accepts transparent full blocks (glass, ice, leaves) as a disguise.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}