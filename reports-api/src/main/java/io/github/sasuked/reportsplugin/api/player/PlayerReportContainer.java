package io.github.sasuked.reportsplugin.api.player;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.ReportType;
import org.jetbrains.annotations.Nullable;

public interface PlayerReportContainer {

    String getPlayerName();

    int countReportsByType(ReportType type);

    int getTotalReports();

    @Nullable Report getMostRecentReport();
}
