package io.github.sasuked.reportsplugin.service;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.api.service.ReportService;
import io.github.sasuked.reportsplugin.data.provider.ReportDataProvider;
import io.github.sasuked.reportsplugin.implementation.ReportImpl;
import io.github.sasuked.reportsplugin.mapper.ReportMappers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ReportServiceImplementation implements ReportService {

    private final ReportDataProvider provider;

    public ReportServiceImplementation(Map<String , Object> credentials){
        this(new ReportDataProvider(credentials));
    }

    public ReportServiceImplementation(ReportDataProvider provider) {
        this.provider = provider;
    }

    @Override
    public @NotNull CompletableFuture<List<Report>> getAllReports() {
        return provider.selectAll().thenApply(ReportMappers::mapFromDataList);
    }

    @Override
    public @NotNull CompletableFuture<List<Report>> getAllFromReportedId(@NotNull UUID reportedPlayerId) {
        return provider.selectAll()
          .thenApply(ReportMappers::mapFromDataList)
          .thenApply(reports -> reports.stream()
            .filter(it -> it.getReportedPlayerId().equals(reportedPlayerId))
            .collect(Collectors.toList())
          );
    }

    @Override
    public @NotNull CompletableFuture<List<Report>> getAllFromAuthorId(@NotNull UUID authorId) {
        return provider.selectAll()
          .thenApply(ReportMappers::mapFromDataList)
          .thenApply(reports -> reports.stream()
            .filter(it -> it.getReportedPlayerId().equals(authorId))
            .collect(Collectors.toList())
          );
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Report> createReport(
      @NotNull UUID reportedPlayerId,
      @NotNull UUID authorId,
      @NotNull ReportType type
    ) {
        return provider.updateReport(ReportMappers.mapToData(ReportImpl.createNewReport(reportedPlayerId , authorId , type)))
          .thenApply(data -> data == null ? null : ReportMappers.mapFromData(data));
    }

    @Override
    public @NotNull CompletableFuture<Report> deleteReport(@NotNull UUID reportId) {
        return null;
    }
}
