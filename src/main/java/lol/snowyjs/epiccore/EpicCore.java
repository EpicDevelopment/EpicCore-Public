package lol.snowyjs.epiccore;

import lol.snowyjs.epiccore.Commands.ClearChatCommand;
import lol.snowyjs.epiccore.Commands.DiscordCommand;
import lol.snowyjs.epiccore.Commands.SetSpawnCommand;
import lol.snowyjs.epiccore.Commands.SpawnCommand;
import lol.snowyjs.epiccore.Commands.MuteChatCommand;
import lol.snowyjs.epiccore.Listener.MuteChatListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lol.snowyjs.epiccore.DataStorage.yaml.Config;
import lol.snowyjs.epiccore.DataStorage.yaml.Lang;

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

        // Command Registers

        getCommand("spawn").setExecutor(new SpawnCommand(this, lang));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this, lang));
        getCommand("clearchat").setExecutor(new ClearChatCommand(this, lang));
        getCommand("discord").setExecutor(new DiscordCommand(this, lang));
        getCommand("mutechat").setExecutor(new MuteChatCommand(this, lang));

        // Event Registers
        getServer().getPluginManager().registerEvents(new MuteChatListener(lang), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
