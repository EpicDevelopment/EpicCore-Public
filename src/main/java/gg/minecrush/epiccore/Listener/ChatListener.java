package gg.minecrush.epiccore.Listener;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

import gg.minecrush.epiccore.DataStorage.yaml.Lang;

public class ChatListener implements Listener {

    private final Lang lang;

    public ChatListener(Lang lang){
        this.lang = lang;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        String format = lang.getReplacedMessage("chat-format");

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            format = PlaceholderAPI.setPlaceholders(player, format);
        }
        e.setFormat(
                format
        );
    }


}
