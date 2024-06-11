package gg.minecrush.epiccore.Commands;

import gg.minecrush.epiccore.DataStorage.ram.ChatManager;
import gg.minecrush.epiccore.DataStorage.yaml.Config;
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
    private ChatManager chatManager;
    private Config config;

    public MuteChatCommand(JavaPlugin plugin, Lang lang, ChatManager chatManager, Config config) {
        this.plugin = plugin;
        this.lang = lang;
        this.chatManager = chatManager;
        this.chatMuted = chatManager.isChatMuted();
        this.config = config;
    }

    public Boolean getchatMuted(){
        return this.chatMuted;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mutechat")) {
            if (sender.hasPermission(config.getValue("mutechat-command-permission"))) {
                chatManager.setChatMuted(!chatManager.isChatMuted());
                String messageKey = "";
                if (chatManager.isChatMuted()) {
                    messageKey = "chat-muted";
                } else {
                    messageKey = "chat-unmuted";
                }
                String message = lang.getReplacedMessage(messageKey);

                Bukkit.broadcastMessage(message.replace("%player%", sender.getName()));

                return true;
            } else {
                sender.sendMessage(lang.getReplacedMessage("no-permission"));
            }
        }
        return false;
    }
}