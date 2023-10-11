/* LICENSE BEGIN
    * This file is part of ReanimateMC.
    * ReanimateMC is under the proprietary of Frouzie.
    * You are not allowed to redistribute it and/or modify it.
LICENSE END
 */

package fr.jachou.reanimatemc;

import fr.jachou.reanimatemc.commands.ReanimateCommand;
import fr.jachou.reanimatemc.events.PlayerEvents;
import fr.jachou.reanimatemc.utils.KOPlayers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bstats.bukkit.Metrics;

import java.util.Objects;

public final class ReanimateMC extends JavaPlugin {

    private static ReanimateMC instance;
    public static final String PREFIX = "§7[§cReanimateMC§7] §r";


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;


        registerEvents(this);
        registerCommands(this);

        // bStats
        int pluginId = 20034;
        Metrics metrics = new Metrics(this, pluginId);

        registerRunnable(new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getHealth() <= 3 && !player.hasMetadata("frozen")) {
                        KOPlayers.koPlayer(player);
                    }
                }
            }
        });

    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    private void registerEvents(Plugin plugin) {
        getServer().getPluginManager().registerEvents(new PlayerEvents(), plugin);
    }

    private void registerCommands(Plugin plugin) {
        Objects.requireNonNull(getCommand("reanimate")).setExecutor(new ReanimateCommand());
    }

    private void registerRunnable(BukkitRunnable runnable) {
        runnable.runTaskTimer(this, 0L, 20L);
    }


    public static ReanimateMC getInstance() {
        return instance;
    }
}
