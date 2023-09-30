/* LICENSE BEGIN
    * This file is part of ReanimateMC.
    * ReanimateMC is under the proprietary of Frouzie.
    * You are not allowed to redistribute it and/or modify it.
LICENSE END
 */

package fr.jachou.reanimatemc;

import fr.jachou.reanimatemc.events.PlayerEvents;
import fr.jachou.reanimatemc.utils.KOPlayers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class ReanimateMC extends JavaPlugin {

    private static ReanimateMC instance;
    private static final String PREFIX = "§7[§cReanimateMC§7] §r";


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        registerEvents(this);

        new BukkitRunnable() {
            @Override
            public void run() {
                // Vérifiez chaque joueur en jeu
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Vérifiez si le joueur a un cœur ou moins (20 = plein cœur)
                    if (player.getHealth() <= 3 && !player.hasMetadata("frozen")) {
                        // Faites quelque chose ici, par exemple envoyer un message au joueur :
                        KOPlayers.koPlayer(player);
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    private void registerEvents(Plugin plugin) {
        getServer().getPluginManager().registerEvents(new PlayerEvents(), plugin);
    }

    public static ReanimateMC getInstance() {
        return instance;
    }
}
