package com.github.beadieststar64.plugins.itemmanager.Maker;

import com.github.beadieststar64.plugins.itemmanager.ConfigMenu;
import com.github.beadieststar64.plugins.itemmanager.ItemManager;
import com.github.beadieststar64.plugins.itemmanager.Manager.AlreadyIncludeException;
import com.github.beadieststar64.plugins.itemmanager.Manager.Manager;
import com.github.beadieststar64.plugins.itemmanager.Manager.NotAllowPermissionException;
import com.github.beadieststar64.plugins.itemmanager.Manager.NotProvideException;
import com.github.beadieststar64.plugins.itemmanager.YamlLoader;
import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MakerCommand extends Manager implements TabExecutor {
    YamlLoader config;
    YamlLoader messenger;

    ItemManager plugin;
    Maker maker;

    public MakerCommand(ItemManager plugin) {
        this.plugin = plugin;
        this.config = new YamlLoader(plugin);
        this.messenger = new YamlLoader(plugin, "message.yml");
        maker = new Maker();
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(messenger.getString("SenderNotPlayer"));
            return true;
        }

        if(!command.getName().equalsIgnoreCase("/maker")) {
            return true;
        }

        try {
            //Basic Permission check
            if(permissionCheck(player, "Basic")) {
                throw new NotAllowPermissionException();
            }

            if(args[0].equalsIgnoreCase("attribute")) {
                switch(args[1]) {
                    case "edit" -> {

                    }
                    case "remove" -> {

                    }
                    case "set" -> {

                    }
                    default -> {
                        return false;
                    }
                }
            }else if(args[0].equalsIgnoreCase("cancel")) {

            }else if(args[0].equalsIgnoreCase("enchanting")) {
                try {
                    if(args[1].equalsIgnoreCase("set")) {
                        Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(args[2].toUpperCase()));
                        if(maker.enchantments.get(player.getUniqueId()).contains(enchant)) {
                            throw new AlreadyIncludeException();
                        }
                        int level = Integer.parseInt(args[3]);
                        if(enchant.getMaxLevel() < level) {
                            if(!(player.hasPermission("Maker.GlobalSetting.Admin.Setting.AllowOverEnchanting")) || !(player.hasPermission("Maker.GlobalSetting.Citizen.Setting.AllowOverEnchanting"))) {
                                throw new NotAllowPermissionException();
                            }
                        }

                        maker.enchantments.get(player.getUniqueId()).add(enchant);
                        maker.enchantmentLevels.get(player.getUniqueId()).add(level);
                        player.sendMessage(messenger.getString("MakerSetEnchantment"));
                        return true;
                    }else{

                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(String.format("[%s] %s: %s", plugin.getName(), messenger.getString("MakerIllegalArgument"), ""));
                    return true;
                } catch (IllegalArgumentException e) {
                    player.sendMessage(String.format(String.format(ChatColor.WHITE + "[%s]" + "%s: %s", plugin.getName(), messenger.getString("MakerInvalidCommand"), "//maker enchanting <arg1(String)> <namespace> <arg2(int)>")));
                    return true;
                }catch (NotAllowPermissionException e) {
                    player.sendMessage(String.format(ChatColor.WHITE + "[%s] %s", plugin.getName(), messenger.getString("PermissionDeny")));
                    return true;
                }catch (AlreadyIncludeException e) {
                    player.sendMessage(String.format(ChatColor.WHITE + "[%s] %s\n[%s] %s\n[%s] %s", plugin.getName(),
                                    messenger.getString("MakerAlreadyInclude"),
                                    messenger.getString("Duplication").substring(0, 1).toUpperCase() + messenger.getString("Duplication").substring(1) + " " + messenger.getString("Enchantment")),
                            messenger.getString("Duplication").substring(0, 1).toUpperCase() + messenger.getString("Duplication").substring(1) + " " + messenger.getString("Enchantment") + " " + messenger.getString("EnchantmentLevel")
                    );
                    return true;
                }
            }else if(args[0].equalsIgnoreCase("generate")) {
                ItemStack item = new ItemStack(maker.material.get(player.getUniqueId()));
                player.getInventory().addItem(item);

                maker.material.put(player.getUniqueId(), null);
                maker.name.put(player.getUniqueId(), null);
                return true;
            }else if(args[0].equalsIgnoreCase("item_flag")) {

            }else if(args[0].equalsIgnoreCase("lore")) {

            }else if(args[0].equalsIgnoreCase("material")) {
                if(Manager.specialMaterial.contains(Material.valueOf(args[2].toUpperCase()))) {
                    if(permissionCheck(player, "AdminItems")) {
                        throw new NotAllowPermissionException();
                    }
                }
                switch(args[1]) {
                    case "edit" -> {

                    }
                    case "remove" -> {

                    }
                    case "set" -> {
                        maker.material.put(player.getUniqueId(), Material.valueOf(args[2].toUpperCase()));
                        player.sendMessage(String.format("[%s] %s", plugin.getName(), messenger.getString("MakerSet").replaceAll("<ARGUMENT>", "Material")));
                    }
                }
            }else if(args[0].equalsIgnoreCase("name")) {

            }
        }catch (NotProvideException e) {
            sender.sendMessage(String.format("[%s] %s", plugin.getName(), messenger.getString("NotProvide")));
            return true;
        }
        catch (NotAllowPermissionException e) {
            sender.sendMessage(String.format("[%s] %s", plugin.getName(), messenger.getString("PermissionDeny")));
            return true;
        }
        catch (AlreadyIncludeException e) {
            sender.sendMessage(String.format("[%s] %s", plugin.getName(), messenger.getString("MakerAlreadyInclude").replaceAll("<ARGUMENT>", "Material")));
            return true;
        }
        catch (NullPointerException e) {
            sender.sendMessage(String.format("[%s] %s", plugin.getName(), messenger.getString("MakerArgumentIsNull").replaceAll("<ARGUMENT>", "")));
            return true;
        }catch (IllegalArgumentException e) {
            sender.sendMessage(String.format(""));
            return true;
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
                    if(permissionCheck(sender, ConfigMenu.valueOf(str.toUpperCase()).toString())) {
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
                    if(permissionCheck(sender, "Enchanting")) {
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
                    if(permissionCheck(sender, "Attribute")) {
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
                    if(permissionCheck(sender, "ItemFlag")) {
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
                    if(permissionCheck(sender, "Lore")) {
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
                    if(permissionCheck(sender, "Basic")) {
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
                    if(permissionCheck(sender, "Attribute")) {
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
                    if(permissionCheck(sender, "ItemFlag")) {
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
                    if(permissionCheck(sender, "Basic")) {
                        return null;
                    }
                    if(args[1].equalsIgnoreCase("set")) {
                        //マテリアル一覧
                        for (Material m : Material.values()) {
                            if(checkSpecialMaterial(m)) {
                                if(!sender.hasPermission(config.getString("Maker.GlobalSetting.Admin.Permission.All"))) {
                                    if(!sender.hasPermission(config.getString("Maker.GlobalSetting.Admin.Permission.AdminItems"))) {
                                        if(!sender.hasPermission(config.getString("item-manager.maker.admin-items.everyone"))) {
                                            continue;
                                        }
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

                    }else{
                        if(maker.material == null || maker.material.isEmpty()) {
                            return null;
                        }
                        result.add(maker.material.get(player.getUniqueId()).getKey().getKey());
                    }
                    return result;
                }
                if (args[0].equalsIgnoreCase("name")) {
                    if(permissionCheck(sender, "ColorName")) {
                        return null;
                    }
                    return new ArrayList<>(List.of("color"));
                }
                if (args[0].equalsIgnoreCase("enchanting")) {
                    //エンチャント一覧
                    if(permissionCheck(sender, "Enchanting")) {
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
                    if(permissionCheck(sender, "ColorName")) {
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
                    if(permissionCheck(sender, "Enchanting")) {
                        return null;
                    }
                    for (int i = 1, l = Enchantment.getByKey(NamespacedKey.minecraft(args[2])).getMaxLevel(); i <= l; i++) {
                        result.add(String.valueOf(i));
                    }
                    return result;
                }
                if(args[0].equalsIgnoreCase("attribute")) {
                    if(permissionCheck(sender, "Attribute")) {
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
                    if(permissionCheck(sender, "ColorName")) {
                        return null;
                    }
                    return new ArrayList<>(List.of("color"));
                }
            }
            if (args.length == 5) {
                if (args[3].equalsIgnoreCase("color")) {
                    if(permissionCheck(sender, "ColorName")) {
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
                    if(permissionCheck(sender, "Attribute")) {
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
                    if(permissionCheck(sender, "Attribute")) {
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

    private boolean permissionCheck(CommandSender sender, String argType) {
        if(sender.hasPermission(config.getString("Maker.GlobalSetting.Admin.Permission.All"))) {
            return false;
        }
        if(sender.hasPermission(config.getString("Maker.GlobalSetting.Admin.Permission." + argType))) {
            return false;
        }
        if(sender.hasPermission(config.getString("Maker.GlobalSetting.Citizen.Permission.All"))) {
            return false;
        }
        return !sender.hasPermission(config.getString("Maker.GlobalSetting.Citizen.Permission." + argType));
    }
}