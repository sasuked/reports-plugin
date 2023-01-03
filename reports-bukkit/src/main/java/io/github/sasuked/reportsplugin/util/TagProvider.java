package io.github.sasuked.reportsplugin.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TagProvider {

    private static final LuckPerms PROVIDER = LuckPermsProvider.get();

    public static String getTagById(String id) {
        final Group group = PROVIDER.getGroupManager().getGroup(id);
        if (group == null) return "";

        final String prefix = getCachedMetadata(group).getPrefix();
        if (prefix == null) return "";

        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public static String getTagWithName(Player player) {
        final CachedMetaData metaData = getCachedMetadata(player.getName());
        if (metaData == null || metaData.getPrefix() == null) return "";

        return ChatColor.translateAlternateColorCodes('&', metaData.getPrefix())
          + player.getName();
    }

    public static String getTagWithName(OfflinePlayer player) {
        final CachedMetaData metaData = getCachedMetadata(player.getName());
        if (metaData == null || metaData.getPrefix() == null) return "";

        return ChatColor.translateAlternateColorCodes('&', metaData.getPrefix())
          + player.getName();
    }

    public static String getOfflineTagWithName(String playerName) {
        final OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if (player == null) return "";

        return getOfflineTagWithName(player);
    }

    public static String getOfflineTagWithName(OfflinePlayer player) {
        final UUID uniqueId = player.getUniqueId();

        final UserManager userManager = PROVIDER.getUserManager();
        if (userManager.isLoaded(uniqueId)) return getTagWithName(player);

        try {
            final User user = userManager.loadUser(uniqueId).get();
            final CachedDataManager cachedData = user.getCachedData();

            final CachedMetaData metaData = cachedData.getMetaData();
            if (metaData.getPrefix() == null) return "";

            return ChatColor.translateAlternateColorCodes(
              '&', metaData.getPrefix()
            ) + player.getName();
        } catch (Exception exception) {
            return "";
        }
    }

    public static String getTagWithNameByName(String playerName) {
        final Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            return getOfflineTagWithName(offlinePlayer);
        }

        return getOnlineTagWithName(player);
    }

    public static String getOnlineTagWithName(Player player) {
        final UUID uniqueId = player.getUniqueId();

        final UserManager userManager = PROVIDER.getUserManager();
        if (userManager.isLoaded(uniqueId)) return getTagWithName(player);

        try {
            final User user = userManager.loadUser(uniqueId).get();
            final CachedDataManager cachedData = user.getCachedData();

            final CachedMetaData metaData = cachedData.getMetaData();
            if (metaData.getPrefix() == null) return "";

            return ChatColor.translateAlternateColorCodes(
              '&', metaData.getPrefix()
            ) + player.getName();
        } catch (Exception exception) {
            return "";
        }
    }

    public static String getTagColor(String tag) {
        return getTagById(tag)
          .substring(0, 2);
    }

    public static String getTagColor(OfflinePlayer player) {
        return getOfflineTagWithName(player)
          .substring(0, 2);
    }

    public static String getTagColorWithName(OfflinePlayer player) {
        return getTagColor(player) + player.getName();
    }

    public static boolean isStaff(UUID uniqueId) {
        final UserManager userManager = PROVIDER.getUserManager();

        User user;
        try {
            user = userManager.getUser(uniqueId);
            if (user == null) user = userManager.loadUser(uniqueId).get();
        } catch (Exception exception) {
            return false;
        }

        return user.getCachedData()
          .getPermissionData(QueryOptions.nonContextual())
          .checkPermission("currencies.bypass")
          .asBoolean();
    }

    public static boolean isStaff(String playerName) {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return isStaff(offlinePlayer);
    }

    public static boolean isStaff(Player player) {
        return isStaff(player.getUniqueId());
    }

    public static boolean isStaff(OfflinePlayer player) {
        return isStaff(player.getUniqueId());
    }

    private static CachedMetaData getCachedMetadata(String userName) {
        final User user = PROVIDER.getUserManager().getUser(userName);
        if (user == null) return null;

        final Group group = PROVIDER.getGroupManager().getGroup(user.getPrimaryGroup());
        if (group == null) return null;

        return group.getCachedData().getMetaData(QueryOptions.nonContextual());
    }

    private static CachedMetaData getCachedMetadata(Group group) {
        return group.getCachedData().getMetaData();
    }
}