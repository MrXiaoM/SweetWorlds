package top.mrxiaom.sweet.worlds.utils.logger;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMDCAdapter;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

public class BukkitLoggerServiceProvider implements SLF4JServiceProvider {
    ILoggerFactory loggerFactory = new BukkitLoggerFactory();
    IMarkerFactory markerFactory = new BasicMarkerFactory();
    MDCAdapter mdcAdapter = new BasicMDCAdapter();

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return markerFactory;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return mdcAdapter;
    }

    @Override
    public String getRequestedApiVersion() {
        return "2.0.16";
    }

    @Override
    public void initialize() {

    }
}
