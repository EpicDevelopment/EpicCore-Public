package gg.minecrush.epiccore.Commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.DataStorage.yaml.Config;

public class EpicCoreCommand implements CommandExecutor {

    private final Lang lang;
    private final Config config;

    public EpicCoreCommand(Lang lang, Config config){
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lavel, String[] args){
        String subCommand = args[0].toLowerCase();
        if (sender instanceof Player){
            Player player = (Player) sender ;
        }

        switch (subCommand){
            case "reload":
                break;
            case "help" :
                break;
            default:
                sender.sendMessage("&cInvalid command /epiccore help");
                break;
        }
        return true;
    }

    private void handlereloadCommand(CommandSender sender, String[] args){
        if (args.length != 1){
            sender.sendMessage();
        }

        return;
    }
}
