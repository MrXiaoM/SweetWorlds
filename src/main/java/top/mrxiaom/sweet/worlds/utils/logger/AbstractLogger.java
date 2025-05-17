package top.mrxiaom.sweet.worlds.utils.logger;

import org.slf4j.Logger;
import org.slf4j.Marker;

public abstract class AbstractLogger implements Logger {
    private final String name;
    public AbstractLogger(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    protected abstract void trace0(String name, String s);
    protected abstract void trace0(String name, String s, Object... objects);
    protected abstract void trace0(String name, String s, Throwable throwable);
    protected abstract void debug0(String name, String s);
    protected abstract void debug0(String name, String s, Object... objects);
    protected abstract void debug0(String name, String s, Throwable throwable);
    protected abstract void info0(String name, String s);
    protected abstract void info0(String name, String s, Object... objects);
    protected abstract void info0(String name, String s, Throwable throwable);
    protected abstract void warn0(String name, String s);
    protected abstract void warn0(String name, String s, Object... objects);
    protected abstract void warn0(String name, String s, Throwable throwable);
    protected abstract void error0(String name, String s);
    protected abstract void error0(String name, String s, Object... objects);
    protected abstract void error0(String name, String s, Throwable throwable);

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public void trace(String s) {
        trace0(name, s);
    }

    @Override
    public void trace(String s, Object o) {
        trace0(name, s, o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        trace0(name, s, o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        trace0(name, s, objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        trace0(name, s, throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return true;
    }

    @Override
    public void trace(Marker marker, String s) {
        trace0(marker.getName(), s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        trace0(marker.getName(), s, o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        trace0(marker.getName(), s, o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        trace0(marker.getName(), s, objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        trace0(marker.getName(), s, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public void debug(String s) {
        debug0(name, s);
    }

    @Override
    public void debug(String s, Object o) {
        debug0(name, s, o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        debug0(name, s, o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        debug0(name, s, objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        debug0(name, s, throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return true;
    }

    @Override
    public void debug(Marker marker, String s) {
        debug0(marker.getName(), s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        debug0(marker.getName(), s, o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        debug0(marker.getName(), s, o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        debug0(marker.getName(), s, objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        debug0(marker.getName(), s, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(String s) {
        info0(name, s);
    }

    @Override
    public void info(String s, Object o) {
        info0(name, s, o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        info0(name, s, o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        info0(name, s, objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        info0(name, s, throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return true;
    }

    @Override
    public void info(Marker marker, String s) {
        info0(marker.getName(), s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        info0(marker.getName(), s, o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        info0(marker.getName(), s, o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        info0(marker.getName(), s, objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        info0(marker.getName(), s, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(String s) {
        warn0(name, s);
    }

    @Override
    public void warn(String s, Object o) {
        warn0(name, s, o);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        warn0(name, s, o, o1);
    }

    @Override
    public void warn(String s, Object... objects) {
        warn0(name, s, objects);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        warn0(name, s, throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return true;
    }

    @Override
    public void warn(Marker marker, String s) {
        warn0(marker.getName(), s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        warn0(marker.getName(), s, o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        warn0(marker.getName(), s, o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        warn0(marker.getName(), s, objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        warn0(marker.getName(), s, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(String s) {
        error0(name, s);
    }

    @Override
    public void error(String s, Object o) {
        error0(name, s, o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        error0(name, s, o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        error0(name, s, objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        error0(name, s, throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return true;
    }

    @Override
    public void error(Marker marker, String s) {
        error0(marker.getName(), s);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        error0(marker.getName(), s, o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        error0(marker.getName(), s, o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        error0(marker.getName(), s, objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        error0(marker.getName(), s, throwable);
    }
}
