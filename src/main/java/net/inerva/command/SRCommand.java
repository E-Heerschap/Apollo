package net.inerva.command;

import net.inerva.apollo.Apollo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Edwin on 1/26/2016.
 */
public class SRCommand implements CommandExecutor {

    public SRCommand(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("Apollo.HelpStaff")) {
            return true;
        }
        if (strings.length > 1) {
            String message = "";
            int i = 0;
            for (String sm : strings) {
                i++;
                if (i > 1) {
                    message = message + sm + " ";
                }
            }
            plugin.getStaffHelpManager().sendStaffReply(commandSender.getName(), strings[0], message);
        } else {
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry, you entered that wrong. /sr <player> <message>");
        }

        return true;
    }
}
