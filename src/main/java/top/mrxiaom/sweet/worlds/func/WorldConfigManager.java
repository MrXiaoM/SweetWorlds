package top.mrxiaom.sweet.worlds.func;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@AutoRegister
public class WorldConfigManager extends AbstractModule {
    private final Map<String, WorldConfig> worldConfigMap = new HashMap<>();
    private DateFormat format;
    private DateTimeFormatter formatter;
    private String unitDays, unitDay, unitHours, unitHour, unitMinutes, unitMinute, unitSeconds, unitSecond;
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

    public String format(Date date) {
        return format.format(date);
    }

    public String format(LocalDateTime date) {
        return date.format(formatter);
    }

    public String format(long timeSeconds) {
        long days = timeSeconds / 86400;
        long hours = (timeSeconds / 3600) % 24;
        long minutes = (timeSeconds / 60) % 60;
        long seconds = timeSeconds % 60;
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(days > 1 ? unitDays : unitDay);
        }
        if (hours > 0 || days > 0) {
            sb.append(hours).append(hours > 1 ? unitHours : unitHour);
        }
        if (minutes > 0 || hours > 0 || days > 0) {
            sb.append(minutes).append(minutes > 1 ? unitMinutes : unitMinute);
        }
        sb.append(seconds).append(seconds > 1 ? unitSeconds : unitSecond);
        return sb.toString();
    }

    @Override
    public void reloadConfig(MemoryConfiguration pluginConfig) {
        String formatString = pluginConfig.getString("date-format", "yyyy/MM/dd HH:mm:ss");
        try {
            format = new SimpleDateFormat(formatString);
        } catch (Throwable t) {
            format = SimpleDateFormat.getDateTimeInstance();
        }
        try {
            formatter = DateTimeFormatter.ofPattern(formatString);
        } catch (Throwable t) {
            formatter = DateTimeFormatter.ISO_DATE_TIME;
        }
        unitDays = pluginConfig.getString("time-units.days", "天");
        unitDay = pluginConfig.getString("time-units.day", "天");
        unitHours = pluginConfig.getString("time-units.hours", "时");
        unitHour = pluginConfig.getString("time-units.hour", "时");
        unitMinutes = pluginConfig.getString("time-units.minutes", "分");
        unitMinute = pluginConfig.getString("time-units.minute", "分");
        unitSeconds = pluginConfig.getString("time-units.seconds", "秒");
        unitSecond = pluginConfig.getString("time-units.second", "秒");

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
