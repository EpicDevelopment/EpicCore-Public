package lol.snowyjs.epiccore.Commands;

import lol.snowyjs.epiccore.DataStorage.yaml.Lang;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnCommand implements CommandExecutor {

    private final Lang lang;
    private final JavaPlugin plugin;

    public SpawnCommand(JavaPlugin plugin, Lang lang) {
        this.plugin = plugin;
        this.lang = lang;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                new BukkitRunnable() {
                    int count = 0;

                    @Override
                    public void run() {
                        if (count < 5) {
                            player.sendMessage(lang.getReplacedMessage("spawn-teleporting-in-seconds"));
                            count++;
                        } else {
                            player.sendMessage(lang.getReplacedMessage("spawn-teleported"));
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
