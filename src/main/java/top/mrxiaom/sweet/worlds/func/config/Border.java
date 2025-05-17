package top.mrxiaom.sweet.worlds.func.config;

import org.bukkit.configuration.ConfigurationSection;
import top.mrxiaom.pluginbase.utils.Util;

public class Border {
    public final boolean enable;
    public final double centerX, centerZ;
    public final String rangeFormula;

    public Border(boolean enable, double centerX, double centerZ, String rangeFormula) {
        this.enable = enable;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.rangeFormula = rangeFormula;
    }

    public static Border load(ConfigurationSection section, String world) {
        boolean enable = section.getBoolean(world + ".border.enable");
        String[] center = section.getString(world + ".border.center", ",").split(",", 2);
        double centerX, centerZ;
        if (center.length == 2) {
            centerX = Util.parseDouble(center[0].trim()).orElse(-0.5d);
            centerZ = Util.parseDouble(center[1].trim()).orElse(-0.5d);
        } else {
            double value = Util.parseDouble(center[0].trim()).orElse(-0.5d);
            centerX = value;
            centerZ = value;
        }
        String rangeFormula = section.getString(world + ".border.range-formula");
        // TODO: 验证公式可用性
        return new Border(enable, centerX, centerZ, rangeFormula);
    }
}
