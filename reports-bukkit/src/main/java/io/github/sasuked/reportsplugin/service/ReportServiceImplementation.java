package io.github.sasuked.reportsplugin.service;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.api.player.PlayerReportContainer;
import io.github.sasuked.reportsplugin.api.service.ReportService;
import io.github.sasuked.reportsplugin.data.provider.ReportDataProvider;
import io.github.sasuked.reportsplugin.mapper.ReportMappers;
import io.github.sasuked.reportsplugin.player.PlayerReportContainerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static io.github.sasuked.reportsplugin.implementation.ReportImpl.createNewReport;

public class ReportServiceImplementation implements ReportService {

    private final ReportDataProvider provider;

    public ReportServiceImplementation(ReportDataProvider provider) {
        this.provider = provider;
    }

    @Override
    public @NotNull CompletableFuture<List<? extends Report>> getAllReports() {
        return provider.selectAll().thenApply(ReportMappers::mapFromDataList);
    }

    @Override
    public @NotNull CompletableFuture<List<? extends Report>> getAllFromReportedPlayerName(@NotNull String playerName) {
        return provider.selectAll()
          .thenApply(ReportMappers::mapFromDataList)
          .thenApply(reports -> reports.stream()
            .filter(it -> it.getReportedPlayerName().equalsIgnoreCase(playerName))
            .collect(Collectors.toList())
          );
    }

    @Override
    public @NotNull CompletableFuture<List<? extends Report>> getAllFromAuthorName(@NotNull String authorName) {
        return provider.selectAll()
          .thenApply(ReportMappers::mapFromDataList)
          .thenApply(reports -> reports.stream()
            .filter(it -> it.getAuthorName().equalsIgnoreCase(authorName))
            .collect(Collectors.toList())
          );
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Report> createReport(
      @NotNull String reportedPlayerName,
      @NotNull String authorName,
      @NotNull ReportType type
    ) {
        return provider.updateReport(ReportMappers.mapToData(createNewReport(reportedPlayerName, authorName, type)))
          .thenApply(data -> data == null ? null : ReportMappers.mapFromData(data));
    }

    @Override
    public @NotNull CompletableFuture<Report> deleteReport(@NotNull UUID reportId) {
        return provider.deleteReport(reportId)
          .thenApply(data -> data != null ? ReportMappers.mapFromData(data) : null);
    }

    @Override
    public @NotNull CompletableFuture<PlayerReportContainer> getPlayerReportContainer(@NotNull String playerName) {
        return this.getAllFromReportedPlayerName(playerName)
          .thenApply(reports -> new PlayerReportContainerImpl(playerName, reports));
    }

    @Override
    public @NotNull CompletableFuture<List<PlayerReportContainer>> getAllPlayerReportContainers() {
        return this.getAllReports()
          .thenApply(reports -> reports.parallelStream()
            .collect(Collectors.groupingBy(Report::getReportedPlayerName))
            .entrySet()
            .parallelStream()
            .map(entry -> new PlayerReportContainerImpl(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList()));
    }
}
