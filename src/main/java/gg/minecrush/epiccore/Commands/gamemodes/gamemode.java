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

public class gamemode implements CommandExecutor {

    Lang lang;
    Config config;

    public gamemode(Lang lang, Config config) {
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0 || args.length < 2){
                sender.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/gamemode"));
                return false;
            }

            if (!sender.hasPermission(config.getValue("gamemode-command-permission"))){
                sender.sendMessage(lang.getReplacedMessage("no-permission"));
                return false;
            }

            Player target = null;
            if (args.length == 2){
                target = Bukkit.getPlayer(args[1]);
                if (target == null){
                    sender.sendMessage(lang.getReplacedMessage("invalid-player"));
                    return false;
                }
            } else {
                target = (Player) sender;
            }

            Player player = (Player) sender;
            String subCommand = args[0];
            switch (subCommand) {
                case "creative":
                    gamemodeCreative(player, target);
                    break;
                case "c":
                    gamemodeCreative(player, target);
                    break;
                case "survival":
                    gamemodeSurvival(player, target);
                    break;
                case "s":
                    gamemodeSurvival(player, target);
                    break;
                case "adventure":
                    gamemodeAdventure(player, target);
                    break;
                case "a":
                    gamemodeAdventure(player, target);
                    break;
                case "spectator":
                    gamemodeSpectator(player, target);
                    break;
                case "sp":
                    gamemodeSpectator(player, target);
                    break;
            }
        } else {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
        }
        return true;
    }

    private void gamemodeAdventure(Player player, Player target){
        player.sendMessage(lang.getReplacedMessage("updated-gamemode").replace("%gamemode%", "adventure").replace("%player%", target.getName()));
        target.setGameMode(GameMode.ADVENTURE);
    }

    private void gamemodeSurvival(Player player, Player target){
        player.sendMessage(lang.getReplacedMessage("updated-gamemode").replace("%gamemode%", "survival").replace("%player%", target.getName()));
        target.setGameMode(GameMode.SURVIVAL);
    }

    private void gamemodeCreative(Player player, Player target){
        player.sendMessage(lang.getReplacedMessage("updated-gamemode").replace("%gamemode%", "creative").replace("%player%", target.getName()));
        target.setGameMode(GameMode.CREATIVE);
    }

    private void gamemodeSpectator(Player player, Player target){
        player.sendMessage(lang.getReplacedMessage("updated-gamemode").replace("%gamemode%", "spectator").replace("%player%", target.getName()));
        target.setGameMode(GameMode.SPECTATOR);
    }
}
