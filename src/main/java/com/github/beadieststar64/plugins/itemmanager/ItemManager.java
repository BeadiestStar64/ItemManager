package com.github.beadieststar64.plugins.itemmanager;

import com.github.beadieststar64.plugins.itemmanager.Maker.MakerCommand;
import org.bukkit.command.PluginCommand;
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

        //Create item.yml
        loader = new YamlLoader(this, "item.yml");
        loader.saveDefaultYaml();

        //register commands
        String[] commands = new String[]{"manager-mode", "manager-help"};
        PluginCommand command;
        for(String str : commands) {
            command = getCommand(str);
            if(command != null) {
                command.setExecutor(new Commands(this));
                command.setTabCompleter(new Commands(this));
            }
        }

        //register maker commands
        final PluginCommand makerCommand = getCommand("/maker");
        if(makerCommand != null) {
            makerCommand.setExecutor(new MakerCommand(this));
            makerCommand.setTabCompleter(new MakerCommand(this));
        }

        getServer().getPluginManager().registerEvents(new Commands(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
