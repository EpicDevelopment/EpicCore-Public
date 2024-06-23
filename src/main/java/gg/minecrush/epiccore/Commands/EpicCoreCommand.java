package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.Async.AutoAnnouncements;
import gg.minecrush.epiccore.DataStorage.yaml.Filter;
import gg.minecrush.epiccore.EpicCore;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.DataStorage.yaml.Config;
import org.bukkit.plugin.Plugin;

public class EpicCoreCommand implements CommandExecutor {

    private Lang lang;
    private Config config;
    private Filter filter;
    private EpicCore plugin;

    public EpicCoreCommand(Lang lang, Config config, Filter filter, EpicCore plugin){
        this.lang = lang;
        this.config = config;
        this.filter = filter;
        this.plugin = plugin;
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
        plugin.reloadPlugin();
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
