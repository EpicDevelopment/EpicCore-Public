package gg.minecrush.epiccore.Listener;

import gg.minecrush.epiccore.DataStorage.yaml.Config;
import gg.minecrush.epiccore.DataStorage.yaml.Warps;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class JoinListener implements Listener {

    private Config config;
    private Warps warps;
    private Plugin plugin;

    public JoinListener(Config config, Warps warps, Plugin plugin) {
        this.config = config;
        this.warps = warps;
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (config.getValueBoolean("teleport-join")){
            boolean hasJoined = player.hasPlayedBefore();
            if (config.getValueBoolean("first-join")){
                if (hasJoined){
                    return;
                }
            }

            if (!warps.getWarps().contains(config.getValue("teleport-warp"))){
                plugin.getLogger().severe("The warp " + config.getValue("teleport-warp") + " does not exist.");
                return;
            }

            try {
                String worldName = warps.getValue(config.getValue("teleport-warp") + ".location.world");
                double x = warps.getValuedb(config.getValue("teleport-warp") + ".location.x");
                double y = warps.getValuedb(config.getValue("teleport-warp") + ".location.y");
                double z = warps.getValuedb(config.getValue("teleport-warp") + ".location.z");
                float pitch = (float) warps.getValuedb(config.getValue("teleport-warp") + ".location.pitch");
                float yaw = (float) warps.getValuedb(config.getValue("teleport-warp") + ".location.yaw");
                World world = Bukkit.getWorld(worldName);
                Location location = new Location(world, x, y, z, yaw, pitch);
                player.teleport(location);
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
    }
}
