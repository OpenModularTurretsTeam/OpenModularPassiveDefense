package omtteam.ompd.handler;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public static int energyUsageSmallLight;

    public static void init(File configFile) {
        Configuration config = new Configuration(configFile);
        config.load();

        energyUsageSmallLight = config.getInt("energyUsageSmallLight", "general", 4, 0, 100, "Energy Usage in RF/t for the small electric light.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}