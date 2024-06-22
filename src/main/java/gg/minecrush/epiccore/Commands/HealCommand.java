package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    Lang lang;
    Config config;

    public HealCommand(Config config, Lang lang){
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
            return false;
        }

        if (!sender.hasPermission(config.getValue("heal-command-permission"))){
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
            if (!player.hasPermission(config.getValue("heal-others-command-permission"))){
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

        target.setHealth(20);
        target.setSaturation(20);
        player.sendMessage(lang.getReplacedMessage("healed-player").replace("%target%", target.getName()));
        return true;
    }

}
