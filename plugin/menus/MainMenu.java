package io.lumia.lumiadevhub.plugin.menus;

import io.lumia.lumiadevhub.plugin.menus.utils.MainHolder;
import io.lumia.lumiadevhub.plugin.utils.RGBcolors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import static io.lumia.lumiadevhub.plugin.Economist.createItem;
import static net.md_5.bungee.api.ChatColor.stripColor;

public class MainMenu implements Listener {

    public static void openMainMenu(Player p) {
        Inventory i = Bukkit.createInventory(new MainHolder("mainMenu", p), 27, RGBcolors.colorize("Главное меню"));
        i.setItem(12, createItem(Material.ACACIA_DOOR, "Баланс"));
        i.setItem(14, createItem(Material.ACACIA_DOOR, "Перевод"));
        i.setItem(16, createItem(Material.ACACIA_DOOR, "Настройки")); // Например, настроить чтобы в чате баланс не был виден

        p.openInventory(i);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if(item == null || !item.hasItemMeta()) return;

        String name = stripColor(item.getItemMeta().getDisplayName());
        Inventory i = e.getInventory();

        if(!(i.getHolder() instanceof MainHolder)) return;
        InventoryHolder holder = i.getHolder();
        String nameHolder = MainHolder.getName();

        InventoryAction a = e.getAction();
        if(a == InventoryAction.MOVE_TO_OTHER_INVENTORY || a == InventoryAction.CLONE_STACK || a == InventoryAction.COLLECT_TO_CURSOR) return;

        e.setCancelled(true);
        if (e.getClickedInventory() == null) return;
        ClickType ct = e.getClick();
        if (ct != ClickType.LEFT) return;

        // TODO
    }
}
