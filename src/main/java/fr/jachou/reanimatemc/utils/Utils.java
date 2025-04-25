package fr.jachou.reanimatemc.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class Utils {
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(message)
        );
    }

    public static boolean isNPC(Player player) {

        if (player.hasMetadata("NPC")) {
            return true;
        }
        return false;
    }
}
