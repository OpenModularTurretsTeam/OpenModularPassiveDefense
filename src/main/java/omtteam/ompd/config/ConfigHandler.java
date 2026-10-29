package omtteam.ompd.config;

import net.minecraftforge.common.config.Configuration;
import omtteam.ompd.core.DefenseStats;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;
    /** Hardness, explosion resistance and fence damage per tier (synced to clients, which need the hardness). */
    private static DefenseStats defenseStats = DefenseStats.DEFAULTS;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        config.load();
        defenseStats = DefenseStats.of(
                tierList("hardness", DefenseStats.defaultHardness(),
                        "Hardness of the hardened blocks, walls and fences of tiers 1 to 5 (stone 1.5, obsidian 50)."),
                tierList("explosionResistance", DefenseStats.defaultExplosionResistance(),
                        "Explosion resistance of the hardened blocks, walls and fences of tiers 1 to 5 (stone 6, obsidian 1200)."),
                tierList("fenceDamage", DefenseStats.defaultFenceDamage(),
                        "Damage a fence of tiers 1 to 5 deals per hit to entities touching it (up to two hits per second)."));

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static DefenseStats getDefenseStats() {
        return defenseStats;
    }

    public static void setDefenseStats(DefenseStats stats) {
        defenseStats = stats;
    }

    /** One value per tier; a list of the wrong length or with negative values is ignored (defaults used). */
    private static float[] tierList(String key, float[] defaults, String comment) {
        double[] defaultValues = new double[defaults.length];
        for (int i = 0; i < defaults.length; i++) {
            defaultValues[i] = defaults[i];
        }
        double[] values = config.get("tiers", key, defaultValues,
                comment + " Five values; an invalid list uses the defaults.", 0.0D, Float.MAX_VALUE, true,
                defaults.length).getDoubleList();
        float[] result = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = (float) values[i];
        }
        return result;
    }
}
