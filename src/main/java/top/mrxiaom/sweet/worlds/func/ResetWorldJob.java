package top.mrxiaom.sweet.worlds.func;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

public class ResetWorldJob implements Job {
    @Override
    public void execute(JobExecutionContext ctx) {
        String worldName = ctx.getJobDetail().getKey().getName();
        WorldConfigManager manager = WorldConfigManager.inst();
        WorldConfig config = manager.getWorld(worldName);
        if (config != null) {
            manager.plugin.getScheduler().runTask(() -> reset(config));
        }
    }

    public static void reset(WorldConfig config) {
        // TODO: 进行世界重置操作

    }
}
