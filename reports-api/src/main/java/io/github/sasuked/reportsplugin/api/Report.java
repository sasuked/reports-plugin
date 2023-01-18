package io.github.sasuked.reportsplugin.api;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Report {

    @NotNull UUID getUniqueId();

    @NotNull String getReportedPlayerName();

    @NotNull String getAuthorName();

    @NotNull ReportType getType();

    long getCreationTime();

    long getExpirationTime();

    @NotNull ItemStack getPlayerHead();
}
