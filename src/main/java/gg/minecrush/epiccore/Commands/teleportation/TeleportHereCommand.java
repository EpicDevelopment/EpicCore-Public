package gg.minecrush.epiccore.Commands.teleportation;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportHereCommand implements CommandExecutor {

    Lang lang;
    Config config;

    public TeleportHereCommand(Lang lang, Config config) {
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {

            if (!(args.length == 1)) {
                player.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/gmsp"));
                return false;
            }

            if (!player.hasPermission("teleport-here-command-permission")) {
                player.sendMessage(lang.getReplacedMessage("no-permission"));
                return false;
            }

            if (args[0].equals("*")){
                for (Player p: Bukkit.getOnlinePlayers()){
                    p.teleport(player.getLocation());
                }
                player.sendMessage(lang.getReplacedMessage("teleported-all"));
                return true;

            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(lang.getReplacedMessage("invalid-player"));
                    return false;
                }
                target.teleport(player.getLocation());
                player.sendMessage(lang.getReplacedMessage("teleport-success").replace("%player%", player.getName()).replace("%target%", target.getName()));
            }

        } else {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
        }
        return false;
    }
}
