package gg.minecrush.epiccore.Async;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.Util.color;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class AutoAnnouncements {

    private static AutoAnnouncements instance;
    private Plugin plugin;
    private List<String> announcements;
    private Lang lang;
    private BukkitTask currentTask;
    private int autoAnnouncementsInterval;
    private Config config;

    public AutoAnnouncements(Plugin plugin, Lang lang, Config config) {
        this.plugin = plugin;
        this.lang = lang;
        this.config = config;
        loadConfig();
        loadAnnouncements();
        scheduleAutomaticReactions();
    }

    public static AutoAnnouncements getInstance(Plugin plugin, Lang lang, Config config) {
        if (instance == null) {
            instance = new AutoAnnouncements(plugin, lang, config);
        }
        return instance;
    }

    private void loadAnnouncements() {
        announcements = lang.getArrayMessages("announcements");
    }

    private void loadConfig() {
        autoAnnouncementsInterval = config.getValueInt("announcement-cooldown");
    }

    public List<String> getAnnouncements() {
        return announcements;
    }

    public synchronized void scheduleAutomaticReactions() {
        cancelCurrentTask();
        plugin.getLogger().info("Scheduling new automatic announcements task.");
        currentTask = new BukkitRunnable() {
            @Override
            public void run() {
                List<String> newAnnouncements = getAnnouncements();
                if (newAnnouncements.isEmpty()) {
                    cancelCurrentTask();
                    return;
                }
                String msg = newAnnouncements.get(new Random().nextInt(newAnnouncements.size()));
                Bukkit.broadcastMessage(color.c(msg));
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, 1.0F, 1.0F);
                }
            }
        }.runTaskTimerAsynchronously(plugin, 35 * 20, 20L * autoAnnouncementsInterval);
    }

    public synchronized void cancelCurrentTask() {
        if (currentTask != null) {
            plugin.getLogger().info("Cancelling current automatic announcements task.");
            try {
                currentTask.cancel();
                plugin.getLogger().info("Current task cancelled successfully.");
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to cancel automatic announcements task: " + e.getMessage());
            } finally {
                currentTask = null; // Ensure the reference is cleared
            }
        } else {
            plugin.getLogger().info("No automatic announcements task found to cancel.");
        }
    }

    public synchronized void reload() {
        plugin.getLogger().info("Reloading announcements and configuration.");
        loadConfig();
        loadAnnouncements();
        scheduleAutomaticReactions();
    }
}