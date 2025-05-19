# SweetWorlds

Minecraft 世界重置插件

## 简介

这个插件用于定期重置你的资源世界，使用 cron 表达式来控制定时时间。
+ 设定重置世界种子
+ 设定要求重置后需要重启服务器才能进入世界
+ 世界重置后，执行控制台命令
+ 世界边界大小可设定随表达式变化

## 兼容性

目前已在以下版本进行测试
+ `1.20.4`

目前已测试兼容以下插件
+ [MyWorlds](https://www.spigotmc.org/resources/39594)

目前插件仅调用 Bukkit 接口，且已测试可使用 `spigot-api 1.8` 编译。  
通常多世界插件会主动兼容，无需额外编写兼容层。

## PAPI 变量

```
%sweetworlds_reset_next_time_<世界>%  -- 下次重置时间
%sweetworlds_reset_next_utc_<世界>%  -- 下次重置时间(UTC时间戳)
%sweetworlds_reset_remaining_time_<世界>%  -- 距离下次重置还剩多长时间
```

## 开发者

插件使用 Quartz Scheduler 作为定时调度器，世界重置任务在 [top.mrxiaom.sweet.worlds.func.ResetWorldJob](src/main/java/top/mrxiaom/sweet/worlds/func/ResetWorldJob.java)。

如需更进一步操作，比如备份世界等等，本插件暂无计划，请自行修改源代码。欢迎 PR。
