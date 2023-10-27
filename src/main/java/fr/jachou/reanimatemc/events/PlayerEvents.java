package fr.jachou.reanimatemc.events;

import fr.jachou.reanimatemc.ReanimateMC;
import fr.jachou.reanimatemc.update.VersionChecker;
import fr.jachou.reanimatemc.utils.PlayerFreezeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
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
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (ReanimateMC.getInstance().getConfig().getBoolean("messageUpdate")) {
            if (player.hasPermission("*") || player.isOp()) {
                if (VersionChecker.isUpToDate(ReanimateMC.VERSION)) {
                    player.sendMessage(ReanimateMC.PREFIX + "Plugin is up to date." + " Latest version: " + VersionChecker.getLatestVersion() + ". You can download the latest version at https://modrinth.com/plugin/reanimatemc");
                    player.sendMessage(ReanimateMC.PREFIX + ChatColor.GRAY + "You can deactivate this message in the config.yml file.");
                } else {
                    player.sendMessage(ReanimateMC.PREFIX + ChatColor.GREEN + "ReanimateMC running on the latest version");
                    player.sendMessage(ReanimateMC.PREFIX + ChatColor.GRAY + "You can deactivate this message in the config.yml file" + ChatColor.ITALIC +  " (messageUpdate).");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRightClickEvent(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (PlayerFreezeUtil.playerIsFreezed(player)) {
            event.setCancelled(true);
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

            if (clickerLocation.distance(targetLocation) <= 5) {
                if (!target.hasMetadata("inReanimation")) {
                    clicker.sendMessage(ReanimateMC.PREFIX + target.getName() + " is being reanimated...");
                    target.sendMessage(ReanimateMC.PREFIX + "You are being reanimated by " + clicker.getName() + "...");
                    target.setMetadata("inReanimation", new FixedMetadataValue(ReanimateMC.getInstance(), true));
                } else {
                    clicker.sendMessage(ReanimateMC.PREFIX + "This player is already being reanimated !");
                    return;
                }
                BukkitTask reanimateTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (PlayerFreezeUtil.playerIsFreezed(target)) {
                            target.setHealth(1.0);
                            target.removeMetadata("inReanimation", ReanimateMC.getInstance());

                            reanimateTaks.remove(clicker);

                            PlayerFreezeUtil.unfreezePlayer(target);

                            clicker.sendMessage(ReanimateMC.PREFIX + "You have reanimated " + target.getName() + " !");
                        }
                    }
                }.runTaskLater(ReanimateMC.getInstance(), 100);

                reanimateTaks.put(clicker, reanimateTask);
            }
        }
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (PlayerFreezeUtil.playerIsFreezed(player)) {
            event.setCancelled(true);
            player.sendMessage(ReanimateMC.PREFIX + "You can't move... Wait for someone to revive you !");
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
