package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.GUI.ReportGUI;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ReportCommand implements CommandExecutor {

    private final ReportGUI reportGUI;

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    private int cooldownTime = 0;

    private final Config config;

    public ReportCommand(ReportGUI reportGUI, Config config) {
        this.reportGUI = reportGUI;
        this.config = config;
        this.cooldownTime = config.getValueInt("report-cooldown");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String reportedPlayer = args[0];
                if (isInCooldown(player.getUniqueId())) {
                    long timeLeft = (cooldowns.get(player.getUniqueId()) / 1000 + cooldownTime) - (System.currentTimeMillis() / 1000);
                    player.sendMessage("You must wait " + timeLeft + " seconds to use this command again.");
                    return true;
                }

                if (reportedPlayer.equals(player.getName())){
                    player.sendMessage(color.c("&cYou cannot report yourself!"));

                } else {
                    setCooldown(player.getUniqueId());
                    reportGUI.openReportGUI(player, reportedPlayer);
                }
            } else {
                player.sendMessage("Usage: /report <player>");
            }
            return true;
        } else {
            sender.sendMessage("Only players can use this command.");
            return false;
        }
    }

    private boolean isInCooldown(UUID playerUUID) {
        if (cooldowns.containsKey(playerUUID)) {
            long timePassed = (System.currentTimeMillis() - cooldowns.get(playerUUID)) / 1000;
            return timePassed < cooldownTime;
        }
        return false;
    }

    private void setCooldown(UUID playerUUID) {
        cooldowns.put(playerUUID, System.currentTimeMillis());
    }
}
