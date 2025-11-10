package io.lumia.lumiadevhub.plugin.menus;

import io.lumia.lumiadevhub.plugin.Economist;
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
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

import static io.lumia.lumiadevhub.plugin.SqlManager.getPinCode;
import static io.lumia.lumiadevhub.plugin.Economist.createItem;
import static io.lumia.lumiadevhub.plugin.Economist.sendMessage;
import static net.md_5.bungee.api.ChatColor.stripColor;
import static org.bukkit.Bukkit.getLogger;
;


public class PinCodeMenu implements Listener {
    HashSet oneNumber = new HashSet();
    HashSet twoNumber = new HashSet();
    HashSet fourNumber = new HashSet();
    HashSet threeNumber = new HashSet();

    public static void openOneNumberPin(Player p) {
        Inventory i = Bukkit.createInventory(new MainHolder("oneNumber", p), 54, RGBcolors.colorize("PinCode: ****"));
        i.setItem(12, createItem(Material.BARRIER, "1"));
        i.setItem(13, createItem(Material.BARRIER, "2"));
        i.setItem(14, createItem(Material.BARRIER, "3"));
        i.setItem(21, createItem(Material.BARRIER, "4"));
        i.setItem(22, createItem(Material.BARRIER, "5"));
        i.setItem(23, createItem(Material.BARRIER, "6"));
        i.setItem(30, createItem(Material.BARRIER, "7"));
        i.setItem(31, createItem(Material.BARRIER, "8"));
        i.setItem(32, createItem(Material.BARRIER, "9"));
        i.setItem(40, createItem(Material.BARRIER, "Закрыть"));
        p.openInventory(i);
    }

    private void openTwoNumberPin(Player p) {
        Inventory i = Bukkit.createInventory(new MainHolder("twoNumber", p), 54, RGBcolors.colorize("PinCode: " + oneNumber.iterator().next() + "***"));
        i.setItem(12, createItem(Material.BARRIER, "1"));
        i.setItem(13, createItem(Material.BARRIER, "2"));
        i.setItem(14, createItem(Material.BARRIER, "3"));
        i.setItem(21, createItem(Material.BARRIER, "4"));
        i.setItem(22, createItem(Material.BARRIER, "5"));
        i.setItem(23, createItem(Material.BARRIER, "6"));
        i.setItem(30, createItem(Material.BARRIER, "7"));
        i.setItem(31, createItem(Material.BARRIER, "8"));
        i.setItem(32, createItem(Material.BARRIER, "9"));
        i.setItem(40, createItem(Material.BARRIER, "Закрыть"));
        p.openInventory(i);
    }

    private void openThreeNumberPin(Player p) {
        Inventory i = Bukkit.createInventory(new MainHolder("threeNumber", p), 54, RGBcolors.colorize("PinCode: " + oneNumber.iterator().next() + twoNumber.iterator().next() + "**"));
        i.setItem(12, createItem(Material.BARRIER, "1"));
        i.setItem(13, createItem(Material.BARRIER, "2"));
        i.setItem(14, createItem(Material.BARRIER, "3"));
        i.setItem(21, createItem(Material.BARRIER, "4"));
        i.setItem(22, createItem(Material.BARRIER, "5"));
        i.setItem(23, createItem(Material.BARRIER, "6"));
        i.setItem(30, createItem(Material.BARRIER, "7"));
        i.setItem(31, createItem(Material.BARRIER, "8"));
        i.setItem(32, createItem(Material.BARRIER, "9"));
        i.setItem(40, createItem(Material.BARRIER, "Закрыть"));
        p.openInventory(i);
    }

    private void openFourNumberPin(Player p) {
        Inventory i = Bukkit.createInventory(new MainHolder("fourNumber", p), 54, RGBcolors.colorize("PinCode: " + oneNumber.iterator().next() + twoNumber.iterator().next() + threeNumber.iterator().next() + "*"));
        i.setItem(12, createItem(Material.BARRIER, "1"));
        i.setItem(13, createItem(Material.BARRIER, "2"));
        i.setItem(14, createItem(Material.BARRIER, "3"));
        i.setItem(21, createItem(Material.BARRIER, "4"));
        i.setItem(22, createItem(Material.BARRIER, "5"));
        i.setItem(23, createItem(Material.BARRIER, "6"));
        i.setItem(30, createItem(Material.BARRIER, "7"));
        i.setItem(31, createItem(Material.BARRIER, "8"));
        i.setItem(32, createItem(Material.BARRIER, "9"));
        i.setItem(40, createItem(Material.BARRIER, "Закрыть"));
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

        if(name.equalsIgnoreCase("Закрыть")) {
            p.closeInventory();
        }

        if(nameHolder.equalsIgnoreCase("oneNumber")) {
            for(int n = 1; n <= 10; n++) {
                if(name.equalsIgnoreCase(String.valueOf(n))) {
                    oneNumber.add(n);
                    openTwoNumberPin(p);
                    break;
                }
            }
        } else if (nameHolder.equalsIgnoreCase("twoNumber")) {
            for(int n = 1; n <= 10; n++) {
                if(name.equalsIgnoreCase(String.valueOf(n))) {
                    twoNumber.add(n);
                    openThreeNumberPin(p);
                    break;
                }
            }
        } else if (nameHolder.equalsIgnoreCase("threeNumber")) {
            for(int n = 1; n <= 10; n++) {
                if(name.equalsIgnoreCase(String.valueOf(n))) {
                    threeNumber.add(n);
                    openFourNumberPin(p);
                    break;
                }
            }
        } else if (nameHolder.equalsIgnoreCase("fourNumber")) {
            for(int n = 1; n <= 10; n++) {
                if(name.equalsIgnoreCase(String.valueOf(n))) {
                    fourNumber.add(n);
                    break;
                }
            }

            getPinCode(p, (basePin) -> {
                String inPin = oneNumber.iterator().next().toString() + twoNumber.iterator().next().toString() + threeNumber.iterator().next().toString() + fourNumber.iterator().next().toString();
                getLogger().info("base " + basePin);
                getLogger().info("in " + inPin);
                if(basePin.equals(inPin)) {
                    MainMenu.openMainMenu(p);
                } else {
                    sendMessage(p, Economist.getPlugin().getMessages().getString("Messages.Errors.invalidError"));
                }
            });
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        Inventory inv = e.getView().getTopInventory();
        if (!(inv.getHolder() instanceof MainHolder holder)) return;
        if (!holder.getName().equalsIgnoreCase("oneNumber") || !holder.getName().equalsIgnoreCase("twoNumber") || !holder.getName().equalsIgnoreCase("threeNumber") || !holder.getName().equalsIgnoreCase("fourNumber")) return;

        e.setCancelled(true);
    }
}
