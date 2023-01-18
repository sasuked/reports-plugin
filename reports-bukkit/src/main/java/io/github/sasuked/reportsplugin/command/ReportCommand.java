package io.github.sasuked.reportsplugin.command;

import io.github.sasuked.reportsplugin.ReportsPlugin;
import io.github.sasuked.reportsplugin.api.ReportType;
import io.github.sasuked.reportsplugin.book.ReportBooks;
import io.github.sasuked.reportsplugin.util.PlayerProvider;
import io.github.sasuked.reportsplugin.util.TagProvider;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.Arrays;

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

        Player target = PlayerProvider.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cJogador inexistente ou offline.");
            return false;
        }

        // /report <player> = Opens a menu to select the report reason.
        if (args.length == 1) {
            BookUtil.openPlayer(player, ReportBooks.buildReportSelectionBook(target));
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
            return false;
        }

        ReportType providedReason = ReportType.fromName(args[1]);
        if (providedReason == null) {
            player.sendMessage("§cMotivo não encontrado.");
            return false;
        }

        // /report <player> <motivo> = Opens the second menu to confirm the report or switch the current reason.
        if (args.length == 2) {
            BookUtil.openPlayer(player, ReportBooks.buildReportConfirmBook(target, providedReason));
            return false;
        }

        // /report <player> <motivo> confirm = Confirms the selected reason and creates the report..
        if (args[2].equalsIgnoreCase("confirm")) {
            plugin.getReportService()
              .createReport(target.getName(), player.getName(), providedReason)
              .whenComplete((report, throwable) -> {
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

            return true;
        }

        player.sendMessage("§cComando inválido! Use /report <player> para abrir o livro de denuncias!");
        return false;
    }


}
