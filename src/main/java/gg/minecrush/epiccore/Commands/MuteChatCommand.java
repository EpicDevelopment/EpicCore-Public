package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Listener.MuteChatListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MuteChatCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Lang lang;
    private boolean chatMuted;

    public MuteChatCommand(JavaPlugin plugin, Lang lang) {
        this.plugin = plugin;
        this.lang = lang;
        this.chatMuted = false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mutechat")) {
            if (sender.hasPermission("epiccore.mutechat.command")) {
                chatMuted = !chatMuted;

                String messageKey = chatMuted ? "chat-muted" : "chat-unmuted";
                String message = lang.getReplacedMessage(messageKey);

                Bukkit.broadcastMessage(message);

                MuteChatListener.setChatMuted(chatMuted);

                return true;
            } else {
                sender.sendMessage(lang.getReplacedMessage("no-permission"));
            }
        }
        return false;
    }
}