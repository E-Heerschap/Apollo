package net.inerva.command;

import net.inerva.apollo.Apollo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Edwin on 1/14/2016.
 */
public class ACommand implements CommandExecutor {

    public ACommand(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("Apollo.AdminChat")) {
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "You do not have access to this command.");
            return true;
        }
        if (strings.length > 0) {

            String messageString = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Admin Chat" + ChatColor.DARK_GRAY + "] " + ChatColor.GOLD;

            if (commandSender instanceof Player) {
                messageString = messageString + ((Player) commandSender).getName() + ChatColor.DARK_GRAY + ":" + ChatColor.WHITE;
            } else {
                messageString = messageString + ChatColor.DARK_PURPLE + "Console" + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE;
            }

            for (String str : strings) {
                messageString = messageString + " " + str;
            }

            plugin.getAdminChatManager().sendChatMessage(messageString);
            return true;
        } else {
            if (commandSender instanceof Player) {
                plugin.getAdminChatManager().toggleAdminChat((Player) commandSender);
                if (plugin.getAdminChatManager().isToggled((Player) commandSender)) {
                    commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.GREEN + "You have toggled admin chat on.");
                } else {
                    commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.GREEN + "You have toggled admin chat off.");
                }
            } else {
                commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry, you entered that wrong. Example: /achat hello admins!");
            }
        }

        return true;
    }
}
