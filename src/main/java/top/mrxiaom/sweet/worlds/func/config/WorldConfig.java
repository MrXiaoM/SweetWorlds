package top.mrxiaom.sweet.worlds.func.config;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.sweet.worlds.func.AbstractModule;

public class WorldConfig {
    public final String worldName;
    public World world;
    public final AutoReset autoReset;
    public final Border border;

    public WorldConfig(String worldName, World world, AutoReset autoReset, Border border) {
        this.worldName = worldName;
        this.world = world;
        this.autoReset = autoReset;
        this.border = border;
    }

    @Nullable
    public static WorldConfig load(AbstractModule parent, ConfigurationSection section, String key) {
        AutoReset autoReset = AutoReset.load(parent, section, key);
        if (autoReset == null) {
            return null;
        }
        Border border = Border.load(parent, section, key);
        if (border == null) {
            return null;
        }
        World world = Bukkit.getWorld(key);
        if (world == null) {
            parent.warn("[worlds.yml/" + key + "] 找不到世界 " + key);
            return null;
        }
        return new WorldConfig(key, world, autoReset, border);
    }
}
