package fr.jachou.reanimatemc.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LootManager {

    private static final Map<UUID, UUID> looters = new HashMap<>();

    public static void startLoot(Player looter, Player target) {
        Inventory inv = target.getInventory();
        looter.openInventory(inv);
        looters.put(looter.getUniqueId(), target.getUniqueId());
    }

    public static boolean isLooting(Player looter) {
        return looters.containsKey(looter.getUniqueId());
    }

    public static Player getLootTarget(Player looter) {
        UUID targetId = looters.get(looter.getUniqueId());
        return targetId == null ? null : Bukkit.getPlayer(targetId);
    }

    public static void stopLoot(Player looter) {
        looters.remove(looter.getUniqueId());
    }

}
