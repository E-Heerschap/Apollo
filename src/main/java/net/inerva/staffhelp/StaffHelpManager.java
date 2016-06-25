package net.inerva.staffhelp;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Edwin on 1/26/2016.
 */
public class StaffHelpManager {

    private List<UUID> staffToNotify = new ArrayList<>();

    /**
     * Adds player to the staff to notify
     *
     * @param playerUUID UUID of player to notify when staff are messaged.
     */
    public void addToStaffToNotify(UUID playerUUID) {
        staffToNotify.add(playerUUID);
    }

    /**
     * Removes a player UUID from the staff to notify
     *
     * @param playerUUID UUID of the player
     */
    public void removeFromStaffToNotify(UUID playerUUID) {
        if (staffToNotify.contains(playerUUID)) {
            staffToNotify.remove(playerUUID);
        }
    }

    /**
     * Sends messages to all the appropriate staff.
     *
     * @param playerName Name of the player sending the message
     * @param message    Message to send to the staff.
     */
    public void sendStaffMessage(String playerName, String message) {
        for (UUID uuid : staffToNotify) {
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Staff Chat" + ChatColor.DARK_GRAY + "] " +
                    ChatColor.GREEN + playerName + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + message);
        }
    }

    /**
     * Sends a reply back to the user from the staff.
     *
     * @param staffName  Staff member sending the reply.
     * @param playerName Playing to send the reply to.
     * @param message    Message back to the player.
     */
    @SuppressWarnings("deprecated")
    public void sendStaffReply(String staffName, String playerName, String message) {

        String messageform = "";
        messageform = ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Staff Chat" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN + staffName
                + ChatColor.DARK_GRAY + " -> " + ChatColor.GREEN;

        if (Bukkit.getPlayer(playerName) != null) {
            Player p = Bukkit.getPlayer(playerName);
            messageform = messageform + p.getName() + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE + message;
            p.sendMessage(messageform);
            for (UUID uuid : staffToNotify) {
                Bukkit.getPlayer(uuid).sendMessage(messageform);
            }
        } else {
            Bukkit.getPlayer(staffName).sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Player not found.");
        }

    }

}
