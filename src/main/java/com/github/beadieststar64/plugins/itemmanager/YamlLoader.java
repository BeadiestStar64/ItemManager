package com.github.beadieststar64.plugins.itemmanager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class YamlLoader {

    private final ItemManager plugin;
    private FileConfiguration yaml = null;
    private final File yamlFile;
    private final String fileName;

    /**
     * @param plugin Use ItemManager
     */
    public YamlLoader(ItemManager plugin) {
        this(plugin, "config.yml");
    }

    /**
     *
     * @param plugin Use ItemManager
     * @param fileName Create the specified yml file from resources
     */
    public YamlLoader(ItemManager plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        yamlFile = new File(plugin.getDataFolder(), fileName);
    }

    /**
     * Create default yml by resource's yml file.
     */
    public void saveDefaultYaml() {
        if(!yamlFile.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    public void reloadYaml() {
        yaml = YamlConfiguration.loadConfiguration(yamlFile);
        final InputStream def = plugin.getResource(fileName);
        if(def == null) {
            return;
        }
        yaml.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(def, StandardCharsets.UTF_8)));
    }

    public FileConfiguration getConfig() {
        if(yaml == null) {
            reloadYaml();
        }
        return yaml;
    }

    public @NotNull String getString(String key) {
        if(yaml == null) {
            reloadYaml();
        }
        String result = yaml.getString(key);
        if(result == null) {
            return "null";
        }
        return result;
    }
}
