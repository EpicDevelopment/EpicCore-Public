package gg.minecrush.epiccore.API;

import gg.minecrush.epiccore.EpicCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;


public class CoreExpansion extends  PlaceholderExpansion{
    private final EpicCore plugin;

    public CoreExpansion(EpicCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "awel & snowyjs";
    }
    @Override
    public String getIdentifier() {
        return "epiccore";
    }
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    @Override
    public boolean persist() {
        return true;
    }
}
