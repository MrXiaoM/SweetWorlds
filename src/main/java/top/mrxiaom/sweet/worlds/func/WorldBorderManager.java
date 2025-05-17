package top.mrxiaom.sweet.worlds.func;

import org.bukkit.World;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import top.mrxiaom.pluginbase.func.AutoRegister;
import top.mrxiaom.sweet.worlds.SweetWorlds;
import top.mrxiaom.sweet.worlds.func.config.Border;
import top.mrxiaom.sweet.worlds.func.config.WorldConfig;

import java.util.HashMap;
import java.util.Map;

@AutoRegister
public class WorldBorderManager extends AbstractModule implements Listener {
    private final Map<String, Border> borders = new HashMap<>();
    public WorldBorderManager(SweetWorlds plugin) {
        super(plugin);
        registerEvents();
    }

    @Override
    public void reloadConfig(MemoryConfiguration config) {
        borders.clear();
        for (WorldConfig world : WorldConfigManager.inst().worlds()) {
            borders.put(world.worldName, world.border);
        }
    }

    @EventHandler
    public void onWorldChanged(PlayerChangedWorldEvent event) {
        World world = event.getPlayer().getWorld();
        Border border = borders.get(world.getName());
        if (border != null) {
            plugin.getScheduler().runTask(() -> border.apply(world));
        }
    }
}
