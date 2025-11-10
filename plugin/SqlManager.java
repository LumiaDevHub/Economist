package io.lumia.lumiadevhub.plugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.function.Consumer;

import static io.lumia.lumiadevhub.plugin.Economist.getPlugin;
import static io.lumia.lumiadevhub.plugin.Economist.sendMessage;
import static org.bukkit.Bukkit.getLogger;

public class SqlManager {

    static FileConfiguration c = Economist.getPlugin().getConfig();
    static FileConfiguration m = Economist.getPlugin().getMessages();

    public static void haveAccount(Player p, Consumer<Boolean> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            boolean have = false;
            String sql = "SELECT Player FROM economist_Main WHERE Player = ?";
            try (Connection conn = getPlugin().getConnection();
                 PreparedStatement checkStmt = conn.prepareStatement(sql)) {
                checkStmt.setString(1, p.getName());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    have = rs.next();
                }
            } catch (Exception e) {
                getLogger().warning("❌ LCTE 0");
                getLogger().warning("❌ LCTE 0 | Ошибка с проверкой аккаунта! - haveAccount -");
                getLogger().warning(e.getMessage());
                getLogger().warning("❌ LCTE 0");
            }

            boolean finalHave = have;
            Bukkit.getScheduler().runTask(getPlugin(), () -> callback.accept(finalHave));
        });
    }

    public static void getAccount(Player p, Consumer<String> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            String name = "";
            String sql = "SELECT Name FROM economist_Main WHERE Player = ?";
            try (Connection conn = getPlugin().getConnection();
                 PreparedStatement checkStmt = conn.prepareStatement(sql)) {
                checkStmt.setString(1, p.getName());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    name = rs.getString("Name");
                }
            } catch (Exception e) {
                getLogger().warning("❌ LCTE 1");
                getLogger().warning("❌ LCTE 1 | Ошибка с проверкой аккаунта! - getAccount -");
                getLogger().warning(e.getMessage());
                getLogger().warning("❌ LCTE 1");
            }

            String finalName = name;
            Bukkit.getScheduler().runTask(getPlugin(), () -> callback.accept(finalName));
        });
    }

    public static void getPinCode(Player p, Consumer<Integer> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            haveAccount(p, (call) -> {
                if (!call) {
                    Bukkit.getScheduler().runTask(getPlugin(), () -> {
                        sendMessage(p, "Messages.Errors.accountError");
                        callback.accept(0);
                    });
                    return;
                }

                int pass = 0;
                String sql = "SELECT PinCode FROM economist_Main WHERE Player = ?";
                try (Connection conn = getPlugin().getConnection();
                     PreparedStatement checkStmt = conn.prepareStatement(sql)) {
                    checkStmt.setString(1, p.getName());
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            pass = rs.getInt("PinCode");
                        } else {
                            getLogger().warning("No PinCode found for player: " + p.getName());
                        }
                    }
                } catch (Exception e) {
                    getLogger().warning("❌ LCTE 2");
                    getLogger().warning("❌ LCTE 2 | Ошибка с проверкой аккаунта! - getPinCode -");
                    getLogger().warning(e.getMessage());
                    getLogger().warning("❌ LCTE 2");
                }

                int finalPass = pass;
                Bukkit.getScheduler().runTask(getPlugin(), () -> callback.accept(finalPass));
            });
        });
    }

    public static void createAccount(Player p, String name, int pin) {
        haveAccount(p, (call) -> {
            if(call) {
                sendMessage(p, m.getString("Messages.Errors.haveAccountError"));
                return;
            }
        });

        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
           String sql = "INSERT INTO economist_Main (Player, Balance, PinCode, Name) VALUES (?, ?, ?, ?)";
           try (Connection conn = getPlugin().getConnection();
           PreparedStatement insertStmt = conn.prepareStatement(sql)) {
               insertStmt.setString(1, p.getName());
               insertStmt.setInt(2, c.getInt("Settings.startBalance"));
               insertStmt.setInt(3, pin);
               insertStmt.setString(4, name);
               insertStmt.executeUpdate();

               sendMessage(p, m.getString("Messages.successfulCreateAccount"));
           } catch (Exception e) {
               getLogger().warning("❌ LCTE 3");
               getLogger().warning("❌ LCTE 3 | Ошибка с созданием аккаунта! - createAccount -");
               getLogger().warning(e.getMessage());
               getLogger().warning("❌ LCTE 3");
           }
        });
    }
}
