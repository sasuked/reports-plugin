package io.github.sasuked.reportsplugin.command;

import io.github.sasuked.reportsplugin.ReportsPlugin;
import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.util.BookUtils;
import io.github.sasuked.reportsplugin.util.TagProvider;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xyz.upperlevel.spigot.book.BookUtil.ClickAction.runCommand;
import static xyz.upperlevel.spigot.book.BookUtil.HoverAction.showText;
import static xyz.upperlevel.spigot.book.BookUtil.TextBuilder.of;

public class ReportCommand extends Command {

    private final ReportsPlugin plugin;

    public ReportCommand(ReportsPlugin plugin) {
        super("report");
        this.plugin = plugin;

        setAliases(Arrays.asList("reportar", "denunciar"));
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player)) return false;

        Player player = (Player) sender;
        if (args.length == 0) {
            sender.sendMessage("§cUso correto: /report <player> [motivo]");
            return false;
        }

        Player target = Bukkit.getOnlinePlayers().stream()
          .filter(it -> it.getName().equalsIgnoreCase(args[0]))
          .findAny()
          .orElse(null);

        if (target == null) {
            player.sendMessage("§c");
            return false;
        }

        if (args.length == 1) {
            BookUtils.openBook(player, buildItem(target));
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            return false;
        }

        ReportType providedReason = ReportType.fromName(args[1]);
        if (providedReason == null) {
            player.sendMessage("§cMotivo não encontrado.");
            return false;
        }

        plugin.getReportService().createReport(target.getUniqueId(), player.getUniqueId(), providedReason)
          .whenCompleteAsync((report, throwable) -> {
              if (throwable != null || report == null) {
                  player.sendMessage("§cFalha no sistema interno de denuncias, por favor tente mais tarde.");
                  player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
              } else {
                  player.sendMessage(new String[]{
                    "§a",
                    "§a Obrigado por reportar " + TagProvider.getTagWithName(player) + " §apor:",
                    "§8  ▪ §f" + providedReason.getName(),
                    "§f",
                    "§a Nossa equipe de moderação irá analisar o comportamento do jogador nas próximas horas.",
                    "§7 A utilização inapropriada deste comando é passível de punição.",
                    "§a"
                  });

                  player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
              }
          });

        return false;
    }

    private ItemStack buildItem(Player player) {
        String playerName = TagProvider.getTagWithName(player); // TODO formatted name with prefix

        BookUtil.BookBuilder book = BookUtil.writtenBook();
        book.title("Reports");
        book.author("Sasuked piroca de tramontina");

        BookUtil.PageBuilder pageBuilder = new BookUtil.PageBuilder();

        List<BaseComponent> components = new ArrayList<>();
        components.add(of("Reportando " + playerName).build());
        components.add(of("§a").build());

        for (ReportType reportType : ReportType.values()) {
            components.add(of("§0 ▪ " + reportType.getName())
              .onHover(showText(String.format("§7Clique para reportar §f%s§7!", player.getName())))
              .onClick(runCommand("/report " + player.getName() + " " + reportType.name().toLowerCase()))
              .build());
        }

        components.add(of("§a").build());
        components.add(of("§aClique §a§lAQUI §apara reportar!").build());

        pageBuilder.add(components.toArray(new BaseComponent[0]));

        book.pages(pageBuilder.build());
        return book.build();
    }
}
