package com.github.beadieststar64.plugins.itemmanager;

import org.bukkit.plugin.java.JavaPlugin;

public final class ItemManager extends JavaPlugin {

    @Override
    public void onEnable() {
        //Create config.yml
        YamlLoader loader = new YamlLoader(this);
        loader.saveDefaultYaml();

        //Create message.yml
        loader = new YamlLoader(this, "message.yml");
        loader.saveDefaultYaml();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
