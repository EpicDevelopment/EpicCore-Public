package gg.minecrush.epiccore.Commands.teleportation.warps;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Lang;
import gg.minecrush.epiccore.DataStorage.yaml.Warps;
import gg.minecrush.epiccore.Util.color;
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

public class WarpsCommand implements CommandExecutor {


    private final Config config;
    private final Lang lang;
    private final Warps warps;
    private final Plugin plugin;

    public WarpsCommand(Lang lang, Warps warps, Config config, JavaPlugin plugin) {
        this.lang = lang;
        this.warps = warps;
        this.config = config;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0){
                player.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/warp help"));
                return false;
            }

            String subCommand = args[0];
            switch (subCommand) {
                default:
                    generalWarphandler(player, args);
                    break;
            }

        } else {
            sender.sendMessage(color.c("&cConsole cannot execute this command!"));
        }

        return false;
    }

    public void generalWarphandler(Player player, String[] args){
        if (args.length != 1){
            player.sendMessage(lang.getReplacedMessage("invalid-arguments").replace("%invalid-arguments%", "/warps <warp>"));
            return;
        }

        if (warps.getValue(args[0] + ".location.world").isEmpty()) {
            player.sendMessage(lang.getReplacedMessage("warp-doesnt-exist"));
            return;
        }

        if (!player.hasPermission(config.getValue("warps-command-permission" + "." + args[0]))){
            player.sendMessage(lang.getReplacedMessage("no-permission"));
            return;
        }
        Location loc = player.getLocation();
        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if (count < config.getValueInt("teleport-delay")) {
                    player.sendMessage(lang.getReplacedMessage("warp-teleporting-in-seconds").replace("%warp%", args[0]).replace("%delay%", 5 - count + ""));
                    count++;
                    if (!Objects.equals(formatWithString(loc), formatWithString(player.getLocation()))) {
                        player.sendMessage(lang.getReplacedMessage("warp-teleporting-canceled").replace("%warp%", args[0]));
                        cancel();
                    }
                } else {
                    player.sendMessage(lang.getReplacedMessage("warp-teleported").replace("%warp%", args[0]));

                    String worldName = warps.getValue(args[0] + ".location.world");
                    double x = warps.getValuedb(args[0] + ".location.x");
                    double y = warps.getValuedb(args[0] + ".location.y");
                    double z = warps.getValuedb(args[0] + ".location.z");
                    float pitch = (float) warps.getValuedb(args[0] + ".location.pitch");
                    float yaw = (float) warps.getValuedb(args[0] + ".location.yaw");
                    World world = Bukkit.getWorld(worldName);
                    Location location = new Location(world, x, y, z, yaw, pitch);
                    player.teleport(location);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public String formatWithString(Location loc){
        String x = loc.getX() + "";
        String y = loc.getY() + "";
        String z = loc.getZ() + "";
        return x + "-" + y + "-" + z;
    }

}
