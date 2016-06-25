package net.inerva.lilypad.redirect;

import net.inerva.apollo.Apollo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Edwin on 1/24/2016.
 */
public class RedirectManager {

    public RedirectManager(Apollo plugin) {
        this.plugin = plugin;
        this.redirectGUIHandler = new RedirectGUIHandler(plugin, this);
    }

    private Apollo plugin;

    private RedirectGUIHandler redirectGUIHandler;

    /**
     * Redirects a player to another server if they have the permissions.
     *
     * @param player Player to redirect to the server.
     * @param server Server to redirect to.
     */
    public void redirectPlayer(Player player, String server) {
        String serverName = null;
        for (String s : plugin.getApolloServers().keySet()) {
            if (s.equalsIgnoreCase(server)) {
                serverName = s;
            }
        }

        if (serverName == null) {
            if (player.hasPermission("Apollo." + server)) {
                Thread thread = new Thread(new RedirectRunnable(server, player, plugin));
                thread.start();
            } else {
                player.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "You do not have access to this server.");
            }
            return;
        }

        if (player.hasPermission("Apollo." + serverName)) {
            Thread thread = new Thread(new RedirectRunnable(serverName, player, plugin));
            thread.start();
        } else {
            player.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "You do not have access to this server.");
        }
    }

    /**
     * Redirects a player to another server if they have the permissions.
     *
     * @param uuid   UUID of player to redirect to another server.
     * @param server Server to redirect to.
     */
    public void redirectPlayer(UUID uuid, String server) {
        String serverName = null;
        for (String s : plugin.getApolloServers().keySet()) {
            if (s.equalsIgnoreCase(server)) {
                serverName = s;
            }
        }

        Player player = Bukkit.getPlayer(uuid);
        if (serverName == null) {
            if (player.hasPermission("Apollo." + server)) {
                Thread thread = new Thread(new RedirectRunnable(server, player, plugin));
                thread.start();
            } else {
                player.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "You do not have access to this server.");
            }
            return;
        }

        if (player.hasPermission("Apollo." + serverName)) {
            Thread thread = new Thread(new RedirectRunnable(serverName, player, plugin));
            thread.start();
        } else {
            player.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "You do not have access to this server.");
        }
    }

    /**
     * Redirects a player to another server if they have the permissions/
     *
     * @param playerName Name of player to send to another server.
     * @param server     Server to redirect to.
     */
    public void redirectPlayer(String playerName, String server) {
        String serverName = null;
        for (String s : plugin.getApolloServers().keySet()) {
            if (s.equalsIgnoreCase(server)) {
                serverName = s;
            }
        }

        Player player = Bukkit.getPlayer(playerName);
        if (serverName == null) {
            if (player.hasPermission("Apollo." + server)) {
                Thread thread = new Thread(new RedirectRunnable(server, player, plugin));
                thread.start();
            } else {
                player.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "You do not have access to this server.");
            }
            return;
        }

        if (player.hasPermission("Apollo." + serverName)) {
            Thread thread = new Thread(new RedirectRunnable(serverName, player, plugin));
            thread.start();
        } else {
            player.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "You do not have access to this server.");
        }
    }

    public void sendPlayer(String playerName, String server) {
        String serverName = null;
        for (String s : plugin.getApolloServers().keySet()) {
            if (s.equalsIgnoreCase(server)) {
                System.out.println("S = " + s);
                System.out.println("Server = " + server);
                System.out.println("SERVER NAME SET");
                serverName = s;
                System.out.println("Server Name = " + serverName);
            }
        }

        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            System.out.println("PLAYER NOT NULL");
            if (serverName != null) {
                System.out.println("Sending!");
                Thread thread = new Thread(new RedirectRunnable(serverName, player, plugin));
                thread.start();
                return;
            }

            Thread thread = new Thread(new RedirectRunnable(server, player, plugin));
            thread.start();
        }
    }


    public RedirectGUIHandler getRedirectGUIHandler() {
        return redirectGUIHandler;
    }
}
