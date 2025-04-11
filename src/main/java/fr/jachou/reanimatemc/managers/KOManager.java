package fr.jachou.reanimatemc.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.jachou.reanimatemc.ReanimateMC;
import fr.jachou.reanimatemc.data.KOData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KOManager {
    private JavaPlugin plugin;
    private Map<UUID, KOData> koPlayers;

    public KOManager(JavaPlugin plugin) {
        this.plugin = plugin;
        koPlayers = new HashMap<>();
    }

    public void setKO(final Player player) {
        if (isKO(player))
            return;

        KOData data = new KOData();
        data.setKo(true);
        // Programmation de la mort naturelle après un délai (en secondes)
        long durationSeconds = plugin.getConfig().getLong("knockout.duration_seconds", 30);
        int taskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (isKO(player)) {
                player.setHealth(0);
                koPlayers.remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + ReanimateMC.lang.get("death_natural"));
            }
        }, durationSeconds * 20L);
        data.setTaskId(taskId);
        koPlayers.put(player.getUniqueId(), data);

        // Appliquer les effets : immobilisation (SLOW) et aveuglement (BLINDNESS)
        if (plugin.getConfig().getBoolean("knockout.movement_disabled", true)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false));
        }

        // Rendre le joueur KO plus visible pour les autres (effet de glow)
        player.setGlowing(true);

        player.sendMessage(ChatColor.RED + ReanimateMC.lang.get("ko_set"));
    }

    public boolean isKO(Player player) {
        return koPlayers.containsKey(player.getUniqueId());
    }

    public void revive(final Player player) {
        if (!isKO(player))
            return;
        KOData data = koPlayers.get(player.getUniqueId());
        plugin.getServer().getScheduler().cancelTask(data.getTaskId());
        koPlayers.remove(player.getUniqueId());

        // Suppression des effets d'immobilisation et d'aveuglement
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        // Désactiver l'effet de glow
        player.setGlowing(false);

        player.sendMessage(ChatColor.GREEN + ReanimateMC.lang.get("revived"));

        // Restauration des points de vie (configurables)
        double healthRestored = plugin.getConfig().getDouble("reanimation.health_restored", 4);
        player.setHealth(Math.min(player.getMaxHealth(), healthRestored));

        // Application d’effets temporaires sur le joueur réanimé
        int nauseaDuration = plugin.getConfig().getInt("effects_on_revive.nausea", 5);
        int slownessDuration = plugin.getConfig().getInt("effects_on_revive.slowness", 10);
        int resistanceDuration = plugin.getConfig().getInt("effects_on_revive.resistance", 10);

        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, nauseaDuration * 20, 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, slownessDuration * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, resistanceDuration * 20, 1));
    }

    public void execute(final Player victim) {
        if (!isKO(victim))
            return;
        KOData data = koPlayers.get(victim.getUniqueId());
        plugin.getServer().getScheduler().cancelTask(data.getTaskId());
        koPlayers.remove(victim.getUniqueId());

        victim.setHealth(0);
        victim.sendMessage(ChatColor.RED + ReanimateMC.lang.get("executed"));

        if (plugin.getConfig().getBoolean("execution.message_broadcast", true)) {
            Bukkit.broadcastMessage(ChatColor.DARK_RED + ReanimateMC.lang.get("execution_broadcast", "player", victim.getName()));
        }
    }

    public void cancelAllTasks() {
        for (KOData data : koPlayers.values()) {
            plugin.getServer().getScheduler().cancelTask(data.getTaskId());
        }
        koPlayers.clear();
    }
}

