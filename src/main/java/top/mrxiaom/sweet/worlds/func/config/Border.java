package top.mrxiaom.sweet.worlds.func.config;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;
import top.mrxiaom.pluginbase.utils.Util;
import top.mrxiaom.sweet.worlds.func.AbstractModule;

import java.time.LocalDateTime;

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

    public Double evaluateRange() {
        try {
            EvaluationValue result = createExpression(rangeFormula).evaluate();
            return result.getNumberValue().doubleValue();
        } catch (Throwable t) {
            return null;
        }
    }

    public void apply(World world) {
        Double range = evaluateRange();
        WorldBorder border = world.getWorldBorder();
        border.setCenter(centerX, centerZ);
        if (range != null) {
            border.setSize(range);
        }
    }

    @Nullable
    public static Border load(AbstractModule parent, ConfigurationSection section, String world) {
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
        if (isInvalidFormula(rangeFormula)) {
            parent.warn("[worlds.yml/" + world + "] 输入的 range-formula 公式无效");
            return null;
        }
        return new Border(enable, centerX, centerZ, rangeFormula);
    }

    protected static Expression createExpression(String formula) {
        LocalDateTime now = LocalDateTime.now();
        return new Expression(formula)
                // TODO: 代入更多数值，验证公式是否正确
                // 目前只有部分预设数值
                .with("daysOfMonth", now.getDayOfMonth())
                .with("daysOfWeek", now.getDayOfWeek())
                .with("daysOfYear", now.getDayOfYear())
                .with("dayOfMonth", now.getDayOfMonth())
                .with("dayOfWeek", now.getDayOfWeek())
                .with("dayOfYear", now.getDayOfYear())
                .with("date", now.getDayOfMonth())
                .with("week", now.getDayOfWeek())
                .with("month", now.getMonthValue())
                .with("year", now.getYear());
    }

    public static boolean isInvalidFormula(String formula) {
        try {
            Expression expression = createExpression(formula);
            return !expression.evaluate().isNumberValue();
        } catch (Throwable t) {
            return true;
        }
    }
}
