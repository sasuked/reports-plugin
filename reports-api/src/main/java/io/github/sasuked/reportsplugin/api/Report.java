package io.github.sasuked.reportsplugin.api;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Report {

    @NotNull UUID getUniqueId();

    @NotNull UUID getReportedPlayerId();

    @NotNull UUID getAuthorId();

    @NotNull ReportType getType();

    long getCreationTime();

    long getExpirationTime();
}
