/* LICENSE BEGIN
    * This file is part of ReanimateMC.
    * ReanimateMC is under the proprietary of Frouzie.
    * You are not allowed to redistribute it and/or modify it.
LICENSE END
 */

package fr.jachou.reanimatemc;

import fr.jachou.reanimatemc.commands.KOCommand;
import fr.jachou.reanimatemc.commands.ReanimateCommand;
import fr.jachou.reanimatemc.commands.ReanimateMCCommand;
import fr.jachou.reanimatemc.events.PlayerEvents;
import fr.jachou.reanimatemc.update.VersionChecker;
import fr.jachou.reanimatemc.utils.KOPlayers;
import fr.jachou.reanimatemc.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class ReanimateMC extends JavaPlugin {

    private static ReanimateMC instance;
    public static final String PREFIX = "§7[§cReanimateMC§7] §r";
    public static final String VERSION = "alpha-1.1.0";


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        registerConfigurationFile();

        if (getConfig().getBoolean("checkUpdate")) {
            if (VersionChecker.isUpToDate(VERSION)) {
                Bukkit.getConsoleSender().sendMessage(PREFIX + "§aPlugin is up to date.");
                Bukkit.getConsoleSender().sendMessage(PREFIX + "§aLatest version: " + VersionChecker.getLatestVersion());
                Bukkit.getConsoleSender().sendMessage(PREFIX + "You can download the latest version at https://modrinth.com/plugin/reanimatemc");
            } else {
                Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "ReanimateMC running on the latest version");
            }
        }


        registerEvents(this);
        registerCommands();
        registerTabCompleter();

        // bStats
        int pluginId = 20034;
        Metrics metrics = new Metrics(this, pluginId);

        registerRunnable(new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getHealth() <= 3 && !player.hasMetadata("frozen") && getConfig().getBoolean("active")) {
                        KOPlayers.koPlayer(player);
                    }
                }
            }
        });

    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }

    private void registerEvents(Plugin plugin) {
        getServer().getPluginManager().registerEvents(new PlayerEvents(), plugin);
    }

    private void registerConfigurationFile() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("reanimate")).setExecutor(new ReanimateCommand());
        Objects.requireNonNull(getCommand("ko")).setExecutor(new KOCommand());
        Objects.requireNonNull(getCommand("reanimatemc")).setExecutor(new ReanimateMCCommand());
    }

    private void registerTabCompleter() {
        Objects.requireNonNull(getCommand("reanimate")).setTabCompleter(new ReanimateCommand());
        Objects.requireNonNull(getCommand("ko")).setTabCompleter(new KOCommand());
        Objects.requireNonNull(getCommand("reanimatemc")).setTabCompleter(new ReanimateMCCommand());
    }

    private void registerRunnable(BukkitRunnable runnable) {
        runnable.runTaskTimer(this, 0L, 20L);
    }


    public static ReanimateMC getInstance() {
        return instance;
    }
}
