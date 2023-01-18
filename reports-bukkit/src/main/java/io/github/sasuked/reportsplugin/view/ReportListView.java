package io.github.sasuked.reportsplugin.view;

import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.api.player.PlayerReportContainer;
import io.github.sasuked.reportsplugin.util.ItemBuilder;
import io.github.sasuked.reportsplugin.util.Skulls;
import me.saiintbrisson.minecraft.PaginatedView;
import me.saiintbrisson.minecraft.PaginatedViewSlotContext;
import me.saiintbrisson.minecraft.ViewContext;
import me.saiintbrisson.minecraft.ViewItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class ReportListView extends PaginatedView<PlayerReportContainer> {


    public ReportListView() {
        super(6, "Reports");

        setLayout(
          "XXXXXXXXX",
          "XXOOOOOOX",
          "<XOOOOOO>",
          "XXOOOOOOX",
          "XXXXXXXXX",
          "XXXXZXXXX"
        );

        setLayout('Z', viewItem -> viewItem
          .onClick(click -> click.open(ReportListFilterView.class))
          .setItem(new ItemBuilder(Material.HOPPER)
            .name("§bFiltrar categoria")
            .lore(
              "§7Clique para selecionar os",
              "§7os reports que deseja ver."
            )
            .build()));
    }

    @Override
    protected void onRender(@NotNull ViewContext ctx) {
        ReportViewFilter filter = ctx.get("currentFilter");
        List<PlayerReportContainer> containers = ctx.get("container");

        ctx.paginated().setSource(containers.stream()
          .sorted(filter.getComparator())
          .collect(Collectors.toList()));
    }

    @Override
    protected void onItemRender(
      @NotNull PaginatedViewSlotContext<PlayerReportContainer> context,
      @NotNull ViewItem viewItem,
      @NotNull PlayerReportContainer value
    ) {
        viewItem.onRender(render -> render.setItem(generateContainerIcon(value)));
    }

    private @NotNull ItemStack generateContainerIcon(@NotNull PlayerReportContainer container) {
        ItemBuilder builder = new ItemBuilder(Skulls.skullFromName(container.getPlayerName()));

        builder.addLore(
          "",
          " §fTotal: §7" + container.getTotalReports(),
          ""
        );
        for (ReportType value : ReportType.values()) {
            int count = container.countReportsByType(value);
            if (count > 0) {
                builder.addLore(" §8▪ §f" + value.getName() + ": §7" + count);
            }
        }


        builder.addLore(
          "",
          "§7Clique para mais detalhes."
        );

        return builder.build();
    }
}
