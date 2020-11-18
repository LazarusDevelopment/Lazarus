package me.qiooip.lazarus.handlers.staff;

import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.config.Language;
import me.qiooip.lazarus.handlers.manager.Handler;
import me.qiooip.lazarus.utils.LocationUtils;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;

public class WarpsHandler extends Handler {

    private final Map<String, Location> warps;

    public WarpsHandler() {
        this.warps = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        this.loadWarps();
    }

    @Override
    public void disable() {
        this.saveWarps();
        this.warps.clear();
    }

    private void loadWarps() {
        ConfigurationSection section = Lazarus.getInstance().getUtilitiesFile().getSection("WARPS");
        if(section == null) return;

        section.getKeys(false).forEach(warp -> this.addWarp(warp, LocationUtils.stringToLocation(section.getString(warp))));
    }

    private void saveWarps() {
        ConfigurationSection section = Lazarus.getInstance().getUtilitiesFile().createSection("WARPS");
        this.warps.forEach((name, location) -> section.set(name, LocationUtils.locationToString(location)));

        Lazarus.getInstance().getUtilitiesFile().save();
    }

    public Location getWarp(String string) {
        return this.warps.get(string);
    }

    public boolean addWarp(String name, Location location) {
        if(this.warps.containsKey(name)) return false;

        this.warps.put(name, location);
        return true;
    }

    public boolean deleteWarp(String name) {
        if(!this.warps.containsKey(name)) return false;

        this.warps.remove(name);
        return true;
    }

    public void listWarps(CommandSender sender) {
        StringJoiner joiner = new StringJoiner(", ");
        this.warps.keySet().forEach(warp -> joiner.add(StringUtils.capitalize(warp.toLowerCase())));

        sender.sendMessage(Language.WARP_LIST.replace("<warps>", joiner.toString()));
    }

    public Set<String> getWarpNames() {
        return this.warps.keySet();
    }
}
