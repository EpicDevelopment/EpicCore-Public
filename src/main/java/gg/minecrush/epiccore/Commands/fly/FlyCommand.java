package gg.minecrush.epiccore.Commands.fly;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    Lang lang;
    Config config;
    public FlyCommand(Lang lang, Config config){
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            if (!sender.hasPermission(config.getValue("fly-command-permission"))){
                sender.sendMessage(lang.getReplacedMessage("no-permission"));
                return false;
            }

            Player player = (Player) sender;
            if (player.hasPermission(lang.getReplacedMessage("fly-command-permission"))) {
                if (args.length == 0) {
                    if (player.getAllowFlight() == false) {
                        player.setFlying(true);
                        player.sendMessage(lang.getReplacedMessage("flight-enabled-self").replace("%player%", player.getName()));

                    } else {
                        player.setFlying(false);
                        player.sendMessage(lang.getReplacedMessage("flight-disabled-self").replace("%player%", player.getName()));
                    }

                }else{
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target != null) {
                        if (target.getAllowFlight() == false) {
                            target.setFlying(true);
                            player.sendMessage(lang.getReplacedMessage("flight-enabled-other")
                                    .replace("%target%", target.getName())
                                    .replace("%player%", player.getName()));

                        }else{
                            
                        }
                    } else {
                        player.sendMessage(lang.getReplacedMessage("player-doesnt-exist"));
                    }
                }

            }
        }else{
            sender.sendMessage("Console cannot execute this command!");
        }
        return false;
    }

}
