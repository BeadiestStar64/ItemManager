package com.github.beadieststar64.plugins.itemmanager.Maker;

import com.github.beadieststar64.plugins.itemmanager.ItemManager;
import com.github.beadieststar64.plugins.itemmanager.Manager;
import com.github.beadieststar64.plugins.itemmanager.YamlLoader;
import com.google.common.collect.Multimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class MakerCommand extends Manager implements TabExecutor {
    FileConfiguration config;
    FileConfiguration messenger;

    ItemManager plugin;
    Maker maker;

    public MakerCommand(ItemManager plugin) {
        this.plugin = plugin;
        this.config = new YamlLoader(plugin).getConfig();
        this.messenger = new YamlLoader(plugin, "message.yml").getConfig();
        maker = new Maker();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Objects.requireNonNull(messenger.getString("SenderNotPlayer")));
            return true;
        }
        Player player = (Player) sender;

        if(!command.getName().equalsIgnoreCase("/maker")) {
            return true;
        }

        if(args[0].equalsIgnoreCase("attribute")) {
            try {
                Multimap<Attribute, AttributeModifier> map = maker.meta.get(player.getUniqueId()).getAttributeModifiers();
                switch(args[1].toLowerCase()) {
                    case "edit" -> {


                    }
                    case "remove" -> {
                        if(map == null) {
                            //セットアップ開始
                            if(maker.meta == null) {
                                maker.meta = new HashMap<>();
                            }
                        }
                        try {
                            if(args.length != 3) {
                                player.sendMessage(String.format("%s: %s", messenger.getString("MakerInvalidCommand"), "//maker attribute <arg1(String)> <namespace> <arg2(Operation)> <amount(Double)> <uuid> <slot(Equipment)>"));
                                return true;
                            }
                            maker.meta.get(player.getUniqueId()).removeAttributeModifier(Attribute.valueOf(args[2]));
                            return true;
                        }catch (IllegalArgumentException e) {
                            player.sendMessage(String.format("%s: %s", messenger.getString("MakerInvalidCommand"), "//maker attribute <arg1(String)> <namespace> <arg2(Operation)> <amount(Double)> <uuid> <slot(Equipment)>"));
                            return true;
                        }catch (NullPointerException e) {
                            player.sendMessage(Objects.requireNonNull(messenger.getString("MakerArgumentIsNull")));
                            return true;
                        }
                    }
                    case "set" -> {
                        try {
                            UUID uuid;
                            ArrayList<EquipmentSlot> slotsList = new ArrayList<>();
                            if(args.length == 6) {
                                //すべてのスロットに属性セット
                                uuid = getUuid(args[5]);
                                slotsList.addAll(Arrays.asList(EquipmentSlot.values()));
                            }else if(args.length == 7) {
                                uuid = getUuid(args[5]);
                                slotsList.add(EquipmentSlot.valueOf(args[6].toUpperCase()));
                            }else{
                                player.sendMessage(String.format("%s: %s", messenger.getString("MakerInvalidCommand"), "//maker attribute <arg1(String)> <namespace> <arg2(Operation)> <amount(Double)> <uuid> <slot(Equipment)>"));
                                return true;
                            }

                            //duplication check
                            if(maker.meta.get(player.getUniqueId()).getAttributeModifiers().containsKey(Attribute.valueOf(args[2].toUpperCase()))) {
                                player.sendMessage(String.format("%s", messenger.getString("MakerAlreadyInclude")).replaceAll("<ARGUMENT>", Objects.requireNonNull(messenger.getString("Attribute"))));
                                return true;
                            }

                            for(EquipmentSlot slot : slotsList) {
                                maker.meta.get(player.getUniqueId()).addAttributeModifier(Attribute.valueOf(args[2].toUpperCase()), new AttributeModifier(uuid, args[2], Double.parseDouble(args[4]), AttributeModifier.Operation.valueOf(args[3].toUpperCase()), slot));
                            }
                        }catch (IllegalArgumentException e) {
                            player.sendMessage(String.format("%s: %s", messenger.getString("MakerInvalidCommand"), "//maker attribute <arg1(String)> <namespace> <arg2(Operation)> <amount(Double)> <uuid> <slot(Equipment)>"));
                            return true;
                        }catch (NullPointerException e) {
                            if(maker.meta == null) {
                                player.sendMessage(Objects.requireNonNull(messenger.getString("MakerArgumentIsNull")).replaceAll("<AUGUEMENT>", "meta"));

                            }
                            player.sendMessage(Objects.requireNonNull(messenger.getString("MakerArgumentIsNull")).replaceAll("<AUGUEMENT>", "argument"));
                            return true;
                        }
                    }
                }
            }catch (Exception e) {
                player.sendMessage(Objects.requireNonNull(messenger.getString("MakerArgumentIsNull")));
            }
        }
        return false;
    }

    private UUID getUuid(String arg) {
        if(!arg.equalsIgnoreCase("random-uuid")) {
            return UUID.fromString(arg);
        }
        return UUID.randomUUID();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            return null;
        }

        Player player = (Player) sender;

        List<String> result = new ArrayList<>();
        List<String> argument = new ArrayList<>();
        List<String> colorList = new ArrayList<>(Arrays.asList(
                "BLACK",
                "DARK_BLUE",
                "DARK_GREEN",
                "DARK_AQUA",
                "DARK_RED",
                "DARK_PURPLE",
                "DARK_GRAY",
                "GOLD",
                "GRAY",
                "BLUE",
                "GREEN",
                "AQUA",
                "RED",
                "LIGHT_PURPLE",
                "YELLOW",
                "WHITE",
                "#[Hex code]"
        ));
        List<String> colorOptionList = new ArrayList<>(Arrays.asList(
                "BOLD",
                "ITALIC",
                "OBFUSCATED",
                "RESET",
                "STRIKETHROUGH",
                "UNDERLINE"
        ));

        if(!command.getName().equalsIgnoreCase("/maker")) {
            return null;
        }
        try {
            if (args.length == 1) {
                argument.add("attribute");
                argument.add("cancel");
                argument.add("enchanting");
                argument.add("generate");
                argument.add("item_flag");
                argument.add("lore");
                argument.add("material");
                argument.add("name");

                for (String str : argument) {
                    if((!player.hasPermission(Objects.requireNonNull(config.getString("Maker.GlobalSetting.Citizen.Permission." + ManagerArg.valueOf(str.toUpperCase())))))) {
                        continue;
                    }
                    if (str.toLowerCase().startsWith(args[0].toLowerCase())) {
                        result.add(str);
                    }
                }
                return result;
            }
            if (args.length == 2) {
                argument.add("edit");
                argument.add("set");
                argument.add("remove");

                if (args[0].equalsIgnoreCase("enchanting")) {
                    if(tabPermissionCheck(sender, "Enchanting")) {
                        return null;
                    }
                    for (String str : argument) {
                        if (str.toLowerCase().startsWith(args[1])) {
                            result.add(str);
                        }
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("attribute")) {
                    if(tabPermissionCheck(sender, "Attribute")) {
                        return null;
                    }
                    for (String str : argument) {
                        if (str.toLowerCase().startsWith(args[1])) {
                            result.add(str);
                        }
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("iem_flag")) {
                    if(tabPermissionCheck(sender, "ItemFlag")) {
                        return null;
                    }
                    for (String str : argument) {
                        if (str.toLowerCase().startsWith(args[1])) {
                            result.add(str);
                        }
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("lore")) {
                    if(tabPermissionCheck(sender, "Lore")) {
                        return null;
                    }
                    for (String str : argument) {
                        if (str.toLowerCase().startsWith(args[1])) {
                            result.add(str);
                        }
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("material")) {
                    if(tabPermissionCheck(sender, "AdminItems")) {
                        return null;
                    }
                    for (String str : argument) {
                        if (str.toLowerCase().startsWith(args[1])) {
                            result.add(str);
                        }
                    }
                    return result;
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("attribute")) {
                    //属性一覧
                    if(tabPermissionCheck(sender, "Attribute")) {
                        return null;
                    }
                    if(args[1].equalsIgnoreCase("set")){
                        for (Attribute attribute : Attribute.values()) {
                            argument.add(attribute.getKey().getKey());
                        }
                        for (String str : argument) {
                            if (str.toLowerCase().startsWith(args[2])) {
                                result.add(str);
                            }
                        }
                    }else{
                        try {
                            for(Attribute attribute : maker.meta.get(player.getUniqueId()).getAttributeModifiers().asMap().keySet()) {
                                argument.add(attribute.getKey().getKey());
                            }
                            for(String str : argument) {
                                if(str.toLowerCase().startsWith(args[2])) {
                                    result.add(str);
                                }
                            }
                        }catch(Exception e) {
                            result.add("");
                        }
                    }
                    return result;
                }

                if (args[0].equalsIgnoreCase("iem_flag")) {
                    //アイテムフラッグ一覧
                    if(tabPermissionCheck(sender, "ItemFlag")) {
                        return null;
                    }
                    for (ItemFlag flag : ItemFlag.values()) {
                        argument.add(flag.name().toLowerCase());
                    }
                    for (String str : argument) {
                        if (str.toLowerCase().startsWith(args[2])) {
                            result.add(str);
                        }
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("material")) {
                    //マテリアル一覧
                    if(tabPermissionCheck(sender, "AdminItems")) {
                        return null;
                    }
                    for (Material m : Material.values()) {
                        if(checkSpecialMaterial(m)) {
                            if(!sender.hasPermission(Objects.requireNonNull(config.getString("Maker.GlobalSetting.Admin.Permission.All")))) {
                                if(!sender.hasPermission(Objects.requireNonNull(config.getString("Maker.GlobalSetting.Admin.Permission.AdminItems")))) {
                                    continue;
                                }
                            }
                        }
                        argument.add(m.name().toLowerCase());
                    }
                    for (String str : argument) {
                        if (str.toLowerCase().startsWith(args[2])) {
                            result.add(str);
                        }
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("name")) {
                    if(tabPermissionCheck(sender, "ColorName")) {
                        return null;
                    }
                    return new ArrayList<>(List.of("color"));
                }
                if (args[0].equalsIgnoreCase("enchanting")) {
                    //エンチャント一覧
                    if(tabPermissionCheck(sender, "Enchanting")) {
                        return null;
                    }
                    for (Enchantment enchantment : Enchantment.values()) {
                        argument.add(enchantment.getKey().getKey().toLowerCase());
                    }
                    for (String str : argument) {
                        if (str.toLowerCase().startsWith(args[2].toLowerCase())) {
                            result.add(str);
                        }
                    }
                    return result;
                }
            }
            if (args.length == 4) {
                if (args[2].equalsIgnoreCase("color")) {
                    if(tabPermissionCheck(sender, "ColorName")) {
                        return null;
                    }
                    for (String color : colorList) {
                        if (color.toLowerCase().startsWith(args[3])) {
                            result.add(color.toLowerCase());
                        }
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("enchanting")) {
                    if(tabPermissionCheck(sender, "Enchanting")) {
                        return null;
                    }
                    for (int i = 1, l = Enchantment.getByKey(NamespacedKey.minecraft(args[2])).getMaxLevel(); i <= l; i++) {
                        result.add(String.valueOf(i));
                    }
                    return result;
                }
                if(args[0].equalsIgnoreCase("attribute")) {
                    if(tabPermissionCheck(sender, "Attribute")) {
                        return null;
                    }
                    if(args[1].equalsIgnoreCase("set")) {
                        for(AttributeModifier.Operation operation : AttributeModifier.Operation.values()) {
                            argument.add(operation.name().toLowerCase());
                        }
                        for(String str : argument) {
                            if(str.toLowerCase().startsWith(args[3])) {
                                result.add(str);
                            }
                        }
                    }else{
                        result.add("");
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("lore")) {
                    if(tabPermissionCheck(sender, "ColorName")) {
                        return null;
                    }
                    return new ArrayList<>(List.of("color"));
                }
            }
            if (args.length == 5) {
                if (args[3].equalsIgnoreCase("color")) {
                    if(tabPermissionCheck(sender, "ColorName")) {
                        return null;
                    }
                    for (String color : colorList) {
                        if (color.toLowerCase().startsWith(args[4])) {
                            result.add(color.toLowerCase());
                        }
                    }
                    return result;
                }
            }
            if(args.length == 6) {
                if(colorList.contains(args[4])) {
                    if(tabPermissionCheck(sender, "Attribute")) {
                        return null;
                    }
                    if(args[1].equalsIgnoreCase("set")) {
                        for (String option : colorOptionList) {
                            if (option.toLowerCase().startsWith(args[5])) {
                                result.add(option.toLowerCase());
                            }
                        }
                    }else{
                        result.add("");
                    }
                    return result;
                }
                if(args[0].equalsIgnoreCase("attribute")) {
                    result.add("random-uuid");
                    return result;
                }
            }
            if(args.length == 7) {
                if(args[0].equalsIgnoreCase("attribute")) {
                    if(tabPermissionCheck(sender, "Attribute")) {
                        return null;
                    }
                    if(args[1].equalsIgnoreCase("set")) {
                        for(EquipmentSlot slot : EquipmentSlot.values()) {
                            argument.add(slot.name().toLowerCase());
                        }
                        for(String str : argument) {
                            if(str.toLowerCase().startsWith(args[6])) {
                                result.add(str);
                            }
                        }
                    }else{
                        result.add("");
                    }
                    return result;
                }
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private boolean tabPermissionCheck(CommandSender sender, String argType) {
        if(sender.hasPermission(Objects.requireNonNull(config.getString("Maker.GlobalSetting.Admin.Permission.All")))) {
            return false;
        }
        if(sender.hasPermission(Objects.requireNonNull(config.getString("Maker.GlobalSetting.Admin.Permission." + argType)))) {
            return false;
        }
        if(sender.hasPermission(Objects.requireNonNull(config.getString("Maker.GlobalSetting.Citizen.Permission.All")))) {
            return false;
        }
        return !sender.hasPermission(Objects.requireNonNull(config.getString("Maker.GlobalSetting.Citizen.Permission." + argType)));
    }
}