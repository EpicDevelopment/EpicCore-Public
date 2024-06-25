package gg.minecrush.epiccore.Util;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.*;

public class propermessage {

    private Config config;

    public propermessage(Config config) {
        this.config = config;
    }

    public void send(Player player, String message){
        if (config.getValueBoolean("action-bar")){
            TextComponent msg = new TextComponent(message);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, msg);
        } else {
            player.sendMessage(message);
        }
    }
}
