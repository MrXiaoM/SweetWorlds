package top.mrxiaom.sweet.worlds.utils.logger;

import top.mrxiaom.pluginbase.utils.Util;
import top.mrxiaom.sweet.worlds.SweetWorlds;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitLogger extends AbstractLogger {
    private final Logger logger = SweetWorlds.getInstance().getLogger();
    public BukkitLogger(String name) {
        super(name);
    }

    private String handleName(String name) {
        if (name.startsWith("top.mrxiaom.sweet.worlds.libs.")) {
            return "[" + name.substring(30) + "]";
        }
        return "[" + name + "]";
    }

    private void log(Level level, String name, String s) {
        logger.log(level, handleName(name) + " " + s);
    }
    private void log(Level level, String name, String s, Object[] objects) {
        StringBuilder sb = new StringBuilder(handleName(name));
        sb.append(" ");
        List<String> split = Util.split(s, "{}");
        int size = split.size();
        for (int i = 0; i < size; i++) {
            sb.append(split.get(i));
            if (i < size - 1) {
                sb.append("{").append(i).append("}");
            }
        }
        logger.log(level, sb.toString(), objects);
    }
    private void log(Level level, String name, String s, Throwable throwable) {
        logger.log(level, handleName(name) + " " + s, throwable);
    }

    @Override
    protected void trace0(String name, String s) {
        log(Level.FINEST, name, s);
    }

    @Override
    protected void trace0(String name, String s, Object... objects) {
        log(Level.FINEST, name, s, objects);
    }

    @Override
    protected void trace0(String name, String s, Throwable throwable) {
        log(Level.FINEST, name, s, throwable);
    }

    @Override
    protected void debug0(String name, String s) {
        log(Level.FINE, name, s);
    }

    @Override
    protected void debug0(String name, String s, Object... objects) {
        log(Level.FINE, name, s, objects);
    }

    @Override
    protected void debug0(String name, String s, Throwable throwable) {
        log(Level.FINE, name, s, throwable);
    }

    @Override
    protected void info0(String name, String s) {
        log(Level.INFO, name, s);
    }

    @Override
    protected void info0(String name, String s, Object... objects) {
        log(Level.INFO, name, s, objects);
    }

    @Override
    protected void info0(String name, String s, Throwable throwable) {
        log(Level.INFO, name, s, throwable);
    }

    @Override
    protected void warn0(String name, String s) {
        log(Level.WARNING, name, s);
    }

    @Override
    protected void warn0(String name, String s, Object... objects) {
        log(Level.WARNING, name, s, objects);
    }

    @Override
    protected void warn0(String name, String s, Throwable throwable) {
        log(Level.WARNING, name, s, throwable);
    }

    @Override
    protected void error0(String name, String s) {
        log(Level.SEVERE, name, s);
    }

    @Override
    protected void error0(String name, String s, Object... objects) {
        log(Level.SEVERE, name, s, objects);
    }

    @Override
    protected void error0(String name, String s, Throwable throwable) {
        log(Level.SEVERE, name, s, throwable);
    }
}
