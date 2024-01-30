package com.github.beadieststar64.plugins.itemmanager.Maker;

import com.github.beadieststar64.plugins.itemmanager.ItemManager;
import com.github.beadieststar64.plugins.itemmanager.YamlLoader;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakerCommand implements TabExecutor {

    static ItemStack item;
    static ItemMeta meta;
    static Material material;

    ItemManager plugin;

    public MakerCommand(ItemManager plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        FileConfiguration messenger = new YamlLoader(plugin, "message.yml").getConfig();
        String[] splitMsg;
        if(message.contains("#maker")) {
            splitMsg = message.split(" ");

            if(splitMsg.length < 2) {
                return;
            }

            event.setCancelled(true);

            switch (splitMsg[1]) {
                case "material" -> {
                    try {
                        material = Material.valueOf(splitMsg[2].toUpperCase());
                        item = new ItemStack(material);
                        meta = item.getItemMeta();

                        player.sendMessage(Objects.requireNonNull(messenger.getString("MakerSetMaterial")));
                    }catch (Exception e) {
                        player.sendMessage(Objects.requireNonNull(messenger.getString("MaterialNotFound")));
                    }
                }
                case "name" -> {
                    handleNameCommand(message);

                }
                case "enchanting" -> {

                }
                case "attribute" -> {

                }
                case "item-flag" -> {

                }
                case "generate" -> {
                    item.setItemMeta(meta);
                    player.getInventory().addItem(item);
                    player.updateInventory();
                    player.sendMessage("生成に成功しました");
                    item = null;
                    meta = null;
                }
                case "cancel" -> {

                }
            }
        }
    }

    // 名前を処理するメソッド
    private void handleNameCommand(String message) {
        String[] splitMsg;

        if (meta == null) {
            setup();
        }

        Pattern masterP = Pattern.compile("(?<=\\[).*?(?=])");
        Matcher matcherP = masterP.matcher(message);
        List<String> list = new ArrayList<>();
        List<String> optionList = new ArrayList<>();

        while (matcherP.find()) {
            String data = message.substring(matcherP.start(), matcherP.end());
            list.add(data);
            message = message.replace(data, "A");
            message = message.replaceFirst("[\\[]A[]]", "");
            matcherP = masterP.matcher(message);
        }

        splitMsg = message.split(" ");
        if(splitMsg.length > 2) {
            if(splitMsg.length > 4) {
                if(splitMsg.length > 5) {
                    //bold等を反映
                    //ex. #maker name [Name] color [hex] bold under_line
                }
                //名前の色を反映
                splitMsg[4] = list.get(1);
                if (splitMsg[3].equalsIgnoreCase("color")) {

                }
            }
            //名前を反映
            splitMsg[2] = list.get(0);
        }



        StringBuilder displayNameBuilder = new StringBuilder();
        displayNameBuilder.append(list.get(0));

        if (!optionList.isEmpty()) {
            for (String s : optionList) {
                displayNameBuilder.append(s);
            }
        }

        displayNameBuilder.append(list.get(0).replaceAll("[\\[\\]]", ""));

        meta.setDisplayName(displayNameBuilder.toString());
    }

    // 色を処理するメソッド
    private List<String> handleColorCommand(List<String> list) {
        Pattern colorP = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher colorM = colorP.matcher(list.get(1));
        List<String> colorL = new ArrayList<>();

        int i = 0;
        while (colorM.find()) {
            String color = list.get(1).substring(colorM.start(), colorM.end());
            String str = list.get(1).replace(color, ChatColor.of(color) + "");
            colorL.set(i, ChatColor.translateAlternateColorCodes('&', str));
            colorM = colorP.matcher(list.get(1));
        }
        return colorL;
    }


    private void setup() {
        if(material == null) {
            return;
        }
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
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
        if(args.length == 1) {
            argument.add("attribute");
            argument.add("cancel");
            argument.add("enchanting");
            argument.add("generate");
            argument.add("item-flag");
            argument.add("lore");
            argument.add("material");
            argument.add("name");

            for(String str : argument) {
                if(str.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(str);
                }
            }
            return result;
        }
        if(args.length == 2) {
            argument.add("edit");
            argument.add("set");
            argument.add("remove");

            if(args[0].equalsIgnoreCase("enchanting")) {
                //メタに対するエンチャントの動作
                for(String str : argument) {
                    if(str.toLowerCase().startsWith(args[1])) {
                        result.add(str);
                    }
                }
                return result;
            }
            if(args[0].equalsIgnoreCase("attribute")) {
                for(String str : argument) {
                    if(str.toLowerCase().startsWith(args[1])) {
                        result.add(str);
                    }
                }
                return result;
            }
            if(args[0].equalsIgnoreCase("item-flag")) {
                for(String str : argument) {
                    if(str.toLowerCase().startsWith(args[1])) {
                        result.add(str);
                    }
                }
                return result;
            }
            if(args[0].equalsIgnoreCase("lore")) {
                for(String str : argument) {
                    if(str.toLowerCase().startsWith(args[1])) {
                        result.add(str);
                    }
                }
                return result;
            }
            if(args[0].equalsIgnoreCase("material")) {
                for(String str : argument) {
                    if(str.toLowerCase().startsWith(args[1])) {
                        result.add(str);
                    }
                }
            }
        }
        if(args.length == 3) {
            if(args[0].equalsIgnoreCase("attribute")) {
                //属性一覧
                for(Attribute attribute : Attribute.values()) {
                    argument.add(attribute.getKey().getKey());
                }
                for(String str : argument) {
                    if(str.toLowerCase().startsWith(args[2])) {
                        result.add(str);
                    }
                }
                return result;
            }

            if(args[0].equalsIgnoreCase("item-flag")) {
                //アイテムフラッグ一覧
                for(ItemFlag flag : ItemFlag.values()) {
                    argument.add(flag.name().toLowerCase());
                }
                for(String str : argument) {
                    if(str.toLowerCase().startsWith(args[2])) {
                        result.add(str);
                    }
                }
                return result;
            }
            if(args[0].equalsIgnoreCase("lore")) {
                return new ArrayList<>(List.of("color"));
            }
            if(args[0].equalsIgnoreCase("material")) {
                //マテリアル一覧
                for(Material m : Material.values()) {
                    argument.add(m.name().toLowerCase());
                }
                for(String str : argument) {
                    if(str.toLowerCase().startsWith(args[2])) {
                        result.add(str);
                    }
                }
                return result;
            }
            if(args[0].equalsIgnoreCase("name")) {
                return new ArrayList<>(List.of("color"));
            }
            if(args[0].equalsIgnoreCase("enchanting")) {
                //エンチャント一覧
                for(Enchantment enchantment : Enchantment.values()) {
                    argument.add(enchantment.getKey().getKey());
                }
                for(String str : argument) {
                    if(str.toLowerCase().startsWith(args[2].toLowerCase())) {
                        result.add(str);
                    }
                }
                return result;
            }
        }
        if(args.length == 4) {
            if(args[2].equalsIgnoreCase("color")) {
                for(String color : colorList) {
                    if(color.toLowerCase().startsWith(args[3])) {
                        result.add(color);
                    }
                }
                return result;
            }
            if(args[0].equalsIgnoreCase("enchanting")) {
                for(int i = 1, l = Enchantment.getByKey(NamespacedKey.minecraft(args[2])).getMaxLevel(); i <= l; i++) {
                    result.add(String.valueOf(i));
                }
                return result;
            }
        }
        if(args.length == 5) {
            if(colorList.contains(args[3])) {
                for(String option : colorOptionList) {
                    if(option.toLowerCase().startsWith(args[4])) {
                        result.add(option);
                    }
                }
                return result;
            }

        }

        return new ArrayList<>(List.of(""));
    }
}
