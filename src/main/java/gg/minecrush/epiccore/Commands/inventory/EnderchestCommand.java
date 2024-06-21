package gg.minecrush.epiccore.Commands.inventory;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EnderchestCommand implements CommandExecutor {

    Lang lang;
    Config config;

    public EnderchestCommand(Lang lang, Config config) {
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 1) {
            sender.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/ec"));
            return false;
        }

        if (!sender.hasPermission(config.getValue("enderchest-command-permission"))){
            sender.sendMessage(lang.getReplacedMessage("no-permission"));
            return false;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Player target = null;

            if (args.length == 1){
                if (!player.hasPermission(config.getValue("enderchest-other-command-permission"))){
                    sender.sendMessage(lang.getReplacedMessage("no-permission"));
                    return false;
                }
                target = Bukkit.getPlayer(args[0]);
            } else {
                target = (Player) sender;
            }

            if (target == null) {
                sender.sendMessage(lang.getReplacedMessage("invalid-player"));
                return false;
            }

            player.openInventory(target.getEnderChest());
            player.sendMessage(lang.getReplacedMessage("opening-inventory"));
        } else {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
        }
        return true;
    }
}
