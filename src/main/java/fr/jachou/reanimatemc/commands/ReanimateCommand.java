package fr.jachou.reanimatemc.commands;

import fr.jachou.reanimatemc.ReanimateMC;
import fr.jachou.reanimatemc.utils.PlayerFreezeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class ReanimateCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ReanimateMC.PREFIX + "§cUsage: /reanimate <player>");
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ReanimateMC.PREFIX + "§cPlayer not found or not online.");
        }

        assert target != null;
        if (PlayerFreezeUtil.playerIsFreezed(target)) {
            PlayerFreezeUtil.unfreezePlayer(target);
            sender.sendMessage(ReanimateMC.PREFIX + "§aThis player is no longer KO.");
        } else {
            sender.sendMessage(ReanimateMC.PREFIX + "§cThis player isn't KO.");
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
