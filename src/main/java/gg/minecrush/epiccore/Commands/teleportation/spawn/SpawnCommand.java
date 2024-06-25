package gg.minecrush.epiccore.Commands.teleportation.spawn;

import gg.minecrush.epiccore.DataStorage.ram.WarpManager;
import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.DataStorage.yaml.Warps;
import gg.minecrush.epiccore.Util.propermessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class SpawnCommand implements CommandExecutor {

    private final Config config;
    private final Lang lang;
    private final Warps warps;
    private final Plugin plugin;
    private final gg.minecrush.epiccore.Util.propermessage propermessage;
    private final WarpManager warpManager;

    public SpawnCommand(Lang lang, Warps warps, Config config, JavaPlugin plugin, propermessage msg, WarpManager warpManager) {
        this.lang = lang;
        this.warps = warps;
        this.config = config;
        this.plugin = plugin;
        this.propermessage = msg;
        this.warpManager = warpManager;
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

                if (warpManager.isWarping(player.getUniqueId())){
                    player.sendMessage(lang.getReplacedMessage("already-teleporting"));
                    return false;
                }

                new BukkitRunnable() {
                    int count = 0;

                    @Override
                    public void run() {
                        if (count < config.getValueInt("teleport-delay")) {
                            propermessage.send(player, lang.getReplacedMessage("warp-teleporting-in-seconds").replace("%warp%", "spawn").replace("%delay%", 5 - count + ""));
                            //player.sendMessage(lang.getReplacedMessage("warp-teleporting-in-seconds").replace("%warp%", "spawn").replace("%delay%", 5 - count + ""));
                            count++;
                            if (!Objects.equals(formatWithString(loc), formatWithString(player.getLocation()))) {
                                propermessage.send(player, lang.getReplacedMessage("warp-teleporting-canceled").replace("%warp%", "spawn"));
                                //player.sendMessage(lang.getReplacedMessage("warp-teleporting-canceled").replace("%warp%", "spawn"));
                                cancel();
                            }
                            if (!warpManager.isWarping(player.getUniqueId())){
                                propermessage.send(player, lang.getReplacedMessage("warp-teleporting-canceled").replace("%warp%", args[0]));
                                cancel();
                            }
                        } else {
                            propermessage.send(player, lang.getReplacedMessage("warp-teleported").replace("%warp%", "spawn"));
                            //player.sendMessage(lang.getReplacedMessage("warp-teleported").replace("%warp%", "spawn"));

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
