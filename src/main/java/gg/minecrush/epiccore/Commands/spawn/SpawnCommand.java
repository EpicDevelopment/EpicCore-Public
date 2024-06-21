package gg.minecrush.epiccore.Commands.spawn;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnCommand implements CommandExecutor {

    private final Lang lang;
    private final JavaPlugin plugin;
    private final Config config;

    public SpawnCommand(JavaPlugin plugin, Lang lang, Config config) {
        this.plugin = plugin;
        this.lang = lang;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location loc = player.getLocation();

                try {
                    if (plugin.getConfig().getString("spawn.location.world").isEmpty()){
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
                        if (count < 5) {
                            player.sendMessage(lang.getReplacedMessage("spawn-teleporting-in-seconds").replace("%delay%", 5 - count + ""));
                            count++;
                            if (!loc.equals(player.getLocation())) {
                                player.sendMessage(lang.getReplacedMessage("spawn-teleporting-canceled"));
                                cancel();
                            }
                        } else {
                            player.sendMessage(lang.getReplacedMessage("spawn-teleported"));

                            String worldName = plugin.getConfig().getString("spawn.location.world");
                            double x = plugin.getConfig().getDouble("spawn.location.x");
                            double y = plugin.getConfig().getDouble("spawn.location.y");
                            double z = plugin.getConfig().getDouble("spawn.location.z");
                            float pitch = (float) plugin.getConfig().getDouble("spawn.location.pitch");
                            float yaw = (float) plugin.getConfig().getDouble("spawn.location.yaw");
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
}
