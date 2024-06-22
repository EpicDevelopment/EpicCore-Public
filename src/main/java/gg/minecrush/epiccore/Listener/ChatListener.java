package gg.minecrush.epiccore.Listener;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Filter;
import gg.minecrush.epiccore.Util.color;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.entity.Player;

import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private final Lang lang;
    private final Filter filter;
    private final Plugin core;
    private final Config config;

    public ChatListener(Lang lang, Filter filter, Plugin core, Config config) {
        this.lang = lang;
        this.filter = filter;
        this.core = core;
        this.config = config;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String message = e.getMessage();

        if (config.getValueBoolean("format-chat")) {
            String format = lang.getMessages("chat-format")
                    .replace("{PLAYER}", player.getName())
                    .replace("{MESSAGE}", message);

            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                format = PlaceholderAPI.setPlaceholders(player, format);
            }
            format = color.c(convertChatFormat(format));

            e.setFormat(format);
        }

        for (String bannedWord : filter.getBannedWords()) {
            if (message.toLowerCase().contains(bannedWord.toLowerCase())) {
                String message2 = e.getMessage().replaceAll("(?i)" + Pattern.quote(bannedWord), color.c("&4&n" + bannedWord.toLowerCase()) + "&b");
                String msg = lang.getReplacedMessage("filter-report")
                        .replace("%message%", message2)
                        .replace("%player%", e.getPlayer().getDisplayName());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.hasPermission(config.getValue("staff-permission"))) {
                                player.sendMessage(color.c(msg));
                            }
                        }
                    }
                }.runTaskAsynchronously(core);

                e.getRecipients().clear();
                e.getRecipients().add(player);
                return;
            }
        }


        if (!filter.containsOnlyAllowedCharacters(message)) {
            e.setCancelled(true);
            player.sendMessage(lang.getMessages("invalid-characters"));
        }
    }
    public static String convertChatFormat(String format) {
        format = format.replace("%", "%%");
        Pattern pattern = Pattern.compile("\\{(\\w+)}");
        Matcher matcher = pattern.matcher(format);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(sb, "%$1\\$s");
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

}