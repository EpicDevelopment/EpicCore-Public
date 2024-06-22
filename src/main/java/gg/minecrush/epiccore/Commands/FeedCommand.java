package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    Lang lang;
    Config config;

    public FeedCommand(Config config, Lang lang){
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
            return false;
        }

        if (!sender.hasPermission(config.getValue("feed-command-permission"))){
            sender.sendMessage(lang.getReplacedMessage("no-permission"));
            return false;
        }

        Player player = (Player) sender;

        if (args.length > 1) {
            player.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/heal"));
            return false;
        }

        Player target = null;
        if (args.length == 1) {
            if (!player.hasPermission(config.getValue("feed-others-command-permission"))){
                sender.sendMessage(lang.getReplacedMessage("no-permission"));
                return false;
            }
            target = Bukkit.getPlayer(args[0]);
        } else {
            target = player;
        }

        if (target == null) {
            sender.sendMessage(lang.getReplacedMessage("invalid-player"));
            return false;
        }

        target.setSaturation(20);
        player.sendMessage(lang.getReplacedMessage("fed-player").replace("%target%", target.getName()));
        return true;
    }

}
