package gg.minecrush.epiccore.Commands.gamemodes;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
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
            if (args.length != 1){
                sender.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/gamemode"));
                return false;
            }

            if (!sender.hasPermission(config.getValue("gamemode-command-permission"))){
                sender.sendMessage(lang.getReplacedMessage("no-permission"));
                return false;
            }

            Player player = (Player) sender;
            String subCommand = args[0];
            switch (subCommand) {
                case "creative":
                    gamemodeCreative(player);
                    break;
                case "c":
                    gamemodeCreative(player);
                    break;
                case "survival":
                    gamemodeSurvival(player);
                    break;
                case "s":
                    gamemodeSurvival(player);
                    break;
                case "adventure":
                    gamemodeAdventure(player);
                    break;
                case "a":
                    gamemodeAdventure(player);
                    break;
                case "spectator":
                    gamemodeSpectator(player);
                    break;
                case "sp":
                    gamemodeSpectator(player);
                    break;
            }
        } else {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
        }
        return false;
    }

    private void gamemodeAdventure(Player player){
        player.sendMessage(lang.getReplacedMessage("updated-gamemode").replace("%gamemode%", "adventure"));
        player.setGameMode(GameMode.ADVENTURE);
    }

    private void gamemodeSurvival(Player player){
        player.sendMessage(lang.getReplacedMessage("updated-gamemode").replace("%gamemode%", "survival"));
        player.setGameMode(GameMode.SURVIVAL);
    }

    private void gamemodeCreative(Player player){
        player.sendMessage(lang.getReplacedMessage("updated-gamemode").replace("%gamemode%", "creative"));
        player.setGameMode(GameMode.CREATIVE);
    }

    private void gamemodeSpectator(Player player){
        player.sendMessage(lang.getReplacedMessage("updated-gamemode").replace("%gamemode%", "spectator"));
        player.setGameMode(GameMode.SPECTATOR);
    }
}
