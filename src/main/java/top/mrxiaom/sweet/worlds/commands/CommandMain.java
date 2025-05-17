package top.mrxiaom.sweet.worlds.commands;
        
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.AbstractModule;
import top.mrxiaom.sweet.worlds.func.ResetWorldJob;
import top.mrxiaom.sweet.worlds.func.WorldConfigManager;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.util.*;

@AutoRegister
public class CommandMain extends AbstractModule implements CommandExecutor, TabCompleter, Listener {
    public CommandMain(SweetWorlds plugin) {
        super(plugin);
        registerCommand("sweetworlds", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 2 && "reset".equalsIgnoreCase(args[0]) && sender.isOp()) {
            WorldConfig config = WorldConfigManager.inst().getWorld(args[1]);
            if (config == null) {
                return t(sender, "&e没有这个世界的配置");
            }
            if (args.length != 3 || !"confirm".equalsIgnoreCase(args[2])) {
                return t(sender, "",
                        "  &e警告，你正在进行世界重置操作。",
                        "  &e这个操作无法被撤销，你确定要这么做吗？",
                        "  &f要进行世界重置，请执行以下命令",
                        "  &b/sweetworlds reset " + args[1] + " confirm",
                        "");
            }
            t(sender, "&a正在执行世界重置操作，详见服务器控制台");
            plugin.getScheduler().runTask(() -> ResetWorldJob.reset(plugin, config));
            return true;
        }
        if (args.length == 1 && "reload".equalsIgnoreCase(args[0]) && sender.isOp()) {
            plugin.reloadConfig();
            return t(sender, "&a配置文件已重载");
        }
        return true;
    }

    private static final List<String> emptyList = Lists.newArrayList();
    private static final List<String> listArg0 = Lists.newArrayList();
    private static final List<String> listOpArg0 = Lists.newArrayList(
            "reset", "reload");
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return startsWith(sender.isOp() ? listOpArg0 : listArg0, args[0]);
        }
        if (args.length == 2) {
            if (sender.isOp()) {
                if ("reset".equalsIgnoreCase(args[0])) {
                    return startsWith(WorldConfigManager.inst().keys(), args[1]);
                }
            }
        }
        return emptyList;
    }

    public List<String> startsWith(Collection<String> list, String s) {
        return startsWith(null, list, s);
    }
    public List<String> startsWith(String[] addition, Collection<String> list, String s) {
        String s1 = s.toLowerCase();
        List<String> stringList = new ArrayList<>(list);
        if (addition != null) stringList.addAll(0, Lists.newArrayList(addition));
        stringList.removeIf(it -> !it.toLowerCase().startsWith(s1));
        return stringList;
    }
}
