package top.mrxiaom.sweet.worlds.func;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AutoRegister
public class WorldConfigManager extends AbstractModule {
    private final Map<String, WorldConfig> worldConfigMap = new HashMap<>();
    public WorldConfigManager(SweetWorlds plugin) {
        super(plugin);
    }

    @Override
    public int priority() {
        return 999;
    }

    public Collection<WorldConfig> worlds() {
        return worldConfigMap.values();
    }

    public Set<String> keys() {
        return worldConfigMap.keySet();
    }

    @Nullable
    public WorldConfig getWorld(String name) {
        return worldConfigMap.get(name);
    }

    @Override
    public void reloadConfig(MemoryConfiguration pluginConfig) {
        File file = plugin.resolve("./worlds.yml");
        if (!file.exists()) {
            plugin.saveResource("worlds.yml");
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        worldConfigMap.clear();
        ConfigurationSection section = config.getConfigurationSection("worlds");
        if (section != null) for (String key : section.getKeys(false)) {
            WorldConfig loaded = WorldConfig.load(this, section, key);
            if (loaded != null) {
                worldConfigMap.put(loaded.worldName, loaded);
            }
        }
        info("加载了 " + worldConfigMap.size() + " 个世界配置");
    }

    public static WorldConfigManager inst() {
        return instanceOf(WorldConfigManager.class);
    }
}
