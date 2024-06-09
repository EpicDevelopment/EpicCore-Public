package lol.snowyjs.epiccore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lol.snowyjs.epiccore.DataStorage.yaml.Config;
import lol.snowyjs.epiccore.DataStorage.yaml.Lang;

import java.io.File;

public final class EpicCore extends JavaPlugin {

    private Lang langConfig;
    private Config config;

    @Override
    public void onEnable() {

        try {
            File configFiles = new File(getDataFolder(), "config.yml");
            if (!configFiles.exists()) {
                saveResource("config.yml", false);
            }
        } catch (Exception e) {
            getLogger().severe("[EpicCore] Failed to create configuration file");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        this.config = new Config(this);

        try {
            File msgFile = new File(getDataFolder(), "lang.yml");
            if (!msgFile.exists()) {
                saveResource("lang.yml", false);
            }
        } catch (Exception e) {
            getLogger().severe("[EpicCore] Failed to create lang file");
            Bukkit.getPluginManager().disablePlugin(this);
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
