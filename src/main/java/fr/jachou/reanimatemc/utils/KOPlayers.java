package fr.jachou.reanimatemc.utils;

import fr.jachou.reanimatemc.ReanimateMC;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class KOPlayers {

    private static Map<Player, BukkitTask> reanimationTasks = new HashMap<>();
    public static void koPlayer(Player player) {
        // L'événement est déclenché lorsque le joueur meurt

        if (canBeReanimated(player)) {
            // Immobilisation du joueur et ajout de la méta-donnée "reanimated"
            PlayerFreezeUtil.freezePlayer(player);

            player.setMetadata("reanimated", new FixedMetadataValue(ReanimateMC.getInstance(), true));

            BukkitTask countdownTask = new BukkitRunnable() {
                int countdown = 60;

                @Override
                public void run() {
                    if (countdown <= 0) {
                        // Le joueur n'a pas été réanimé dans les 60 secondes, il meurt
                        player.setHealth(0); // Tuez le joueur
                        cancelTask(player);
                    } else if (!player.isDead() || !canBeReanimated(player)) {
                        // Le joueur a été réanimé ou ne peut plus être réanimé, annulez la tâche
                        cancelTask(player);
                    } else {
                        // Mettez à jour le joueur avec le temps restant
                        player.sendMessage("Temps restant : " + countdown + " secondes");
                        countdown--;
                    }
                }
            }.runTaskTimer(ReanimateMC.getInstance(), 20L, 20L); // Exécutez la tâche chaque seconde (20 ticks)

            // Stockez la tâche dans la carte pour pouvoir l'annuler plus tard
            reanimationTasks.put(player, countdownTask);
        } else {
            // Le joueur ne peut pas être réanimé
            player.removeMetadata("reanimated", ReanimateMC.getInstance());
            if (PlayerFreezeUtil.playerIsFreezed(player)) {
                PlayerFreezeUtil.unfreezePlayer(player);
            }
            return;
        }
    }

        private static boolean canBeReanimated(Player player) {
            // Vérifie si le joueur peut être réanimé
            // Le joueur a déjà été réanimé
            return !player.hasMetadata("reanimated");
        }

    private static void cancelTask(Player player) {
        BukkitTask task = reanimationTasks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }
}
