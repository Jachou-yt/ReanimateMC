package fr.jachou.reanimatemc.utils;

import fr.jachou.reanimatemc.ReanimateMC;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerFreezeUtil {
    public static void freezePlayer(Player player) {
        player.setWalkSpeed(0);
        player.setFlySpeed(0);


        player.setAllowFlight(false);
        player.setInvulnerable(true);

        player.setCanPickupItems(false);
        player.setCollidable(false);
        player.setGliding(false);
        player.setGravity(false);
        player.setGlowing(true);
        player.setSilent(true);
        player.setSwimming(false);

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 99999999, 1));

        player.sendMessage(ReanimateMC.PREFIX + "You can't move... Wait for someone to revive you !");

        player.setMetadata("frozen", new FixedMetadataValue(ReanimateMC.getInstance(), true));
    }

    public static void unfreezePlayer(Player player) {
        player.setWalkSpeed(0.2f);

        player.setAllowFlight(true);
        player.setInvulnerable(false);

        player.setCanPickupItems(true);
        player.setCollidable(true);
        player.setGliding(true);
        player.setGravity(true);
        player.setGlowing(false);
        player.setSilent(false);
        player.setSwimming(true);

        player.removePotionEffect(PotionEffectType.BLINDNESS);

        player.sendMessage(ReanimateMC.PREFIX + "You have been revived !");

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
