package gg.minecrush.epiccore.Commands.teleportation.spawn;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.DataStorage.yaml.Warps;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetSpawnCommand implements CommandExecutor {

    private final Lang lang;
    private final JavaPlugin plugin;
    private final Config config;
    private final Warps warps;

    public SetSpawnCommand(JavaPlugin plugin, Lang lang, Config config, Warps warps) {
        this.plugin = plugin;
        this.lang = lang;
        this.config = config;
        this.warps = warps;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (player.hasPermission(config.getValue("setspawn-command-permission"))) {
                    Location location = player.getLocation();

                    warps.setValue("spawn.location.world", location.getWorld().getName());
                    warps.setDoubble("spawn.location.x", location.getX());
                    warps.setDoubble("spawn.location.y", location.getY());
                    warps.setDoubble("spawn.location.z", location.getZ());
                    warps.setFloat("spawn.location.pitch", location.getPitch());
                    warps.setFloat("spawn.location.yaw", location.getYaw());
                    player.sendMessage(lang.getReplacedMessage("spawn-set"));
                } else {
                    player.sendMessage(lang.getReplacedMessage("no-permission"));
                }
            } else {
                sender.sendMessage("This command can only be executed by a player.");
            }
        }
        return true;
    }
}
