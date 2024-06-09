package lol.snowyjs.epiccore.Commands;

import lol.snowyjs.epiccore.DataStorage.yaml.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ClearChatCommand implements CommandExecutor {

    private final Lang lang;
    private final JavaPlugin plugin;

    public ClearChatCommand(JavaPlugin plugin, Lang lang) {
        this.plugin = plugin;
        this.lang = lang;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearchat")) {
            int lines = 100; // number of lines to clear [FUTURE CONFIG IMPLEMENTATION]

            if (args.length > 0) {
                try {
                    lines = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(lang.getReplacedMessage("invalid-number"));
                    return false;
                }
            }

            clearChat(lines);

            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(lang.getReplacedMessage("chat-cleared"));
            } else {
                sender.sendMessage(lang.getReplacedMessage("chat-cleared"));
            }
            return true;
        }
        return false;
    }

    private void clearChat(int lines) {
        String blankLine = " ";
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (int i = 0; i < lines; i++) {
                player.sendMessage(blankLine);
            }
        }
    }
}
