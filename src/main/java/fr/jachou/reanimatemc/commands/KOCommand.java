package fr.jachou.reanimatemc.commands;

import fr.jachou.reanimatemc.ReanimateMC;
import fr.jachou.reanimatemc.utils.KOPlayers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class KOCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ReanimateMC.PREFIX + "§cUsage: /ko <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        assert target != null;
        if (target.isOnline()) {
            KOPlayers.koPlayer(target);
            sender.sendMessage(ReanimateMC.PREFIX + "§a" + target.getName() + " is now KO.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                return List.of(player.getName());
            }
        }

        return null;
    }
}
