package gg.minecrush.epiccore.Commands.fly;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private Lang lang;
    private Config config;

    public FlyCommand(Lang lang, Config config) {
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(config.getValue("fly-command-permission"))) {
            player.sendMessage(lang.getReplacedMessage("no-permission"));
            return true;
        }

        if (args.length == 0) {
            toggleFlight(player, player);
        } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                toggleFlight(player, target);
            } else {
                player.sendMessage(lang.getReplacedMessage("invalid-player"));
            }
        }
        return true;
    }

    private void toggleFlight(Player executor, Player target) {
        if (!target.getAllowFlight()) {
            target.setAllowFlight(true);
            target.setFlying(true);
            if (executor.equals(target)) {
                executor.sendMessage(lang.getReplacedMessage("flight-enabled-self").replace("%player%", executor.getName()));
            } else {
                executor.sendMessage(lang.getReplacedMessage("flight-enabled-other")
                        .replace("%target%", target.getName())
                        .replace("%player%", executor.getName()));
                target.sendMessage(lang.getReplacedMessage("flight-enabled-target")
                        .replace("%target%", target.getName())
                        .replace("%player%", executor.getName()));
            }
        } else {
            target.setAllowFlight(false);
            target.setFlying(false);
            if (executor.equals(target)) {
                executor.sendMessage(lang.getReplacedMessage("flight-disabled-self").replace("%player%", executor.getName()));
            } else {
                executor.sendMessage(lang.getReplacedMessage("flight-disabled-other")
                        .replace("%target%", target.getName())
                        .replace("%player%", executor.getName()));
                target.sendMessage(lang.getReplacedMessage("flight-disabled-target")
                        .replace("%target%", target.getName())
                        .replace("%player%", executor.getName()));
            }
        }
    }
}
