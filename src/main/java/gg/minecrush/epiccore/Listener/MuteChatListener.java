package gg.minecrush.epiccore.Listener;

import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteChatListener implements Listener {

    private final Lang lang;
    private static boolean chatMuted = false;

    public MuteChatListener(Lang lang) {
        this.lang = lang;
    }

    public static void setChatMuted(boolean muted) {
        chatMuted = muted;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (chatMuted && !event.getPlayer().hasPermission("epiccore.mutechat.bypass")) {
            event.getPlayer().sendMessage(lang.getReplacedMessage("chat-muted-message"));
            event.setCancelled(true);
        }
    }
}
