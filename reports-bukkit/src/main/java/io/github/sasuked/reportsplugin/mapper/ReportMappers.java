package io.github.sasuked.reportsplugin.mapper;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.data.ReportData;
import io.github.sasuked.reportsplugin.implementation.ReportImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ReportMappers {

    public static @NotNull Report mapFromData(@NotNull ReportData data) {
        return new ReportImpl(
          data.getUniqueId(),
          data.getReportedPlayerId(),
          data.getAuthorId(),
          ReportType.valueOf(data.getType().toUpperCase()),
          data.getCreationTime(),
          data.getExpirationTime()
        );
    }

    public static @NotNull ReportData mapToData(@NotNull Report report) {
        return new ReportData(
          report.getUniqueId(),
          report.getReportedPlayerId(),
          report.getAuthorId(),
          report.getType().name(),
          report.getCreationTime(),
          report.getExpirationTime()
        );
    }

    public static @NotNull List<Report> mapFromDataList(@NotNull List<ReportData> dataList) {
        return dataList.stream()
          .map(ReportMappers::mapFromData)
          .collect(Collectors.toList());
    }

    public static @NotNull List<ReportData> mapToDataList(@NotNull List<Report> reports) {
        return reports.stream()
          .map(ReportMappers::mapToData)
          .collect(Collectors.toList());
    }
}
