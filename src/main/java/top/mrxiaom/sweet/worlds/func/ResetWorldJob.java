package top.mrxiaom.sweet.worlds.func;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import top.mrxiaom.pluginbase.actions.ActionProviders;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.io.File;
import java.io.IOException;
import java.util.Random;

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
        reset(SweetWorlds.getInstance(), config);
    }

    public static void reset(SweetWorlds plugin, WorldConfig config) {
        World world = config.world;
        File folder = world.getWorldFolder();
        WorldResetManager manager = WorldResetManager.inst();
        for (Player player : world.getPlayers()) {
            player.teleport(manager.getSpawnLocation());
        }

        manager.markResetting(config.worldName, true);

        // 复制世界设定
        WorldCreator creator = WorldCreator.name(config.worldName).copy(world);

        // 卸载世界 并删除世界
        Bukkit.unloadWorld(world, true);
        config.world = null;
        try {
            FileUtils.deleteDirectory(folder);
        } catch (IOException e) {
            plugin.warn("删除世界 " + config.worldName + " 时出现异常", e);
        }

        // 重新创建世界
        if (config.autoReset.resetSeed) {
            creator.seed((new Random()).nextLong());
        }
        World newWorld = Bukkit.createWorld(creator);

        // 善后工作
        config.world = newWorld;
        if (config.autoReset.banUntilRestart) {
            manager.markBanned(config.worldName, true);
        }
        manager.markResetting(config.worldName, false);
        if (newWorld != null) {
            if (config.border.enable) {
                config.border.apply(newWorld);
            }
            ActionProviders.run(plugin, null, config.autoReset.commands);
            return;
        }
        plugin.warn("世界 " + config.worldName + " 重新生成失败!");
    }
}
