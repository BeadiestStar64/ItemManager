package com.github.beadieststar64.plugins.itemmanager;

public enum ConfigMenu {

    ATTRIBUTE("Attribute"),
    CANCEL("Basic"),
    ENCHANTING("Enchanting"),
    GENERATE("Basic"),
    ITEM_FLAG("ItemFlag"),
    LORE("Lore"),
    MATERIAL("Basic"),
    NAME("Basic");

    private String key;

    ConfigMenu(String key) {
        this.key = key;
    }

    public String toString() {
        return this.key;
    }
}
