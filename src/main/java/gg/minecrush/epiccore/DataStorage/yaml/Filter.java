package gg.minecrush.epiccore.DataStorage.yaml;

import gg.minecrush.epiccore.Util.color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Filter {
    private final Plugin plugin;
    private File configFile;
    private FileConfiguration config;
    private String filePath = "filtered.yml";
    private List<String> bannedWords;
    private List<Pattern> bannedPatterns;
    private static final Pattern ALLOWED_CHARACTERS_PATTERN = Pattern.compile("[a-zA-Z0-9 .,!?@#%^&*()_+=\\-\\[\\]{}|;:'\"<>,./?`~\\\\]+");


    public Filter(Plugin plugin) {
        this.plugin = plugin;
        createFile();
        loadBannedWords();
    }

    public String getFilePath() {
        return filePath;
    }

    private void createFile() {
        configFile = new File(plugin.getDataFolder(), filePath);
        if (!configFile.exists()) {
            plugin.saveResource(filePath, false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reloadFile() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), filePath);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        loadBannedWords();
    }

    public void saveFile() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBannedWords() {
        bannedWords = config.getStringList("words");
    }

    public List<String> getBannedWords() {
        return bannedWords;
    }

    public List<Pattern> getBannedPatterns() {
        return bannedPatterns;
    }

    public boolean containsOnlyAllowedCharacters(String message) {
        return ALLOWED_CHARACTERS_PATTERN.matcher(message).matches();
    }
}
