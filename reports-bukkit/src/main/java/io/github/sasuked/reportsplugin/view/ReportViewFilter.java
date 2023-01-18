package io.github.sasuked.reportsplugin.view;

import io.github.sasuked.reportsplugin.api.Report;
import io.github.sasuked.reportsplugin.api.player.PlayerReportContainer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;


@Getter
@RequiredArgsConstructor
public class ReportViewFilter {

    public static final ReportViewFilter MOST_RECENT = new ReportViewFilter(
      "most-recent",
      Comparator.comparingLong(ReportViewFilter::getMostRecentReportTime)
    );

    public static final ReportViewFilter OLDER = new ReportViewFilter(
       "olders",
      Comparator.comparingLong(ReportViewFilter::getMostRecentReportTime).reversed()
    );



    private final String id;
    private final Comparator<PlayerReportContainer> comparator;



    private static long getMostRecentReportTime(PlayerReportContainer reportContainer){
        Report mostRecentReport = reportContainer.getMostRecentReport();
        return mostRecentReport == null ? -1 : mostRecentReport.getCreationTime();
    }
}
