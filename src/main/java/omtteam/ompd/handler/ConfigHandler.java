package omtteam.ompd.handler;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        config.load();

        if (config.hasChanged()) {
            config.save();
        }
    }
}