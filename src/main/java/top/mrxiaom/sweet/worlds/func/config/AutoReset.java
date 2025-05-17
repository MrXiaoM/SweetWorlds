package top.mrxiaom.sweet.worlds.func.config;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import org.quartz.CronExpression;
import top.mrxiaom.pluginbase.actions.ActionProviders;
import top.mrxiaom.pluginbase.api.IAction;
import top.mrxiaom.sweet.worlds.func.AbstractModule;

import java.util.List;

public class AutoReset {
    public final CronExpression cron;
    public final boolean resetSeed;
    public final boolean banUntilRestart;
    public final List<IAction> commands;

    public AutoReset(CronExpression cron, boolean resetSeed, boolean banUntilRestart, List<IAction> commands) {
        this.cron = cron;
        this.resetSeed = resetSeed;
        this.banUntilRestart = banUntilRestart;
        this.commands = commands;
    }

    @Nullable
    public static AutoReset load(AbstractModule parent, ConfigurationSection section, String world) {
        String cronStr = section.getString(world + ".auto-reset.cron", "INVALID");
        CronExpression cron;
        try {
            cron = new CronExpression(cronStr);
        } catch (Throwable e) {
            parent.warn("[worlds.yml/" + world + "] cron 表达式读取失败，请检查是否存在输入错误");
            return null;
        }
        boolean resetSeed = section.getBoolean(world + ".auto-reset.reset-seed");
        boolean banUntilRestart = section.getBoolean(world + ".auto-reset.ban-until-restart");
        List<IAction> commands = ActionProviders.loadActions(section, world + ".auto-reset.commands");
        return new AutoReset(cron, resetSeed, banUntilRestart, commands);
    }
}
