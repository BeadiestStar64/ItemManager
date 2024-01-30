package com.github.beadieststar64.plugins.itemmanager.Maker;

import com.github.beadieststar64.plugins.itemmanager.ItemManager;
import com.github.beadieststar64.plugins.itemmanager.YamlLoader;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakerCommand implements Listener {

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
}
