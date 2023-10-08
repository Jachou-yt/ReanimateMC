package fr.jachou.reanimatemc.commands;

import fr.jachou.reanimatemc.ReanimateMC;
import fr.jachou.reanimatemc.utils.PlayerFreezeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReanimateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ReanimateMC.PREFIX + "§cUsage: /reanimate <player>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ReanimateMC.PREFIX + "§cPlayer not found or not online.");
            return false;
        }

        if (PlayerFreezeUtil.playerIsFreezed(target)) {
            PlayerFreezeUtil.unfreezePlayer(target);
            sender.sendMessage(ReanimateMC.PREFIX + "§aThis player is no longer KO.");
        } else {
            sender.sendMessage(ReanimateMC.PREFIX + "§cThis player isn't KO.");
        }

        return true;
    }
}
