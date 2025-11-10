package io.lumia.lumiadevhub.plugin.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholdersAPI extends PlaceholderExpansion {

    @Override
    @NotNull
    public String getAuthor() {
        return "Lumia";
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "7.7.7";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "7.7.7";
    }

    @Override
    public String getRequiredPlugin() {
        return "Economist";
    }

    @Override
    public String onRequest(OfflinePlayer p, @NotNull String params) {
        if (params.equalsIgnoreCase("lum")) {
            // TODO?
        }

        return null;
    }
}