package fr.jachou.reanimatemc.listeners;

import fr.jachou.reanimatemc.ReanimateMC;
import fr.jachou.reanimatemc.managers.KOManager;
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
        if (!(event.getRightClicked() instanceof Player))
            return;

        Player target = (Player) event.getRightClicked();
        Player player = event.getPlayer();

        if (!koManager.isKO(target))
            return;

        // Vérification que le joueur est accroupi
        if (!player.isSneaking()) {
            player.sendMessage(ChatColor.RED + ReanimateMC.lang.get("not_sneaking"));
            return;
        }

        // Vérification de l'item spécial si exigé par la configuration
        boolean requireSpecial = ReanimateMC.getInstance().getConfig().getBoolean("reanimation.require_special_item", true);
        String requiredItem = ReanimateMC.getInstance().getConfig().getString("reanimation.required_item", "GOLDEN_APPLE");
        if (requireSpecial && !player.getInventory().getItemInMainHand().getType().toString().equalsIgnoreCase(requiredItem)) {
            player.sendMessage(ChatColor.RED + ReanimateMC.lang.get("special_item_required", "item", requiredItem));
            return;
        }

        player.sendMessage(ChatColor.GREEN + ReanimateMC.lang.get("revive_progress"));
        int durationTicks = ReanimateMC.getInstance().getConfig().getInt("reanimation.duration_ticks", 100);

        // Planification de la réanimation (possibilité d'afficher une barre de progression en amélioration)
        ReanimateMC.getInstance().getServer().getScheduler().runTaskLater(ReanimateMC.getInstance(), () -> {
            if (koManager.isKO(target)) {
                koManager.revive(target);
                target.sendMessage(ChatColor.GREEN + ReanimateMC.lang.get("revived_by", "player", player.getName()));
                player.sendMessage(ChatColor.GREEN + ReanimateMC.lang.get("revived_confirmation", "player", target.getName()));
            }
        }, durationTicks);
    }
}
