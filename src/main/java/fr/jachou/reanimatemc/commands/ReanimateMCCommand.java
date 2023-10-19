package fr.jachou.reanimatemc.commands;

import fr.jachou.reanimatemc.ReanimateMC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ReanimateMCCommand implements CommandExecutor, TabCompleter {

    private final FileConfiguration reanimateConfig = ReanimateMC.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§7[§cReanimateMC§7] §r§cUsage: /reanimatemc <command>");
        }

        if (args[0].equalsIgnoreCase("plugin")) {
            if (args[1].equalsIgnoreCase("desactivate")) {
                sender.sendMessage("§7[§cReanimateMC§7] §r§cPlugin desactivated.");
                if (!reanimateConfig.getBoolean("active")) {
                    sender.sendMessage("§7[§cReanimateMC§7] §r§cPlugin is already desactivated.");
                } else {
                    reanimateConfig.set("active", false);
                    ReanimateMC.getInstance().saveConfig();
                }
            } else if (args[1].equalsIgnoreCase("activate")) {
                sender.sendMessage("§7[§cReanimateMC§7] §r§aPlugin activated.");
                if (reanimateConfig.getBoolean("active")) {
                    sender.sendMessage("§7[§cReanimateMC§7] §r§cPlugin is already activated.");
                } else {
                    reanimateConfig.set("active", true);
                    ReanimateMC.getInstance().saveConfig();
                }
            } else {
                sender.sendMessage("§7[§cReanimateMC§7] §r§cUsage: /reanimatemc plugin <activate/desactivate>");
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            return List.of("plugin");
        } else if (args.length == 1) {
            ArrayList<String> commands = new ArrayList<>();
            commands.add("activate");
            commands.add("desactivate");
            return commands;
        }

        return null;
    }
}
