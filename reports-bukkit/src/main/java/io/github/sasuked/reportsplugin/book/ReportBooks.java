package io.github.sasuked.reportsplugin.book;

import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.util.TagProvider;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.ArrayList;
import java.util.List;

import static xyz.upperlevel.spigot.book.BookUtil.ClickAction.runCommand;
import static xyz.upperlevel.spigot.book.BookUtil.HoverAction.showText;
import static xyz.upperlevel.spigot.book.BookUtil.TextBuilder.of;

public class ReportBooks {

    public static ItemStack buildReportConfirmBook(Player reportedPlayer, ReportType reportType) {
        String playerName = TagProvider.getTagWithName(reportedPlayer); // TODO formatted name with prefix

        BookUtil.BookBuilder book = BookUtil.writtenBook();
        book.title("Reports");
        book.author("Sasuked");

        BookUtil.PageBuilder pageBuilder = new BookUtil.PageBuilder();

        List<BaseComponent> components = new ArrayList<>();
        components.add(of(String.format("Reportando %s.", playerName)).build());
        components.add(of("§a").build());


        for (ReportType availableType : ReportType.values()) {
            boolean selected = availableType == reportType;

            components.add(of("§0" + (selected ? "§l" : "") + "▪ " + availableType.getName())
              .onHover(showText("§7Clique para selecionar!"))
              .onClick(runCommand(String.format("/report %s %s", reportedPlayer.getName(), availableType.name())))
              .build());
        }

        components.add(of("§a").build());
        components.add(of("§aClique §a§lAQUI §apara reportar!")
          .onHover(showText(String.format("§7Clique para reportar §f%s§7!", reportedPlayer.getName())))
          .onClick(runCommand(String.format("/report %s %s confirm", playerName, reportType.name())))
          .build());

        pageBuilder.add(components.toArray(new BaseComponent[0]));

        book.pages(pageBuilder.build());
        return book.build();
    }


    public static ItemStack buildReportSelectionBook(Player reportedPlayer) {
        String playerName = TagProvider.getTagWithName(reportedPlayer); // TODO formatted name with prefix

        BookUtil.BookBuilder book = BookUtil.writtenBook();
        book.title("Reports");
        book.author("Sasuked");

        BookUtil.PageBuilder pageBuilder = new BookUtil.PageBuilder();

        List<BaseComponent> components = new ArrayList<>();
        components.add(of(String.format("Reportando %s.", playerName)).build());
        components.add(of("§a").build());

        for (ReportType reportType : ReportType.values()) {
            components.add(of("§0 ▪ " + reportType.getName())
              .onHover(showText("§7Clique para selecionar!"))
              .onClick(runCommand(String.format("/report %s %s", reportedPlayer.getName(), reportType.name())))
              .build());
        }

        components.add(of("§a").build());
        components.add(of("§c§lSELECIONE UM MOTIVO!").build());

        pageBuilder.add(components.toArray(new BaseComponent[0]));

        book.pages(pageBuilder.build());
        return book.build();
    }
}
