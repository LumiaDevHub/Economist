package io.lumia.lumiadevhub.plugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.lumia.lumiadevhub.API.EconomistProvider;
import io.lumia.lumiadevhub.API.ManagerAPI;
import io.lumia.lumiadevhub.plugin.commands.PlayerCommands;
import io.lumia.lumiadevhub.plugin.menus.MainMenu;
import io.lumia.lumiadevhub.plugin.menus.PinCodeMenu;
import io.lumia.lumiadevhub.plugin.utils.PlaceholdersAPI;
import io.lumia.lumiadevhub.plugin.utils.RGBcolors;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public final class Economist extends JavaPlugin {

    // ! = LCTE - Lumia Code Throws Error

    // Message
    FileConfiguration message;
    File messages;

    // Main
    PluginManager pm = Bukkit.getPluginManager();
    private HikariDataSource dataSource;
    static Economist plugin;

    // API
    private ManagerAPI api;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        api = new ManagerAPI();
        EconomistProvider.register(api);
        new PlaceholdersAPI().register();
        getConfig().options().copyDefaults(true);


        pm.registerEvents(new PinCodeMenu(), this);
        pm.registerEvents(new MainMenu(), this);

        getCommand("bcreate").setExecutor(new PlayerCommands());
        getCommand("bank").setExecutor(new PlayerCommands());
//        getCommand("aeco").setExecutor(new AdminCommand()); TODO

        loadMessages();
        loadDataBase();
    }

    @Override
    public void onDisable() {
        getLogger().info("");
        getLogger().info("Lumia/Developer..");
        getLogger().info("");
    }

    private void loadDataBase() {
        String type = getConfig().getString("Settings.DataBase.type");
        String user = getConfig().getString("Settings.DataBase.user");
        String pass = getConfig().getString("Settings.DataBase.pass");
        String data = getConfig().getString("Settings.DataBase.data");
        String ipdb = getConfig().getString("Settings.DataBase.ipdb");
        String port = getConfig().getString("Settings.DataBase.port");

        if (type.toLowerCase().equalsIgnoreCase("sbd")) {
            new File(getDataFolder(), "database.db");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:h2:file:" + new File(getDataFolder(), "database").getAbsolutePath());
            config.setDriverClassName("org.h2.Driver");
            config.setUsername(user);
            config.setPassword(pass);
            config.setMaximumPoolSize(10);
            config.setPoolName("Lumia-Mini");
            config.setConnectionTimeout(10000);
            HikariDataSource dataSource = new HikariDataSource(config);

            try (Connection conn = dataSource.getConnection()) {
                getLogger().info("✅ |");
                getLogger().info("✅ | Успешное подключение!");
                getLogger().info("✅ |");
            } catch (SQLException e) {
                getLogger().info("❌");
                getLogger().info("❌ | В чем то проблема.. Стек: " + e.getMessage());
                getLogger().info("❌");
                getServer().shutdown();
            }

            createMain();
        } else if (type.toLowerCase().equalsIgnoreCase("mysql") || (type.toLowerCase().equalsIgnoreCase("mariadb")) || (type.equalsIgnoreCase("sqllite"))) {
            String jbdc = "jdbc:mysql://" + ipdb + ":" + port + "/" + data + "?useSSL=false&serverTimezone=UTC&characterEncoding=utf8&allowPublicKeyRetrieval=true";
            try {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(jbdc);
                config.setUsername(user);
                config.setPassword(pass);
                config.setMaximumPoolSize(10);
                config.setPoolName("Lumia-Pool");
                config.setConnectionTimeout(10000);
                dataSource = new HikariDataSource(config);

                try (Connection conn = dataSource.getConnection()) {
                    getLogger().info("✅ | Успешное подключение!");
                }

                createMain();
            } catch (SQLException e) {
                getLogger().info("❌ | В чем то проблема.. Стек: " + e.getMessage());
                getServer().shutdown();
            }
        }
    }

    // Data | Базы
    private void createMain() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            String sql = "CREATE TABLE IF NOT EXISTS economist_Main (" +
                    "Player VARCHAR(16) NOT NULL, " +
                    "Balance INT(10) DEFAULT 0, " +
                    "PinCode INT(6) NOT NULL, " +
                    "Name VARCHAR(16) NOT NULL, " +
                    "PRIMARY KEY (Player)" +
                    ");";
            try (Connection conn = getConnection();
                 PreparedStatement createStmt = conn.prepareStatement(sql)) {
                createStmt.executeUpdate();
            } catch (SQLException e) {
                getLogger().info("❌ LCTE: 777-Main | В чем то проблема.. Стек: " + e.getMessage());
                getServer().shutdown();
            }
        });
    }

    public void loadMessages() {
        messages = new File(getDataFolder(), "messages.yml");
        if(!messages.exists()) {
            saveResource("messages.yml", false);
        }
        message = YamlConfiguration.loadConfiguration(messages);
    }

    public FileConfiguration getMessages() {
        return message;
    }

    // Getters | Геттеры
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static Economist getPlugin() {
        return plugin;
    }

    public ManagerAPI getApi() {
        return api;
    }

    // Utils | Разное
    public static void sendMessage(Player p, String s) {
        s = PlaceholderAPI.setPlaceholders(p, s);
        p.sendMessage(RGBcolors.colorize(s));
    }

    public static ItemStack createItem(Material mat, String name) {
        ItemStack i = new ItemStack(mat);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(RGBcolors.colorize(name));
        i.setItemMeta(im);
        return i;
    }
    public static ItemStack createItemLore(Material mat, String name, List<String> lore) {
        ItemStack i = new ItemStack(mat);
        ItemMeta im = i.getItemMeta();
        im.setDisplayName(RGBcolors.colorize(name));
        im.setLore(lore);
        i.setItemMeta(im);
        return i;
    }
}
