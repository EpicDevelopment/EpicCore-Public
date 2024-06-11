package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscordCommand implements CommandExecutor {

    private final Lang lang;
    private final JavaPlugin plugin;
    private final Config config;

    public DiscordCommand(JavaPlugin plugin, Lang lang, Config config) {
        this.plugin = plugin;
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("discord")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (player.hasPermission(config.getValue("discord-command-permission"))) {
                    player.sendMessage(lang.getReplacedMessage("discord-link"));
                } else {
                    player.sendMessage(lang.getReplacedMessage("no-permission"));
                }
            } else {
                sender.sendMessage("This command can only be executed by a player.");
            }
            return true;
        }
        return false;
    }
}
