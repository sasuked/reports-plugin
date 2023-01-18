package io.github.sasuked.reportsplugin.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class PlayerProvider {

    public static OfflinePlayer getOfflinePlayer(UUID uuid) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
          .filter(it -> it.getUniqueId().equals(uuid))
          .findFirst()
          .orElse(null);
    }

    public static OfflinePlayer getOfflinePlayerOrNull(String playerName) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
          .filter(it -> it.getName().equalsIgnoreCase(playerName))
          .findFirst()
          .orElse(null);
    }

    public static Player getPlayer(String playerName) {
        return Bukkit.getOnlinePlayers()
          .stream()
          .filter(it -> it.getName().equalsIgnoreCase(playerName))
          .findFirst()
          .orElse(null);
    }


}
