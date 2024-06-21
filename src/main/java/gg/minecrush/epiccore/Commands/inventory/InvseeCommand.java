package gg.minecrush.epiccore.Commands.inventory;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor {

    Lang lang;
    Config config;

    public InvseeCommand(Lang lang, Config config) {
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) {
            sender.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/invsee"));
            return false;
        }

        if (!sender.hasPermission(config.getValue("invsee-command-permission"))){
            sender.sendMessage(lang.getReplacedMessage("no-permission"));
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(lang.getReplacedMessage("invalid-player"));
                return false;
            }

            player.openInventory(target.getInventory());
            player.sendMessage(lang.getReplacedMessage("opening-inventory"));
        } else {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
        }
        return true;
    }
}
