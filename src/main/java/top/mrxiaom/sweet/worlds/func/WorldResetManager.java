package top.mrxiaom.sweet.worlds.func;

import com.google.common.collect.Iterables;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@AutoRegister
public class WorldResetManager extends AbstractModule implements Listener {
    private final List<TriggerKey> triggerKeys = new ArrayList<>();
    private DateFormat format;
    private Location spawnLocation;
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

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        try {
            format = new SimpleDateFormat(config.getString("date-format", "yyyy/MM/dd HH:mm:ss"));
        } catch (Throwable t) {
            format = SimpleDateFormat.getDateTimeInstance();
        }
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
        try {
            plugin.getQuartz().unscheduleJobs(triggerKeys);
        } catch (SchedulerException e) {
            warn("取消任务失败", e);
        }
        triggerKeys.clear();
        Collection<WorldConfig> worlds = WorldConfigManager.inst().worlds();
        for (WorldConfig world : worlds) {
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(world.worldName, "SweetWorlds-reset")
                    .withSchedule(CronScheduleBuilder.cronSchedule(world.autoReset.cron))
                    .usingJobData("world", world.worldName)
                    .build();
            try {
                Date date = plugin.getQuartz().scheduleJob(jobDetail, trigger);
                triggerKeys.add(trigger.getKey());
                Instant instant = date.toInstant();
                ZonedDateTime zoned = instant.atZone(ZoneId.systemDefault());
                world.autoReset.nextTime = zoned.toLocalDateTime();
                info("世界 " + world.worldName + " 将在 " + format.format(date) + " 重置");
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
            player.teleport(getSpawnLocation());
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
