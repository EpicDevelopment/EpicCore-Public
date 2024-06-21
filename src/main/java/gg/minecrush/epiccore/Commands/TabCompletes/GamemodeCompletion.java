package gg.minecrush.epiccore.Commands.TabCompletes;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class GamemodeCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("creative");
            completions.add("survival");
            completions.add("spectator");
            completions.add("adventure");

        }
        return completions;
    }
}
