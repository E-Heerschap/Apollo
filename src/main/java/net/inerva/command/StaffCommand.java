package net.inerva.command;

import net.inerva.apollo.Apollo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Edwin on 1/26/2016.
 */
public class StaffCommand implements CommandExecutor {

    public StaffCommand(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length > 0) {
            String message = "";
            for (String sm : strings) {
                message = message + sm + " ";
            }
            plugin.getStaffHelpManager().sendStaffMessage(commandSender.getName(), message);
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.GREEN + "Your message has been sent to staff.");
        } else {
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry! You have entered that wrong. Enter it as such: /Staff Your message here");
        }

        return true;
    }
}
