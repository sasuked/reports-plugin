package io.github.sasuked.reportsplugin.api.service;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.api.player.PlayerReportContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ReportService {

    @NotNull CompletableFuture<List<? extends Report>> getAllReports();

    @NotNull CompletableFuture<List<? extends Report>> getAllFromReportedPlayerName(
      @NotNull String playerName
    );

    @NotNull CompletableFuture<List<? extends Report>> getAllFromAuthorName(
      @NotNull String authorName
    );

    @NotNull CompletableFuture<? extends Report> createReport(
      @NotNull String playerName,
      @NotNull String authorName,
      @NotNull ReportType type
    );

    @NotNull CompletableFuture<Report> deleteReport(
      @NotNull UUID reportId
    );

    @NotNull CompletableFuture<PlayerReportContainer> getPlayerReportContainer(
      @NotNull String playerName
    );

    @NotNull CompletableFuture<List<PlayerReportContainer>> getAllPlayerReportContainers();
}
