package io.github.sasuked.reportsplugin.player;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.api.player.PlayerReportContainer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class PlayerReportContainerImpl implements PlayerReportContainer {

    private final String playerName;
    private final List<? extends Report> reports;


    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int countReportsByType(ReportType type) {
        return (int) reports.stream()
          .filter(it -> it.getType() == type)
          .count();
    }

    @Override
    public int getTotalReports() {
        return reports.size();
    }

    @Override
    public @Nullable Report getMostRecentReport() {
        return reports.stream()
          .max(Comparator.comparingLong(Report::getCreationTime))
          .orElse(null);
    }
}
