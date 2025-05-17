package top.mrxiaom.sweet.worlds.func;
        
import top.mrxiaom.sweet.worlds.SweetWorlds;

@SuppressWarnings({"unused"})
public abstract class AbstractPluginHolder extends top.mrxiaom.pluginbase.func.AbstractPluginHolder<SweetWorlds> {
    public AbstractPluginHolder(SweetWorlds plugin) {
        super(plugin);
    }

    public AbstractPluginHolder(SweetWorlds plugin, boolean register) {
        super(plugin, register);
    }
}
