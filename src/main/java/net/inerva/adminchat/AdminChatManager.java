package net.inerva.adminchat;

import net.inerva.apollo.Apollo;
import net.inerva.lilypad.message.MessageSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Edwin on 1/14/2016.
 */
public class AdminChatManager {

    public AdminChatManager(Apollo plugin) {
        this.plugin = plugin;
        messageSender = new MessageSender(plugin);
    }

    private Apollo plugin;

    private MessageSender messageSender;

    //UUID's of all the admins
    //Key = UUID of admin
    //Value = if toggled or not.
    private HashMap<UUID, Boolean> admins = new HashMap<>();

    /**
     * Adds player to the admin chat
     *
     * @param player Player to add to the admin chat
     */
    public void addAdminToChat(Player player) {
        admins.put(player.getUniqueId(), false);
    }

    /**
     * Removes player from that admin chat
     *
     * @param player Player to remove from admin chat.
     */
    public void removeAdminFromChat(Player player) {
        if (admins.containsKey(player.getUniqueId())) {
            admins.remove(player.getUniqueId());
        }
    }

    /**
     * Sends a messages to the admin chat
     *
     * @param message Formatted messages to send to admin chat.
     */
    public void sendChatMessage(String message) {
        displayChatMessage(message);
        messageSender.sendAdminChatMessage(message);
    }

    /**
     * Displays a messages to the admins on this server.
     *
     * @param message Formatted messages to send to admin chat.
     */
    public void displayChatMessage(String message) {
        for (UUID player : admins.keySet()) {
            Bukkit.getPlayer(player).sendMessage(message);
        }
    }

    /**
     * Toggles the admin chat on or off depending on their current toggle state.
     *
     * @param player Player to toggle chat for.
     */
    public void toggleAdminChat(Player player) {
        if (admins.containsKey(player.getUniqueId())) {
            if (admins.get(player.getUniqueId()).booleanValue() == true) {
                admins.put(player.getUniqueId(), new Boolean(false));
            } else {
                admins.put(player.getUniqueId(), new Boolean(true));
            }
        }
    }

    public boolean isToggled(Player player) {
        if (admins.containsKey(player.getUniqueId())) {
            return admins.get(player.getUniqueId());
        }
        return false;
    }

}
