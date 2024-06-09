package gg.minecrush.epiccore;

import gg.minecrush.epiccore.API.CoreExpansion;
import gg.minecrush.epiccore.Commands.*;
import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.GUI.ReportGUI;
import gg.minecrush.epiccore.Listener.MuteChatListener;
import gg.minecrush.epiccore.Listener.ReportListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class EpicCore extends JavaPlugin {

    private Lang lang;
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

        ReportListener reportListener = new ReportListener();
        ReportGUI reportGUI = new ReportGUI(reportListener);

        // Check for PAPI

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new CoreExpansion(this).register();
        } else {
            getLogger().severe("-------------------------------------");
            getLogger().severe("While looking for PlaceholderAPI");
            getLogger().severe("");
            getLogger().severe("Could not find PlaceholderAPI! This plugin is required.");
            getLogger().severe("-------------------------------------");
        }


        // Command Registers

        getCommand("spawn").setExecutor(new SpawnCommand(this, lang));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this, lang));
        getCommand("clearchat").setExecutor(new ClearChatCommand(this, lang));
        getCommand("discord").setExecutor(new DiscordCommand(this, lang));
        // getCommand("mutechat").setExecutor(new MuteChatCommand(this, lang));
        getCommand("report").setExecutor(new ReportCommand(reportGUI));

        // Event Registers

        getServer().getPluginManager().registerEvents(new MuteChatListener(lang), this);
        getServer().getPluginManager().registerEvents(reportListener, this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
