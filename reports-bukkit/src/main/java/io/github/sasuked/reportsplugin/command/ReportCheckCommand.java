package io.github.sasuked.reportsplugin.command;

import io.github.sasuked.reportsplugin.ReportsPlugin;
import io.github.sasuked.reportsplugin.util.PlayerProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class ReportCheckCommand extends Command {


    private final ReportsPlugin plugin;


    public ReportCheckCommand(ReportsPlugin plugin) {
        super("reports");
        this.plugin = plugin;

        setAliases(Collections.singletonList("reportes"));
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("screen.mod")) {
            player.sendMessage("§cVocê não tem permissão para utilizar este comando.");
            return false;
        }

        if (args.length == 0) {
            // TODO ViewProvider.openReportList(player);
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);


        // TODO ViewProvider.openPlayerReports(player , target);

        return false;
    }
}
