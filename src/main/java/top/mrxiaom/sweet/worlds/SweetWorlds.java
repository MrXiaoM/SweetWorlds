package top.mrxiaom.sweet.worlds;
        
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.pluginbase.utils.scheduler.FoliaLibScheduler;

public class SweetWorlds extends BukkitPlugin {
    public static SweetWorlds getInstance() {
        return (SweetWorlds) BukkitPlugin.getInstance();
    }

    public SweetWorlds() {
        super(options()
                .bungee(false)
                .adventure(true)
                .database(false)
                .reconnectDatabaseWhenReloadConfig(false)
                .scanIgnore("top.mrxiaom.sweet.worlds.libs")
        );
        this.scheduler = new FoliaLibScheduler(this);
    }

    @Override
    protected void afterEnable() {
        getLogger().info("SweetWorlds 加载完毕");
    }
}
