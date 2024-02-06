package com.github.beadieststar64.plugins.itemmanager.Maker;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Maker implements Listener {

    Map<UUID, Material> material = new HashMap<>();
    Map<UUID, ItemStack> item = new HashMap<>();
    Map<UUID, ItemMeta> meta = new HashMap<>();
    Map<UUID, String> name = new HashMap<>();
    Map<UUID, ArrayList<Enchantment>> enchantments = new HashMap<>();
    Map<UUID, ArrayList<Integer>> enchantmentLevels = new HashMap<>();
    Map<UUID, ArrayList<Attribute>> attributes = new HashMap<>();
    Map<UUID, ArrayList<UUID>> modifierUUIDs = new HashMap<>();
    Map<UUID, ArrayList<String>> modifierNames = new HashMap<>();
    Map<UUID, ArrayList<Double>> modifierAmount = new HashMap<>();
    Map<UUID, ArrayList<AttributeModifier.Operation>> modifierOperations = new HashMap<>();
    Map<UUID, ArrayList<EquipmentSlot>> modifierSlots = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        material.put(player.getUniqueId(), null);
        item.put(player.getUniqueId(), null);
        meta.put(player.getUniqueId(), null);
        name.put(player.getUniqueId(), null);
        enchantments.put(player.getUniqueId(), null);
        enchantmentLevels.put(player.getUniqueId(), null);
        attributes.put(player.getUniqueId(), null);
        modifierUUIDs.put(player.getUniqueId(), null);
        modifierNames.put(player.getUniqueId(), null);
        modifierAmount.put(player.getUniqueId(), null);
        modifierOperations.put(player.getUniqueId(), null);
        modifierSlots.put(player.getUniqueId(), null);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        material.remove(player.getUniqueId());
        item.remove(player.getUniqueId());
        meta.remove(player.getUniqueId());
        name.remove(player.getUniqueId());
        enchantments.remove(player.getUniqueId());
        enchantmentLevels.remove(player.getUniqueId());
        attributes.remove(player.getUniqueId());
        modifierUUIDs.remove(player.getUniqueId());
        modifierNames.remove(player.getUniqueId());
        modifierAmount.remove(player.getUniqueId());
        modifierOperations.remove(player.getUniqueId());
        modifierSlots.remove(player.getUniqueId());
    }
}
