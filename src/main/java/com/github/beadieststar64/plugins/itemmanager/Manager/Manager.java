package com.github.beadieststar64.plugins.itemmanager.Manager;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Manager {

    public static ArrayList<Material> specialMaterial = new ArrayList<>(
            Arrays.asList(
                    Material.PETRIFIED_OAK_SLAB,
                    Material.COMMAND_BLOCK,
                    Material.BARRIER,
                    Material.LIGHT,
                    Material.STRUCTURE_BLOCK,
                    Material.STRUCTURE_VOID,
                    Material.JIGSAW,
                    Material.SPAWNER,
                    Material.COMMAND_BLOCK_MINECART,
                    Material.KNOWLEDGE_BOOK,
                    Material.DEBUG_STICK,
                    Material.BUNDLE
            )
    );

    public boolean checkSpecialMaterial(Material material) {
        try {
            if(specialMaterial.contains(material)) {
                return true;
            }
            if(material.name().toUpperCase().contains("SPAWN_EGG")) {
                return true;
            }
        }catch (IllegalArgumentException e) {
            return true;
        }
        return false;
    }

    public static enum ManagerArg {
        ATTRIBUTE("Attribute"),
        CANCEL("Basic"),
        ENCHANTING("Enchanting"),
        GENERATE("Basic"),
        ITEM_FLAG("ItemFlag"),
        LORE("Lore"),
        MATERIAL("Basic"),
        NAME("Basic");

        private final String str;

        private ManagerArg(String str) {
            this.str = str;
        }

        public String toString() {
            return this.str;
        }
    }
}
