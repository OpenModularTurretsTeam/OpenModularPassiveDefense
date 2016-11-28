package omtteam.ompd.handler;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {

    public static boolean IGWNotification;

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);
        config.load();


        IGWNotification = config.get("ModCompatibility", "Enable IGW Mod notification", true).getBoolean();

        if (config.hasChanged()) {
            config.save();
        }
    }
}