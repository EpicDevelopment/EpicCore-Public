package gg.minecrush.epiccore.Commands.TabCompletes;

import gg.minecrush.epiccore.DataStorage.yaml.Warps;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class WarpsCompletion implements TabCompleter {

    private Warps warps;

    public WarpsCompletion(Warps warps) {
        this.warps = warps;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(warps.getWarps());
        }
        return completions;
    }
}
