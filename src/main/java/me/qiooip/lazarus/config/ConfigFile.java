package me.qiooip.lazarus.config;

import lombok.Getter;
import me.qiooip.lazarus.Lazarus;
import me.qiooip.lazarus.utils.Color;
import me.qiooip.lazarus.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigFile extends YamlConfiguration {

    @Getter private final File file;

    public ConfigFile(String name) throws RuntimeException {
        this.file = new File(Lazarus.getInstance().getDataFolder(), name);

        if(!this.file.exists()) {
            Lazarus.getInstance().saveResource(name, false);
        }

        try {
            this.load(this.file);
        } catch(IOException | InvalidConfigurationException e) {
            Lazarus.getInstance().log("");
            Lazarus.getInstance().log("&4===&c=============================================&4===");
            Lazarus.getInstance().log(StringUtils.center("&cError occurred while loading " + name + ".", 51));
            Lazarus.getInstance().log("");

            Stream.of(e.getMessage().split("\n")).forEach(line -> Lazarus.getInstance().log(line));

            Lazarus.getInstance().log("&4===&c=============================================&4===");
            throw new RuntimeException();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationSection getSection(String name) {
        return super.getConfigurationSection(name);
    }

    @Override
    public int getInt(String path) {
        return super.getInt(path, 0);
    }

    @Override
    public double getDouble(String path) {
        return super.getDouble(path, 0.0);
    }

    @Override
    public boolean getBoolean(String path) {
        return super.getBoolean(path, false);
    }

    @Override
    public String getString(String path) {
        return Color.translate(super.getString(path, ""));
    }

    @Override
    public List<String> getStringList(String path) {
        return super.getStringList(path).stream().map(Color::translate).collect(Collectors.toList());
    }
}
