package gg.minecrush.epiccore.Commands.fly;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.NumberFormatting;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlySpeedCommand implements CommandExecutor {

    Config config;
    Lang lang;

    public FlySpeedCommand(Config config, Lang lang){
        this.config = config;
        this.lang = lang;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/flyspeed"));
            return false;
        }

        Player target = null;
        if (args.length == 1){
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayer(args[0]);
        }

        if (!sender.hasPermission(config.getValue("fly-command-permission"))) {
            sender.sendMessage(lang.getReplacedMessage("no-permission"));
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
            return false;
        }
        try {

            if (target == null){
                sender.sendMessage(lang.getReplacedMessage("invalid-player"));
                return false;
            }

            float speed = 0F;
            if (args.length == 1){
                speed = Float.parseFloat(args[0]);
            } else {
                speed = Float.parseFloat(args[1]);
            }

            if (speed < 0.0F || speed > 10.0F) {
                sender.sendMessage(lang.getReplacedMessage("invalid-speed"));
                return false;
            }

            Player player = (Player) sender;
            if (player.isFlying()){
                player.setFlySpeed(speed/10F);
                player.sendMessage(lang.getReplacedMessage("updated-speed").replace("%type%", "flight").replace("%speed%", speed + ""));
            } else {
                player.setWalkSpeed(speed/10F);
                player.sendMessage(lang.getReplacedMessage("updated-speed").replace("%type%", "walk").replace("%speed%", speed + ""));
            }
        } catch (NumberFormatException e){
            sender.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/flyspeed"));
        }

        return true;
    }
}
