package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.DataStorage.yaml.Filter;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.DataStorage.yaml.Config;

public class EpicCoreCommand implements CommandExecutor {

    private final Lang lang;
    private final Config config;
    private final Filter filter;

    public EpicCoreCommand(Lang lang, Config config, Filter filter){
        this.lang = lang;
        this.config = config;
        this.filter = filter;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lavel, String[] args){

        if (!sender.hasPermission(config.getValue("admin-command-permission"))){
            sender.sendMessage(lang.getReplacedMessage("no-permission"));
            return false;
        }

        if (args.length == 0){
            sender.sendMessage(color.c("&cInvalid command /epiccore help"));
            return false;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand){
            case "reload":
                handlereloadCommand(sender, args);
                break;
            case "help" :
                handlehelpCommand(sender);
                break;
            default:
                sender.sendMessage(color.c("&cInvalid command /epiccore help"));
                break;
        }
        return true;
    }

    private void handlereloadCommand(CommandSender sender, String[] args){
        if (args.length != 1){
            sender.sendMessage();
        }

        long startTime = System.currentTimeMillis();
        filter.reloadFile();
        lang.reloadConfig();
        config.reloadConfig();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        sender.sendMessage(lang.getReplacedMessage("reloaded").replace("%time%", Long.toString(duration)));

        return;
    }

    private void handlehelpCommand(CommandSender sender){
        sender.sendMessage(lang.getReplacedMessage("epiccore-help"));
        return;
    }
}
