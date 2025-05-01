package fr.jachou.reanimatemc.listeners;

import fr.jachou.reanimatemc.ReanimateMC;
import fr.jachou.reanimatemc.managers.KOManager;
import fr.jachou.reanimatemc.managers.LootManager;
import fr.jachou.reanimatemc.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ReanimationListener implements Listener {
    private KOManager koManager;

    public ReanimationListener(KOManager koManager) {
        this.koManager = koManager;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        // Only real players, only target real players
        if (!(event.getRightClicked() instanceof Player) || !(event.getPlayer() instanceof Player))
            return;
        Player target = (Player) event.getRightClicked();
        Player player = event.getPlayer();
        if (Utils.isNPC(target) || Utils.isNPC(player)) return;

        if (!koManager.isKO(target)) return;

        if (ReanimateMC.getInstance().getConfig().getBoolean("looting.enabled", false)
                && !player.isSneaking()) {

            if (!player.hasPermission("reanimatemc.loot")) {
                player.sendMessage(ChatColor.RED + ReanimateMC.lang.get("loot_no_permission"));
                return;
            }

            player.sendMessage(ChatColor.GREEN +
                    ReanimateMC.lang.get("loot_opening", "player", target.getName()));
            LootManager.startLoot(player, target);
            return;
        }


        if (!player.isSneaking()) {
            player.sendMessage(ChatColor.RED + ReanimateMC.lang.get("not_sneaking"));
            return;
        }

        boolean requireSpecial = ReanimateMC.getInstance()
                .getConfig().getBoolean("reanimation.require_special_item", true);
        String requiredItem = ReanimateMC.getInstance()
                .getConfig().getString("reanimation.required_item", "GOLDEN_APPLE");
        if (requireSpecial
                && !player.getInventory().getItemInMainHand()
                .getType().toString().equalsIgnoreCase(requiredItem)) {
            player.sendMessage(ChatColor.RED
                    + ReanimateMC.lang.get("special_item_required", "item", requiredItem));
            return;
        }

        player.sendMessage(ChatColor.GREEN + ReanimateMC.lang.get("revive_progress"));
        int durationTicks = ReanimateMC.getInstance().getConfig()
                .getInt("reanimation.duration_ticks", 100);

        ReanimateMC.getInstance().getServer().getScheduler()
                .runTaskLater(ReanimateMC.getInstance(), () -> {
                    if (koManager.isKO(target)) {
                        koManager.revive(target);
                        target.sendMessage(ChatColor.GREEN
                                + ReanimateMC.lang.get("revived_by", "player", player.getName()));
                        player.sendMessage(ChatColor.GREEN
                                + ReanimateMC.lang.get("revived_confirmation", "player", target.getName()));
                    }
                }, durationTicks);
    }
}
