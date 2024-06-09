package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.GUI.ReportGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    private final ReportGUI reportGUI;

    public ReportCommand(ReportGUI reportGUI) {
        this.reportGUI = reportGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String reportedPlayer = args[0];
                reportGUI.openReportGUI(player, reportedPlayer);
            } else {
                player.sendMessage("Usage: /report <player>");
            }
            return true;
        } else {
            sender.sendMessage("Only players can use this command.");
            return false;
        }
    }
}
