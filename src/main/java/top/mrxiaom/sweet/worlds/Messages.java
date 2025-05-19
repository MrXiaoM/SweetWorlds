package top.mrxiaom.sweet.worlds;

import top.mrxiaom.pluginbase.func.language.IHolderAccessor;
import top.mrxiaom.pluginbase.func.language.LanguageEnumAutoHolder;

import java.util.List;

import static top.mrxiaom.pluginbase.func.language.LanguageEnumAutoHolder.wrap;

public enum Messages implements IHolderAccessor {
    commands__reload("&a配置文件已重载"),
    commands__reset__not_found("&e没有这个世界的配置"),
    commands__reset__confirm("",
            "  &e警告，你正在进行世界重置操作。",
            "  &e这个操作无法被撤销，你确定要这么做吗？",
            "  &f要进行世界重置，请执行以下命令",
            "  &b/sweetworlds reset %world% confirm",
            ""),
    commands__reset__start("&a正在执行世界重置操作，详见服务器控制台"),
    logs__reset__manual("正在手动重置世界 %world%"),
    logs__reset__auto("正在自动重置世界 %world%"),
    logs__reset__next__end("这是最后一次重置"),
    logs__reset__next__time("下次将在 %time% 重置世界"),
    logs__reset__not_found("找不到世界配置 %world%"),
    logs__reset__done("世界 %world% 已重置!"),
    logs__reset__failed("世界 %world% 重新生成失败!"),
    logs__world__unloaded("世界 %world% 已卸载"),
    logs__world__deleted("世界文件夹 %folder% 已删除"),
    logs__world__delete_error("删除世界 %world% 时出现异常"),
    logs__world__creating("正在创建世界 %world%"),
    logs__timer__next__end("世界 %world% 没有下次重置时间"),
    logs__timer__next__time("世界 %world% 将在 %world% 重置"),
    teleport__resetting("&e世界正在重置，请稍等片刻"),
    teleport__banned("&e休整中，请在服务器重启后再进入该世界"),
    ;
    Messages(String defaultValue) {
        holder = wrap(this, defaultValue);
    }
    Messages(String... defaultValue) {
        holder = wrap(this, defaultValue);
    }
    Messages(List<String> defaultValue) {
        holder = wrap(this, defaultValue);
    }
    private final LanguageEnumAutoHolder<Messages> holder;
    public LanguageEnumAutoHolder<Messages> holder() {
        return holder;
    }
}
