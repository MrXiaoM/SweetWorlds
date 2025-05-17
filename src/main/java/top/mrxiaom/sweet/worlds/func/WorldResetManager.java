package top.mrxiaom.sweet.worlds.func;

import org.bukkit.configuration.MemoryConfiguration;
import org.quartz.*;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@AutoRegister
public class WorldResetManager extends AbstractModule {
    private final List<TriggerKey> triggerKeys = new ArrayList<>();
    private DateFormat format;
    public WorldResetManager(SweetWorlds plugin) {
        super(plugin);
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        try {
            format = new SimpleDateFormat(config.getString("date-format", "yyyy/MM/dd HH:mm:ss"));
        } catch (Throwable t) {
            format = SimpleDateFormat.getDateTimeInstance();
        }
        for (TriggerKey key : triggerKeys) try {
            plugin.getQuartz().unscheduleJob(key);
        } catch (SchedulerException e) {
            warn("取消任务 " + key + " 失败", e);
        }
        triggerKeys.clear();
        Collection<WorldConfig> worlds = WorldConfigManager.inst().worlds();
        for (WorldConfig world : worlds) {
            JobDetail jobDetail = JobBuilder.newJob(ResetWorldJob.class)
                    .withIdentity(world.worldName, "SweetWorlds-reset")
                    .build();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(world.worldName, "SweetWorlds-reset")
                    .withSchedule(CronScheduleBuilder.cronSchedule(world.autoReset.cron))
                    .build();
            try {
                Date date = plugin.getQuartz().scheduleJob(jobDetail, trigger);
                triggerKeys.add(trigger.getKey());
                info("世界 " + world.worldName + " 将在 " + format.format(date) + " 重置");
            } catch (SchedulerException e) {
                warn("为世界 " + world.worldName + " 创建重置任务失败", e);
            }
        }
    }
}
