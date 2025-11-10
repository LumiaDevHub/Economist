package io.lumia.lumiadevhub.plugin.commands;

import io.lumia.lumiadevhub.plugin.SqlManager;
import io.lumia.lumiadevhub.plugin.menus.PinCodeMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.lumia.lumiadevhub.plugin.SqlManager.createAccount;
import static io.lumia.lumiadevhub.plugin.Economist.getPlugin;
import static io.lumia.lumiadevhub.plugin.Economist.sendMessage;

public class PlayerCommands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        FileConfiguration c = getPlugin().getConfig();
        FileConfiguration m = getPlugin().getMessages();
        if(!(commandSender instanceof Player p)) {
            sendMessage((Player) commandSender, m.getString("Messages.Errors.consoleError"));
            return true;
        }

        switch (command.getName()) {
            case "bank":
                SqlManager.haveAccount(p, (call) -> {
                    if(!call) {
                        sendMessage(p, m.getString("Messages.Errors.accountError"));
                        p.closeInventory();
                        return;
                    }
                });

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        PinCodeMenu.openOneNumberPin(p);
                    }
                }.runTaskLater(getPlugin(), 20L);

                break;
            case "bcreate":
            if(strings.length < 2) {
                sendMessage(p, m.getString("Message.Errors.lengthError"));
                return true;
            }

            if(strings[1].length() != 4) {
                String pin = strings[1].length() > 4
                        ? "Messages.Errors.pinCodeErrorMinus"
                        : "Messages.Errors.pinCodeErrorPlus";
                sendMessage(p, m.getString(pin).replace("%pin", strings[1]));
            }

            createAccount(p, strings[0], Integer.parseInt(strings[1]));
            break;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        List<String> tab = new ArrayList<>();
        String cmd = command.getName();
        if(strings.length == 1) {
            if(cmd.equalsIgnoreCase("bcreate")) {
                tab.add("Название счета");
            }
        } else if (strings.length == 2) {
            if(cmd.equalsIgnoreCase("bcreate")) {
                tab.add("PinCode (4 цифры)");
            }
        }

        return tab;
    }

}
