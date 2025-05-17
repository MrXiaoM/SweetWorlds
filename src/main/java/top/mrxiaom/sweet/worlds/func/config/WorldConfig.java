package top.mrxiaom.sweet.worlds.func.config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class WorldConfig {
    public final String worldName;
    public final World world;
    public final AutoReset autoReset;
    public final Border border;

    public WorldConfig(String worldName, World world, AutoReset autoReset, Border border) {
        this.worldName = worldName;
        this.world = world;
        this.autoReset = autoReset;
        this.border = border;
    }

    public static WorldConfig load(ConfigurationSection section, String key) {
        AutoReset autoReset = AutoReset.load(section, key);
        Border border = Border.load(section, key);
        World world = Bukkit.getWorld(key);
        if (world == null) {
            return null;
        }
        return new WorldConfig(key, world, autoReset, border);
    }
}
