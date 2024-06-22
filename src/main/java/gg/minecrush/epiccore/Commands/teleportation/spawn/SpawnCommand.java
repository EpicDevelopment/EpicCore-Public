package gg.minecrush.epiccore.Commands.teleportation.spawn;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.DataStorage.yaml.Warps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class SpawnCommand implements CommandExecutor {

    private final Lang lang;
    private final JavaPlugin plugin;
    private final Config config;
    private final Warps warps;

    public SpawnCommand(JavaPlugin plugin, Lang lang, Config config, Warps warps) {
        this.plugin = plugin;
        this.lang = lang;
        this.config = config;
        this.warps = warps;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location loc = player.getLocation();

                try {
                    if (warps.getValue("spawn.location.world").isEmpty()){
                        player.sendMessage(lang.getReplacedMessage("spawn-not-set"));
                        return false;
                    }
                } catch (Exception e) {
                    player.sendMessage(lang.getReplacedMessage("spawn-not-set"));
                    return false;
                }

                new BukkitRunnable() {
                    int count = 0;

                    @Override
                    public void run() {
                        if (count < config.getValueInt("teleport-delay")) {
                            player.sendMessage(lang.getReplacedMessage("warp-teleporting-in-seconds").replace("%warp%", "spawn").replace("%delay%", 5 - count + ""));
                            count++;
                            if (!Objects.equals(formatWithString(loc), formatWithString(player.getLocation()))) {
                                player.sendMessage(lang.getReplacedMessage("warp-teleporting-canceled").replace("%warp%", "spawn"));
                                cancel();
                            }
                        } else {
                            player.sendMessage(lang.getReplacedMessage("warp-teleported").replace("%warp%", "spawn"));

                            String worldName = warps.getValue("spawn.location.world");
                            double x = warps.getValuedb("spawn.location.x");
                            double y = warps.getValuedb("spawn.location.y");
                            double z = warps.getValuedb("spawn.location.z");
                            float pitch = (float) warps.getValuedb("spawn.location.pitch");
                            float yaw = (float) warps.getValuedb("spawn.location.yaw");
                            World world = Bukkit.getWorld(worldName);
                            Location spawn = new Location(world, x, y, z, yaw, pitch);
                            player.teleport(spawn);
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0L, 20L);
            } else {
                sender.sendMessage("This command can only be executed by a player.");
            }
        }
        return true;
    }

    public String formatWithString(Location loc){
        String x = loc.getX() + "";
        String y = loc.getY() + "";
        String z = loc.getZ() + "";
        return x + "-" + y + "-" + z;
    }
}
