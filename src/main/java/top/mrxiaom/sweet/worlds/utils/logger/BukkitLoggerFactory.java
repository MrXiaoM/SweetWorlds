package top.mrxiaom.sweet.worlds.utils.logger;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class BukkitLoggerFactory implements ILoggerFactory {
    @Override
    public Logger getLogger(String name) {
        return new BukkitLogger(name);
    }
}
