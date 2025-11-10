package io.lumia.lumiadevhub.plugin.menus.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class MainHolder implements InventoryHolder {
    static String name;
    static Player player;

    public MainHolder(String name, Player player) {
        MainHolder.name = name;
        MainHolder.player = player;
    }

    public static String getName() {
        return name;
    }

    public static Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
