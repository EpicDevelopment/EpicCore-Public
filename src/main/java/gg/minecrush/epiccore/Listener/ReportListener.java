package gg.minecrush.epiccore.Listener;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import gg.minecrush.epiccore.DataStorage.yaml.Lang;

import java.util.HashMap;
import java.util.Map;

public class ReportListener implements Listener {

    Lang lang;

    Config config;

    private final Map<Player, String> reportMap = new HashMap<>();

    public void setReportedPlayer(Player player, String reportedPlayer, Config config, Lang lang) {
        reportMap.put(player, reportedPlayer);
        this.lang = lang;
        this.config = config;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getView().getTopInventory();

        if (inventory != null && event.getView().getTitle().startsWith("Report ")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();

            if (item != null && item.hasItemMeta()) {
                String reason = item.getItemMeta().getDisplayName();
                String reportedPlayer = reportMap.get(player);

                if (reportedPlayer != null) {
                    player.sendMessage(lang.getReplacedMessage("report-reported-success").replace("%player%", reportedPlayer).replace("%reason%", reason));
                    String broadcastMessage = lang.getReplacedMessage("report-broadcast");
                    for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                        if (onlinePlayer.hasPermission(config.getValue("staff-permission"))) {
                            onlinePlayer.sendMessage(broadcastMessage.replace("%player%", reportedPlayer).replace("%reporter%", player.getName()).replace("%reason%", reason));
                        }
                    }
                    //player.sendMessage(lang.getReplacedMessage("report-proof-question"));
                    reportMap.remove(player);
                    player.closeInventory();
                }
            }
        }
    }
}
