package com.github.beadieststar64.plugins.itemmanager;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Commands implements CommandExecutor, TabCompleter, Listener {
    private final FileConfiguration messenger;
    private static FileConfiguration config;
    private ItemManager plugin;

    public Commands(ItemManager plugin) {
        this.plugin = plugin;
        this.messenger = new YamlLoader(plugin, "message.yml").getConfig();
        config = new YamlLoader(plugin).getConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if(command.getName().equals("manager-mode")) {
            if(args[0].equalsIgnoreCase("edit")) {
                if(checkSender(sender, false)) {
                    sender.sendMessage(Objects.requireNonNull(messenger.getString("SenderNotPlayer")));
                    return true;
                }
                if(checkPerms(
                        sender,
                        Objects.requireNonNull(config.getString("Editor.GlobalSetting.Admin.Permission")),
                        Objects.requireNonNull(config.getString("Editor.GlobalSetting.Citizen.Permission"))
                )) {
                    sender.sendMessage(Objects.requireNonNull(messenger.getString("PermissionDeny")));
                    return true;
                }

                //edit
                return true;
            }else if(args[0].equalsIgnoreCase("make")) {
                if(checkSender(sender, false)) {
                    sender.sendMessage(Objects.requireNonNull(messenger.getString("SenderNotPlayer")));
                    return true;
                }
                if(checkPerms(
                        sender,
                        Objects.requireNonNull(config.getString("Maker.GlobalSetting.Admin.Permission")),
                        Objects.requireNonNull(config.getString("Maker.GlobalSetting.Citizen.Permission"))
                )) {
                    sender.sendMessage(Objects.requireNonNull(messenger.getString("PermissionDeny")));
                    return true;
                }

                //make
                return true;
            }else if(args[0].equalsIgnoreCase("record")) {
                if(checkSender(sender, false)) {
                    sender.sendMessage(Objects.requireNonNull(messenger.getString("SenderNotPlayer")));
                    return true;
                }
                if(args.length == 2) {
                    switch (args[1]) {
                        case "edit" -> {
                            if(checkPerms(sender,
                                    Objects.requireNonNull(config.getString("Record.GlobalSetting.Admin.Permission.Edit")),
                                    Objects.requireNonNull(config.getString("Record.GlobalSetting.Citizen.Permission.Edit"))
                            )) {
                                sender.sendMessage(Objects.requireNonNull(messenger.getString("PermissionDeny")));
                                return true;
                            }

                            //record register
                        }
                        case "delete" -> {
                            if(checkPerms(sender,
                                    Objects.requireNonNull(config.getString("Record.GlobalSetting.Admin.Permission.Delete")),
                                    Objects.requireNonNull(config.getString("Record.GlobalSetting.Citizen.Permission.Delete"))
                            )) {
                                sender.sendMessage(Objects.requireNonNull(messenger.getString("PermissionDeny")));
                                return true;
                            }

                            //record delete
                        }
                        case "register" -> {
                            if(checkPerms(sender,
                                    Objects.requireNonNull(config.getString("Record.GlobalSetting.Admin.Permission.Register")),
                                    Objects.requireNonNull(config.getString("Record.GlobalSetting.Citizen.Permission.Register"))
                            )) {
                                sender.sendMessage(Objects.requireNonNull(messenger.getString("PermissionDeny")));
                                return true;
                            }

                            //record register
                        }
                        default -> {
                            command.setUsage("/manager-mode record <edit, delete, register>");
                            return false;
                        }
                    }
                }
            }
        }
        if(command.getName().equalsIgnoreCase("manager-help")) {
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        List<String> result = new ArrayList<>();

        List<String> arguments;
        if(command.getName().equalsIgnoreCase("manager-mode")) {
            arguments = new ArrayList<>(Arrays.asList(
                    "edit",
                    "make",
                    "record"
            ));

            if(args.length == 1) {
                for(String s : arguments) {
                    if(s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(s);
                    }
                }
                return result;
            }
            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("record")) {
                    arguments = new ArrayList<>(Arrays.asList(
                            "edit",
                            "delete",
                            "register"
                    ));

                    for(String s : arguments) {
                        if(s.toLowerCase().startsWith(args[0].toLowerCase())) {
                            result.add(s);
                        }
                    }
                    return result;
                }
            }
        }else if(command.getName().equalsIgnoreCase("manager-help")) {
            arguments = new ArrayList<>(Arrays.asList(
                    "edit-help",
                    "make-help",
                    "record-help"
            ));

            if(args.length == 1) {
                for(String s : arguments) {
                    if(s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(s);
                    }
                }
                return result;
            }
        }
        return null;
    }

    private boolean checkSender(CommandSender sender, boolean supportConsole) {
        if(sender instanceof Player) {
            return false;
        }
        if(sender instanceof ConsoleCommandSender) {
            if(supportConsole) {
                return false;
            }
            String message = messenger.getString("SenderNotPlayer");
            if(message == null) {
                message = ChatColor.RED + "Message is null!";
            }
            sender.sendMessage(message);
            return true;
        }
        return true;
    }

    private boolean checkPerms(CommandSender sender, String adminPerms, String citizenPerms) {
        if(config == null) {
            config = new YamlLoader(plugin).getConfig();
        }
        return !sender.hasPermission(Objects.requireNonNull(config.getString(adminPerms))) && !sender.hasPermission(Objects.requireNonNull(config.getString(citizenPerms)));
    }
}
