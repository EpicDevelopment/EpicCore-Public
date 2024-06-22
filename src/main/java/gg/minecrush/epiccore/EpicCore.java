package gg.minecrush.epiccore;

import gg.minecrush.epiccore.API.CoreExpansion;
import gg.minecrush.epiccore.Async.AutoAnnouncements;
import gg.minecrush.epiccore.Commands.*;
import gg.minecrush.epiccore.Commands.TabCompletes.*;
import gg.minecrush.epiccore.Commands.fly.FlyCommand;
import gg.minecrush.epiccore.Commands.fly.FlySpeedCommand;
import gg.minecrush.epiccore.Commands.gamemodes.*;
import gg.minecrush.epiccore.Commands.inventory.EnderchestCommand;
import gg.minecrush.epiccore.Commands.TabCompletes.DefaultCompletion;
import gg.minecrush.epiccore.Commands.inventory.InvseeCommand;
import gg.minecrush.epiccore.Commands.teleportation.warps.WarpCommand;
import gg.minecrush.epiccore.Commands.teleportation.spawn.SetSpawnCommand;
import gg.minecrush.epiccore.Commands.teleportation.spawn.SpawnCommand;
import gg.minecrush.epiccore.Commands.teleportation.warps.WarpsCommand;
import gg.minecrush.epiccore.DataStorage.ram.ChatManager;
import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Filter;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.DataStorage.yaml.Warps;
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
    private Warps warps;


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
            File configFiles = new File(getDataFolder(), "warps.yml");
            if (!configFiles.exists()) {
                saveResource("warps.yml", false);
            }
        } catch (Exception e) {
            getLogger().severe("[EpicCore] Failed to create warps file");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        this.warps = new Warps(this);

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

        getCommand("spawn").setExecutor(new SpawnCommand(this, lang, config, warps));
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this, lang, config, warps));
        getCommand("clearchat").setExecutor(new ClearChatCommand(this, lang, config));
        getCommand("clearchat").setTabCompleter(new ClearChatCompletion());
        getCommand("discord").setExecutor(new DiscordCommand(this, lang, config));
        getCommand("epiccore").setExecutor(new EpicCoreCommand(lang, config, filter));
        getCommand("epiccore").setTabCompleter(new AdminCompletion());
        getCommand("mutechat").setExecutor(new MuteChatCommand(this, lang, chatManager, config));
        getCommand("report").setExecutor(new ReportCommand(reportGUI, config));
        getCommand("report").setTabCompleter(new ReportCompletion());
        getCommand("broadcast").setExecutor(new BroadcastCommand(lang, config));

        getCommand("gma").setExecutor(new gamemodeAdventure(lang, config));
        getCommand("gmc").setExecutor(new gamemodeCreative(lang, config));
        getCommand("gmsp").setExecutor(new gamemodeSpectator(lang, config));
        getCommand("gms").setExecutor(new gamemodeSurvival(lang, config));
        getCommand("gamemode").setExecutor(new gamemode(lang, config));
        getCommand("gamemode").setTabCompleter(new GamemodeCompletion());
        getCommand("fly").setExecutor(new FlyCommand(lang, config));
        getCommand("fly").setTabCompleter(new FlyCompletion());
        getCommand("flyspeed").setExecutor(new FlySpeedCommand(config, lang));
        getCommand("flyspeed").setTabCompleter(new FlySpeedCompletion());

        getCommand("enderchest").setExecutor(new EnderchestCommand(lang, config));
        getCommand("invsee").setExecutor(new InvseeCommand(lang, config));
        getCommand("enderchest").setTabCompleter(new DefaultCompletion());
        getCommand("invsee").setTabCompleter(new DefaultCompletion());

        getCommand("heal").setExecutor(new HealCommand(config, lang));
        getCommand("heal").setTabCompleter(new DefaultCompletion());

        getCommand("feed").setExecutor(new FeedCommand(config, lang));
        getCommand("feed").setTabCompleter(new DefaultCompletion());

        getCommand("warp").setExecutor(new WarpCommand(lang, warps, config, this));
        getCommand("warps").setExecutor(new WarpsCommand(lang, warps, config, this));
        getCommand("warps").setTabCompleter(new WarpsCompletion(warps));



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
        registerPermission(config.getValue("gma-command-permission"), "Update player gamemode", PermissionDefault.OP);
        registerPermission(config.getValue("gmc-command-permission"), "Update player gamemode", PermissionDefault.OP);
        registerPermission(config.getValue("gms-command-permission"), "Update player gamemode", PermissionDefault.OP);
        registerPermission(config.getValue("gmsp-command-permission"), "Update player gamemode", PermissionDefault.OP);
        registerPermission(config.getValue("gamemode-command-permission"), "Update player gamemode", PermissionDefault.OP);
        registerPermission(config.getValue("fly-command-permission"), "Update player flight mode", PermissionDefault.OP);
        registerPermission(config.getValue("invsee-command-permission"), "View players inventory", PermissionDefault.OP);
        registerPermission(config.getValue("enderchest-command-permission"), "View players inventory", PermissionDefault.OP);
        registerPermission(config.getValue("enderchest-other-command-permission"), "View players inventory", PermissionDefault.OP);
        registerPermission(config.getValue("heal-command-permission"), "Heal and feed yourself", PermissionDefault.OP);
        registerPermission(config.getValue("heal-other-command-permission"), "Heal and feed another player", PermissionDefault.OP);
        registerPermission(config.getValue("feed-command-permission"), "Heal and feed yourself", PermissionDefault.OP);
        registerPermission(config.getValue("feed-others-command-permission"), "Heal and feed another player", PermissionDefault.OP);
        registerPermission(config.getValue("warps-manage-command-permission"), "Heal and feed another player", PermissionDefault.OP);


        // Automatic Events Register

        if (config.getValueBoolean("automatic-announcements")) {
            this.autoAnnouncements = new AutoAnnouncements(this, lang, config);
        }
    }

    public void registerPermission(String name, String description, PermissionDefault defaultValue) {
        Permission permission = new Permission(name, description, defaultValue);
        Bukkit.getPluginManager().addPermission(permission);
    }

    public void unregisterPermission(String name, String description, PermissionDefault defaultValue){
        Permission permission = new Permission(name, description, defaultValue);
        Bukkit.getPluginManager().removePermission(permission);
    }

    @Override
    public void onDisable() {
        unregisterPermission(config.getValue("staff-permission"), "Staff commands!", PermissionDefault.OP);
        unregisterPermission(config.getValue("clearchat-permission"), "Clear the public chat!", PermissionDefault.OP);
        unregisterPermission(config.getValue("chat-muted-bypass-permission"), "Bypass muted chat", PermissionDefault.OP);
        unregisterPermission(config.getValue("admin-command-permission"), "Manage the core plugin", PermissionDefault.OP);
        unregisterPermission(config.getValue("broadcast-command-permission"), "Broadcast messages", PermissionDefault.OP);
        unregisterPermission(config.getValue("mutechat-command-permission"), "Mutechat command", PermissionDefault.OP);
        unregisterPermission(config.getValue("setspawn-command-permission"), "Update server spawn", PermissionDefault.OP);
        unregisterPermission(config.getValue("discord-command-permission"), "Find the discord!", PermissionDefault.TRUE);
        unregisterPermission(config.getValue("gma-command-permission"), "Update player gamemode", PermissionDefault.OP);
        unregisterPermission(config.getValue("gmc-command-permission"), "Update player gamemode", PermissionDefault.OP);
        unregisterPermission(config.getValue("gms-command-permission"), "Update player gamemode", PermissionDefault.OP);
        unregisterPermission(config.getValue("gmsp-command-permission"), "Update player gamemode", PermissionDefault.OP);
        unregisterPermission(config.getValue("gamemode-command-permission"), "Update player gamemode", PermissionDefault.OP);
        unregisterPermission(config.getValue("fly-command-permission"), "Update player fly mode", PermissionDefault.OP);
        unregisterPermission(config.getValue("invsee-command-permission"), "View players inventory", PermissionDefault.OP);
        unregisterPermission(config.getValue("enderchest-command-permission"), "View players inventory", PermissionDefault.OP);
        unregisterPermission(config.getValue("enderchest-other-command-permission"), "View players inventory", PermissionDefault.OP);
        unregisterPermission(config.getValue("heal-command-permission"), "Heal and feed yourself", PermissionDefault.OP);
        unregisterPermission(config.getValue("heal-other-command-permission"), "Heal and feed another player", PermissionDefault.OP);
        unregisterPermission(config.getValue("feed-command-permission"), "Heal and feed yourself", PermissionDefault.OP);
        unregisterPermission(config.getValue("feed-others-command-permission"), "Heal and feed another player", PermissionDefault.OP);
        unregisterPermission(config.getValue("warps-manage-command-permission"), "Heal and feed another player", PermissionDefault.OP);
    }
}
