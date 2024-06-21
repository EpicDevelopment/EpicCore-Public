package gg.minecrush.epiccore.Commands.gamemodes;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class gamemodeSpectator implements CommandExecutor {
    Lang lang;
    Config config;

    public gamemodeSpectator(Lang lang, Config config) {
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            if (!sender.hasPermission(config.getValue("gmsp-command-permission"))){
                sender.sendMessage(lang.getReplacedMessage("no-permission"));
                return false;
            }
            Player target = null;
            if (args.length == 1) {
                target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(lang.getReplacedMessage("invalid-player"));
                    return false;
                }
            } else if (args.length == 0){
                target = (Player) sender;
            }

            Player player = (Player) sender;
            if (args.length == 0 || args.length == 1) {
                player.sendMessage(lang.getReplacedMessage("updated-gamemode").replace("%gamemode%", "spectator"));
                target.setGameMode(GameMode.SPECTATOR);
            } else {
                player.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/gmsp"));
            }
        } else {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
        }
        return true;
    }
}
