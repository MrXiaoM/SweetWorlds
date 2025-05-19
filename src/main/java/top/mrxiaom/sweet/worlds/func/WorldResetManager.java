package top.mrxiaom.sweet.worlds.func;

import org.bukkit.Location;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.quartz.*;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.config.PluginData;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@AutoRegister
public class WorldResetManager extends AbstractModule implements Listener {
    private final List<TriggerKey> triggerKeys = new ArrayList<>();
    private final Set<String> resettingWorlds = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    private final Set<String> bannedWorlds = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    private final JobDetail jobDetail = JobBuilder.newJob(ResetWorldJob.class)
            .withIdentity("reset-world")
            .build();
    public WorldResetManager(SweetWorlds plugin) {
        super(plugin);
        registerEvents();
    }

    public void markResetting(String worldName, boolean state) {
        if (state) {
            resettingWorlds.add(worldName);
        } else {
            resettingWorlds.remove(worldName);
        }
    }

    public void markBanned(String worldName, boolean state) {
        if (state) {
            bannedWorlds.add(worldName);
        } else {
            bannedWorlds.remove(worldName);
        }
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        try {
            plugin.getQuartz().unscheduleJobs(triggerKeys);
        } catch (SchedulerException e) {
            warn("取消任务失败", e);
        }
        triggerKeys.clear();
        WorldConfigManager manager = WorldConfigManager.inst();
        Collection<WorldConfig> worlds = manager.worlds();
        for (WorldConfig world : worlds) {
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(world.worldName, "SweetWorlds-reset")
                    .withSchedule(CronScheduleBuilder.cronSchedule(world.autoReset.cron))
                    .usingJobData("world", world.worldName)
                    .build();
            try {
                Date date = plugin.getQuartz().scheduleJob(jobDetail, trigger);
                if (date == null) {
                    info("世界 " + world.worldName + " 没有下次重置时间");
                } else {
                    triggerKeys.add(trigger.getKey());
                    Instant instant = date.toInstant();
                    ZonedDateTime zoned = instant.atZone(ZoneId.systemDefault());
                    world.autoReset.nextTime = zoned.toLocalDateTime();
                    info("世界 " + world.worldName + " 将在 " + manager.format(date) + " 重置");
                }
            } catch (SchedulerException e) {
                warn("为世界 " + world.worldName + " 创建重置任务失败", e);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String name = player.getWorld().getName();
        if (resettingWorlds.contains(name) || bannedWorlds.contains(name)) {
            player.teleport(PluginData.inst().getSpawnLocation());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.isCancelled()) return;
        Location to = e.getTo();
        Player player = e.getPlayer();
        String name = to == null || to.getWorld() == null ? null : to.getWorld().getName();
        if (name == null) return;
        if (resettingWorlds.contains(name)) {
            t(player, "&e世界正在重置，请稍等片刻");
            e.setCancelled(true);
            return;
        }
        if (bannedWorlds.contains(name)) {
            t(player, "&e休整中，请在服务器重启后再进入该世界");
            e.setCancelled(true);
            return;
        }
    }

    public static WorldResetManager inst() {
        return instanceOf(WorldResetManager.class);
    }
}
