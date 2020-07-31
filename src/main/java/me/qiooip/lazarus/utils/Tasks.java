package me.qiooip.lazarus.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.qiooip.lazarus.Lazarus;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

public class Tasks {

    public static ThreadFactory newThreadFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).build();
    }

    public static ThreadFactory newThreadFactory(String name, UncaughtExceptionHandler handler) {
        return new ThreadFactoryBuilder().setNameFormat(name).setUncaughtExceptionHandler(handler).build();
    }

    public static void sync(Callable callable) {
        Bukkit.getScheduler().runTask(Lazarus.getInstance(), callable::call);
    }

    public static BukkitTask syncLater(Callable callable, long delay) {
        return Bukkit.getScheduler().runTaskLater(Lazarus.getInstance(), callable::call, delay);
    }

    public static BukkitTask syncTimer(Callable callable, long delay, long value) {
        return Bukkit.getScheduler().runTaskTimer(Lazarus.getInstance(), callable::call, delay, value);
    }

    public static void async(Callable callable) {
        Bukkit.getScheduler().runTaskAsynchronously(Lazarus.getInstance(), callable::call);
    }

    public static BukkitTask asyncLater(Callable callable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Lazarus.getInstance(), callable::call, delay);
    }

    public static BukkitTask asyncTimer(Callable callable, long delay, long value) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Lazarus.getInstance(), callable::call, delay, value);
    }

    public interface Callable {
        void call();
    }
}
