package fr.jachou.reanimatemc.events;

import fr.jachou.reanimatemc.ReanimateMC;
import fr.jachou.reanimatemc.utils.PlayerFreezeUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class PlayerEvents implements Listener {

    private final Map<Player, BukkitTask> reanimateTaks = new HashMap<>();
    private static PlayerEvents instance;



    @EventHandler
    public void onPlayerRightClickEvent(PlayerInteractEvent event) {
        // L'événement est déclenché lorsque le joueur clique avec le bouton droit de la souris

        Player player = event.getPlayer();

        if (PlayerFreezeUtil.playerIsFreezed(player)) {
            event.setCancelled(true); // Annule l'événement pour empêcher le joueur de cliquer sur des objets
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent e) {
        Player player = e.getEntity().getPlayer();

        assert player != null;
        if (player.hasMetadata("reanimated")) {
            player.removeMetadata("reanimated", ReanimateMC.getInstance());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player clicker = event.getPlayer();
        Player target = (Player) event.getRightClicked();

        if (PlayerFreezeUtil.playerIsFreezed(target) && canReanimate(clicker, target)) {
            Location clickerLocation = clicker.getLocation();
            Location targetLocation = target.getLocation();

            // Vérifiez si le joueur est suffisamment proche du joueur KO
            if (clickerLocation.distance(targetLocation) <= 5) {
                clicker.sendMessage(target.getName() + " is being reanimated...");
                target.sendMessage("You are being reanimated by " + clicker.getName() + "...");
                // Créez une tâche planifiée pour la réanimation après 5 secondes
                BukkitTask reanimateTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (PlayerFreezeUtil.playerIsFreezed(target)) {
                            // Réanimez le joueur KO
                            target.setHealth(1.0);
                            target.setMetadata("reanimated", new FixedMetadataValue(ReanimateMC.getInstance(), true));

                            reanimateTaks.remove(clicker); // Retirez la tâche de la carte

                            PlayerFreezeUtil.unfreezePlayer(target);

                            target.sendMessage("You have been reanimated by " + clicker.getName() + " !");
                            clicker.sendMessage("You have reanimated " + target.getName() + " !");
                        }
                    }
                }.runTaskLater(ReanimateMC.getInstance(), 100); // Exécutez la tâche après 5 secondes (100 ticks)

                // Enregistrez la tâche dans la carte
                reanimateTaks.put(clicker, reanimateTask);
            }
        }
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (PlayerFreezeUtil.playerIsFreezed(player)) {
            event.setCancelled(true);
            player.sendMessage("You can't move... Wait for someone to revive you !");
        }
    }

    private boolean canReanimate(Player clicker, Player target) {
        return true;
    }

    public static PlayerEvents getInstance() {
        return instance;
    }

    public Map<Player, BukkitTask> getReanimateTaks() {
        return reanimateTaks;
    }
}
