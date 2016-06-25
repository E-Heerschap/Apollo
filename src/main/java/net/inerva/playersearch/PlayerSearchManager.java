package net.inerva.playersearch;

import net.inerva.apollo.Apollo;
import net.inerva.lilypad.message.MessageSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Edwin on 1/16/2016.
 */
public class PlayerSearchManager {

    public PlayerSearchManager(Apollo plugin) {
        this.plugin = plugin;
        this.messageSender = new MessageSender(plugin);
    }

    private Apollo plugin;

    private MessageSender messageSender;

    private Map<CommandSender, SearchPlayer> playersOnSearch = new HashMap<>();


    /**
     * Executes a search for a player over all the servers.
     *
     * @param commandSender Instance that sent the command.
     * @param playerName    Name of player to search for.
     */
    public void search(CommandSender commandSender, String playerName) {
        if (plugin.getApolloServers().isEmpty()) {
            if (isOnThisServer(playerName)) {
                commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.GREEN + playerName + " is on this server.");
                return;
            } else {
                commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.GREEN + playerName + " is not on this server.");
                return;
            }
        }
        if (isOnThisServer(playerName)) {
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.GREEN + playerName + " is on this server.");
            return;
        }
        System.out.println("Search manager");
        playersOnSearch.put(commandSender, new SearchPlayer(playerName));
        messageSender.sendPlayerSearch(playerName);

    }

    /**
     * This method should be called each time a server has been searched and the player was not found.
     *
     * @param playerName Name of player to be searching for
     * @param serverName Name of server that did not contain the player.
     */
    public void failedServerSearch(String playerName, String serverName) {
        for (Map.Entry<CommandSender, SearchPlayer> s : playersOnSearch.entrySet()) {
            if (s.getValue().getPlayerName().equals(playerName)) {
                s.getValue().getCheckedServers().add(serverName);
                if (s.getValue().getCheckedServers().containsAll(plugin.getApolloServers().keySet())) {
                    s.getKey().sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Player not found.");
                }
            }
        }
    }

    /**
     * This method should be called when a server has found a player.
     *
     * @param playerName Name of player found.
     * @param serverName Server that contains the player.
     */
    public void successfulServerSearch(String playerName, String serverName) {
        for (Map.Entry<CommandSender, SearchPlayer> s : playersOnSearch.entrySet()) {
            if (s.getValue().getPlayerName().equals(playerName)) {
                s.getKey().sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.GREEN + "Player is on server: " + ChatColor.BLUE + serverName);
            }
        }
    }

    /**
     * Searches this server for a player by name.
     *
     * @param playerName Name of player to search for.
     * @return True if found, false if not.
     */
    public boolean isOnThisServer(String playerName) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(playerName)) {
                return true;
            }
        }
        return false;
    }

}
