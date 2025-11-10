package io.lumia.lumiadevhub.API;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface EconomistAPI {
    void haveAccount(Player p, Consumer<Boolean> callback);
    void getAccount(Player p, Consumer<String> callback);
    void getPinCode(Player p, Consumer<Integer> callback);
    void createAccount(Player p, String name, int pin);
}
