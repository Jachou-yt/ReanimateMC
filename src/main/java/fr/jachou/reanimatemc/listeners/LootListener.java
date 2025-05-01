package fr.jachou.reanimatemc.listeners;

import fr.jachou.reanimatemc.managers.LootManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LootListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player looter = (Player) event.getPlayer();
        if (LootManager.isLooting(looter)) {
            LootManager.stopLoot(looter);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player looter = event.getPlayer();
        if (LootManager.isLooting(looter)) {
            LootManager.stopLoot(looter);
        }
    }

}
