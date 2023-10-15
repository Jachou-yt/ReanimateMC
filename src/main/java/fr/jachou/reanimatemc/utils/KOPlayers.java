package fr.jachou.reanimatemc.utils;

import fr.jachou.reanimatemc.ReanimateMC;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class KOPlayers {

    private static final Map<Player, BukkitTask> reanimationTasks = new HashMap<>();
    public static void koPlayer(Player player) {
        PlayerFreezeUtil.freezePlayer(player);


        BukkitTask countdownTask = new BukkitRunnable() {
            int countdown = 60;

            @Override
            public void run() {
                if (countdown <= 0) {
                    player.setHealth(0);
                    cancelTask(player);
                } else if (!player.isDead()) {
                    cancelTask(player);
                } else {
                    countdown--;
                }
            }
        }.runTaskTimer(ReanimateMC.getInstance(), 20L, 20L);

        reanimationTasks.put(player, countdownTask);
    }



    private static void cancelTask(Player player) {
        BukkitTask task = reanimationTasks.remove(player);
        if (task != null) {
            task.cancel();
        }
    }
}
