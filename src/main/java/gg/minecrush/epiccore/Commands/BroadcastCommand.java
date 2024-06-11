package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BroadcastCommand implements CommandExecutor {

    Lang lang;
    Config config;

    public BroadcastCommand(Lang lang, Config config) {
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.hasPermission(config.getValue("broadcast-command-permission")))) {
            sender.sendMessage(lang.getReplacedMessage("no-permission"));
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(lang.getReplacedMessage(color.c("&cInvalid arguments: /broadcast")));
            return false;
        }

        String messages = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        Bukkit.broadcastMessage(color.c(lang.getReplacedMessage("broadcast-format").replace("%message%", messages)));

        return true;
    }
}
