package top.mrxiaom.sweet.worlds.depend;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.WorldConfigManager;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Placeholders extends PlaceholderExpansion {
    private final SweetWorlds plugin;
    public Placeholders(SweetWorlds plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean register() {
        try {
            unregister();
        } catch (Throwable ignored) {}
        return super.register();
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.startsWith("reset_next_time_")) {
            String worldName = params.substring(16);
            WorldConfigManager manager = WorldConfigManager.inst();
            WorldConfig config = manager.getWorld(worldName);
            LocalDateTime nextTime = config == null ? null : config.autoReset.nextTime;
            if (nextTime == null) return "";
            return manager.format(nextTime);
        }
        if (params.startsWith("reset_next_utc_")) {
            String worldName = params.substring(15);
            WorldConfigManager manager = WorldConfigManager.inst();
            WorldConfig config = manager.getWorld(worldName);
            LocalDateTime nextTime = config == null ? null : config.autoReset.nextTime;
            if (nextTime == null) return "";
            return String.valueOf(nextTime.toEpochSecond(ZoneOffset.UTC));
        }
        if (params.startsWith("reset_remaining_time_")) {
            String worldName = params.substring(21);
            WorldConfigManager manager = WorldConfigManager.inst();
            WorldConfig config = manager.getWorld(worldName);
            LocalDateTime nextTime = config == null ? null : config.autoReset.nextTime;
            if (nextTime == null) return "";
            long now = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            long next = nextTime.toEpochSecond(ZoneOffset.UTC);
            return manager.format(Math.max(0, next - now));
        }
        return super.onRequest(player, params);
    }
}
