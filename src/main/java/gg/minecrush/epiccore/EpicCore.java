package gg.minecrush.epiccore;

import gg.minecrush.epiccore.API.CoreExpansion;
import gg.minecrush.epiccore.Async.AutoAnnouncements;
import gg.minecrush.epiccore.Commands.*;
import gg.minecrush.epiccore.Commands.TabCompletes.AdminCompletion;
import gg.minecrush.epiccore.DataStorage.ram.ChatManager;
import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Filter;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.GUI.ReportGUI;
import gg.minecrush.epiccore.Listener.ChatListener;
import gg.minecrush.epiccore.Listener.MuteChatListener;
import gg.minecrush.epiccore.Listener.ReportListener;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class EpicCore extends JavaPlugin {

    private Lang lang;
    private Config config;
    private Filter filter;
    private ChatManager chatManager;
    private AutoAnnouncements autoAnnouncements;


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
        this.lang = new Lang(this, config);

        try {
            File msgFile = new File(getDataFolder(), "filtered.yml");
            if (!msgFile.exists()) {
                saveResource("filtered.yml", false);
            }
        } catch (Exception e) {
            getLogger().severe("[EpicCore] Failed to create filter file");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        this.filter = new Filter(this);

        chatManager = new ChatManager();

        ReportListener reportListener = new ReportListener();
        ReportGUI reportGUI = new ReportGUI(reportListener, config, lang);

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
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this, lang, config));
        getCommand("clearchat").setExecutor(new ClearChatCommand(this, lang, config));
        getCommand("discord").setExecutor(new DiscordCommand(this, lang, config));
        getCommand("epiccore").setExecutor(new EpicCoreCommand(lang, config, filter));
        getCommand("epiccore").setTabCompleter(new AdminCompletion());
        getCommand("mutechat").setExecutor(new MuteChatCommand(this, lang, chatManager, config));
        getCommand("report").setExecutor(new ReportCommand(reportGUI, config));
        getCommand("broadcast").setExecutor(new BroadcastCommand(lang, config));

        // Event Registers

        getServer().getPluginManager().registerEvents(new MuteChatListener(lang, chatManager, config), this);
        getServer().getPluginManager().registerEvents(reportListener, this);
        getServer().getPluginManager().registerEvents(new ChatListener(lang, filter, this, config), this);

        // Permission Register
        registerPermission(config.getValue("staff-permission"), "Staff commands!", PermissionDefault.OP);
        registerPermission(config.getValue("clearchat-permission"), "Clear the public chat!", PermissionDefault.OP);
        registerPermission(config.getValue("chat-muted-bypass-permission"), "Bypass muted chat", PermissionDefault.OP);
        registerPermission(config.getValue("admin-command-permission"), "Manage the core plugin", PermissionDefault.OP);
        registerPermission(config.getValue("broadcast-command-permission"), "Broadcast messages", PermissionDefault.OP);
        registerPermission(config.getValue("mutechat-command-permission"), "Mutechat command", PermissionDefault.OP);
        registerPermission(config.getValue("setspawn-command-permission"), "Update server spawn", PermissionDefault.OP);
        registerPermission(config.getValue("discord-command-permission"), "Find the discord!", PermissionDefault.TRUE);

        // Automatic Events Register

        if (config.getValueBoolean("automatic-announcements")) {
            this.autoAnnouncements = new AutoAnnouncements(this, lang, config);
        }
    }

    public void registerPermission(String name, String description, PermissionDefault defaultValue) {
        Permission permission = new Permission(name, description, defaultValue);
        Bukkit.getPluginManager().addPermission(permission);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
