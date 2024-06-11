package gg.minecrush.epiccore.Listener;

import gg.minecrush.epiccore.DataStorage.ram.ChatManager;
import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteChatListener implements Listener {

    private final Lang lang;
    private static boolean chatMuted = false;
    private ChatManager chatManager;
    private Config config;

    public MuteChatListener(Lang lang, ChatManager chatManager, Config config) {
        this.lang = lang;
        this.chatManager = chatManager;
        this.config = config;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (chatManager.isChatMuted() && !event.getPlayer().hasPermission(config.getValue("chat-muted-bypass-permission"))) {
            event.getPlayer().sendMessage(lang.getReplacedMessage("chat-muted-message"));
            event.setCancelled(true);
        }
    }
}
