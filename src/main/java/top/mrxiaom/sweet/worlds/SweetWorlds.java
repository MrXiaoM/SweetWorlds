package top.mrxiaom.sweet.worlds;
        
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.DirectSchedulerFactory;
import top.mrxiaom.pluginbase.BukkitPlugin;
import top.mrxiaom.pluginbase.utils.scheduler.FoliaLibScheduler;

public class SweetWorlds extends BukkitPlugin {
    public static SweetWorlds getInstance() {
        return (SweetWorlds) BukkitPlugin.getInstance();
    }
    private Scheduler quartz;

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

    public Scheduler getQuartz() {
        return quartz;
    }

    @Override
    protected void beforeLoad() {
        try {
            DirectSchedulerFactory factory = DirectSchedulerFactory.getInstance();
            factory.createVolatileScheduler(10);
            this.quartz = factory.getScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException("无法初始化 Quartz 调度器", e);
        }
    }

    @Override
    protected void afterEnable() {
        getLogger().info("SweetWorlds 加载完毕");
    }

    @Override
    protected void afterDisable() {
        if (quartz != null) try {
            quartz.shutdown(false);
        } catch (SchedulerException e) {
            warn("关闭 Quartz 调度器时出现异常", e);
        }
    }
}
