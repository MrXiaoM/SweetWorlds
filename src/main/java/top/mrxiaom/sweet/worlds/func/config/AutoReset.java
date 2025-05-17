package top.mrxiaom.sweet.worlds.func.config;

import org.bukkit.configuration.ConfigurationSection;
import top.mrxiaom.pluginbase.actions.ActionProviders;
import top.mrxiaom.pluginbase.api.IAction;

import java.util.List;

public class AutoReset {
    public final String cron;
    public final boolean resetSeed;
    public final boolean banUntilRestart;
    public final List<IAction> commands;

    public AutoReset(String cron, boolean resetSeed, boolean banUntilRestart, List<IAction> commands) {
        this.cron = cron;
        this.resetSeed = resetSeed;
        this.banUntilRestart = banUntilRestart;
        this.commands = commands;
    }

    public static AutoReset load(ConfigurationSection section, String world) {
        String cron = section.getString(world + ".auto-reset.cron");
        boolean resetSeed = section.getBoolean(world + ".auto-reset.reset-seed");
        boolean banUntilRestart = section.getBoolean(world + ".auto-reset.ban-until-restart");
        List<IAction> commands = ActionProviders.loadActions(section, world + ".auto-reset.commands");
        return new AutoReset(cron, resetSeed, banUntilRestart, commands);
    }
}
