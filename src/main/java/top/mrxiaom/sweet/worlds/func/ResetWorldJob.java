package top.mrxiaom.sweet.worlds.func;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import top.mrxiaom.pluginbase.actions.ActionProviders;
import top.mrxiaom.pluginbase.utils.Pair;
import top.mrxiaom.sweet.worlds.Messages;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.config.PluginData;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class ResetWorldJob implements Job {
    @Override
    public void execute(JobExecutionContext ctx) {
        String world = ctx.getTrigger().getJobDataMap().getString("world");
        WorldConfigManager manager = WorldConfigManager.inst();
        WorldConfig config = manager.getWorld(world);
        if (config != null) {
            manager.plugin.info(Messages.logs__reset__auto.str(Pair.of("%world%", world)));
            manager.plugin.getScheduler().runTask(() -> reset(config));
            Date next = ctx.getTrigger().getNextFireTime();
            if (next == null) {
                manager.plugin.info(Messages.logs__reset__next__end.str());
            } else {
                manager.plugin.info(Messages.logs__reset__next__time.str(Pair.of("%time%", manager.format(next))));
            }
        } else {
            manager.plugin.warn(Messages.logs__reset__not_found.str(Pair.of("%world%", world)));
        }
    }

    public static void reset(WorldConfig config) {
        reset(SweetWorlds.getInstance(), config);
    }

    public static void reset(SweetWorlds plugin, WorldConfig config) {
        World world = config.world;
        File folder = world.getWorldFolder();
        WorldResetManager manager = WorldResetManager.inst();
        Location spawn = PluginData.inst().getSpawnLocation();
        for (Player player : world.getPlayers()) {
            Messages.teleport__resetting.tm(player);
            player.teleport(spawn);
        }

        manager.markResetting(config.worldName, true);

        // 复制世界设定
        WorldCreator creator = WorldCreator.name(config.worldName).copy(world);

        // 卸载世界 并删除世界
        Bukkit.unloadWorld(world, true);
        config.world = null;
        plugin.warn(Messages.logs__world__unloaded.str(Pair.of("%world%", config.worldName)));
        try {
            FileUtils.deleteDirectory(folder);
            plugin.warn(Messages.logs__world__deleted.str(Pair.of("%folder%", folder)));
        } catch (IOException e) {
            plugin.warn(Messages.logs__world__delete_error.str(Pair.of("%world%", config.worldName)));
        }

        // 重新创建世界
        if (config.autoReset.resetSeed) {
            creator.seed((new Random()).nextLong());
        }
        plugin.warn(Messages.logs__world__creating.str(Pair.of("%world%", config.worldName)));
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
            plugin.warn(Messages.logs__reset__done.str(Pair.of("%world%", config.worldName)));
            ActionProviders.run(plugin, null, config.autoReset.commands);
            return;
        }
        plugin.warn(Messages.logs__reset__failed.str(Pair.of("%world%", config.worldName)));
    }
}
