package fr.jachou.reanimatemc.utils;

import fr.jachou.reanimatemc.ReanimateMC;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerFreezeUtil {
    public static void freezePlayer(Player player) {
        // Vous pouvez utiliser des métadonnées ou une autre méthode pour marquer le joueur comme gelé
        // Par exemple, en ajoutant une métadonnée "frozen" comme nous l'avons vu précédemment

        // Ensuite, désactivez la capacité de déplacement du joueur
        player.setWalkSpeed(0); // Le joueur ne pourra pas marcher
        player.setFlySpeed(0); // Le joueur ne pourra pas voler (si applicable)

        // Désactivez d'autres actions potentielles, comme les clics de souris

        player.setAllowFlight(false);
        player.setInvulnerable(true);

        // Désactivez les clics de souris
        player.setCanPickupItems(false);
        player.setCollidable(false);
        player.setGliding(false);
        player.setGravity(false);
        player.setGlowing(true);
        player.setSilent(true);
        player.setSwimming(false);

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999999, 1));

        // Vous pouvez également afficher un message au joueur pour indiquer qu'il est gelé
        player.sendMessage("You can't move... Wait for someone to revive you !");

        player.setMetadata("frozen", new FixedMetadataValue(ReanimateMC.getInstance(), true));
    }

    public static void unfreezePlayer(Player player) {
        // Réactivez la capacité de déplacement du joueur
        player.setWalkSpeed(0.2f); // Réglez la vitesse de marche par défaut (0.2f)

        // Réactivez d'autres actions potentielles, comme les clics de souris
        player.setAllowFlight(true);
        player.setInvulnerable(false);

        // Réactivez les clics de souris
        player.setCanPickupItems(true);
        player.setCollidable(true);
        player.setGliding(true);
        player.setGravity(true);
        player.setGlowing(false);
        player.setSilent(false);
        player.setSwimming(true);

        player.removePotionEffect(PotionEffectType.BLINDNESS);

        // Affichez un message au joueur pour indiquer qu'il a été réanimé
        player.sendMessage("You have been revived !");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.removeMetadata("frozen", ReanimateMC.getInstance());
            }
        }.runTaskLater(ReanimateMC.getInstance(), 1200);
    }

    public static boolean playerIsFreezed(Player player) {
        return player.getWalkSpeed() == 0;
    }
}
