package com.github.beadieststar64.plugins.itemmanager.Maker;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Maker implements Listener {

    Map<UUID, Material> material = new HashMap<>();
    Map<UUID, ItemMeta> meta = new HashMap<>();
    Map<UUID, String> name = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        material.put(player.getUniqueId(), null);
        meta.put(player.getUniqueId(), null);
        name.put(player.getUniqueId(), null);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        material.remove(player.getUniqueId());
        meta.remove(player.getUniqueId());
        name.remove(player.getUniqueId());
    }
}
