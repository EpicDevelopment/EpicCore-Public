package gg.minecrush.epiccore.GUI;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Listener.ReportListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ReportGUI {

    private final ReportListener reportListener;

    private final Config config;

    private final Lang lang;

    public ReportGUI(ReportListener reportListener, Config config, Lang lang) {
        this.reportListener = reportListener;
        this.config = config;
        this.lang = lang;
    }

    public void openReportGUI(Player player, String reportedPlayer) {
        Inventory gui = Bukkit.createInventory(null, 9, "Report " + reportedPlayer);

        gui.setItem(0, createGuiItem(Material.PAPER, "Cheating"));
        gui.setItem(1, createGuiItem(Material.PAPER, "Bug Abuse"));
        gui.setItem(2, createGuiItem(Material.PAPER, "Racism"));
        gui.setItem(3, createGuiItem(Material.PAPER, "Spam"));
        gui.setItem(4, createGuiItem(Material.PAPER, "Advertisement"));
        gui.setItem(5, createGuiItem(Material.PAPER, "Filter Bypass"));
        gui.setItem(6, createGuiItem(Material.PAPER, "Inappropriate Content"));
        gui.setItem(7, createGuiItem(Material.PAPER, "Doxing/DDOS"));
        gui.setItem(8, createGuiItem(Material.PAPER, "Other"));

        player.openInventory(gui);

        // Register the reported player with the listener
        reportListener.setReportedPlayer(player, reportedPlayer,config, lang);
    }

    private ItemStack createGuiItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}
