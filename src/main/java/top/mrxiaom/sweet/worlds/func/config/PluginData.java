package top.mrxiaom.sweet.worlds.func.config;

import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.AbstractModule;

import java.io.File;
import java.io.IOException;

@AutoRegister
public class PluginData extends AbstractModule {
    private final File file;
    private Location spawnLocation;
    public PluginData(SweetWorlds plugin) {
        super(plugin);
        this.file = plugin.resolve("./data.yml");
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    private static boolean has(String plugin) {
        return Bukkit.getPluginManager().isPluginEnabled(plugin);
    }
    @Override
    public void reloadConfig(MemoryConfiguration pluginConfig) {
        if (!file.exists()) {
            YamlConfiguration config = new YamlConfiguration();
            boolean success = false;
            if (has("CMI")) {
                // TODO: 从 CMI 读取主城坐标
            } else if (has("Essentials")) {
                // TODO: 从 Essentials 读取主城坐标
            } else {
                World first = Iterables.getFirst(Bukkit.getWorlds(), null);
                if (first != null) {
                    Location loc = first.getSpawnLocation();
                    config.set("server-spawn.world", first.getName());
                    config.set("server-spawn.x", loc.getX());
                    config.set("server-spawn.y", loc.getY());
                    config.set("server-spawn.z", loc.getZ());
                    success = true;
                }
            }
            if (!success) {
                config.set("server-spawn.world", "world");
                config.set("server-spawn.x", -0.5);
                config.set("server-spawn.y", 70);
                config.set("server-spawn.z", -0.5);
            }
            try {
                config.save(file);
            } catch (IOException e) {
                warn("保存 data.yml 时出现异常", e);
            }
        }
        reload();
    }

    private void reload() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String worldName = config.getString("server-spawn.world", "world");
        {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                world = Iterables.getFirst(Bukkit.getWorlds(), null);
                if (world != null) {
                    warn("找不到主城世界 " + worldName + "，将改用第一个世界 " + world.getName());
                } else {
                    warn("找不到主城世界 " + worldName);
                }
            }
            double x = config.getDouble("server-spawn.x", -0.5);
            double y = config.getDouble("server-spawn.y", 70);
            double z = config.getDouble("server-spawn.z", -0.5);
            spawnLocation = new Location(world, x, y, z);
        }
    }

    public static PluginData inst() {
        return instanceOf(PluginData.class);
    }
}
