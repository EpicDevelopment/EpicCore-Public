package gg.minecrush.epiccore.Commands.teleportation;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    Lang lang;
    Config config;

    public TeleportCommand(Lang lang, Config config) {
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player player = (Player) sender;
            if (args.length == 0 || args.length > 2) {
                player.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/gmsp"));
                return false;
            }
            if (!player.hasPermission(config.getValue("teleport-command-permission"))) {
                player.sendMessage(lang.getReplacedMessage("no-permission"));
                return false;
            }
            Player target = null;
            Player teleported = null;
            if (args.length == 2) {
                if (!player.hasPermission(config.getValue("teleport-others-command-permission"))) {
                    player.sendMessage(lang.getReplacedMessage("no-permission"));
                    return false;
                }
                teleported = Bukkit.getPlayer(args[0]);
                target = Bukkit.getPlayer(args[1]);
            } else {
                teleported = (Player) sender;
                target = Bukkit.getPlayer(args[0]);
            }
            if (target == null) {
                player.sendMessage(lang.getReplacedMessage("invalid-player"));
                return false;
            }
            if (teleported == null) {
                player.sendMessage(lang.getReplacedMessage("invalid-player"));
                return false;
            }
            teleported.teleport(target);
            teleported.sendMessage(lang.getReplacedMessage("teleport-success").replace("%target%", teleported.getName()).replace("%player%", target.getName()));
        } else {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
        }
        return false;
    }
}
