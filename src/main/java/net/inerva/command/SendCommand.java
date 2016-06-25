package net.inerva.command;

import net.inerva.apollo.Apollo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Edwin on 1/28/2016.
 */
public class SendCommand implements CommandExecutor {

    public SendCommand(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!commandSender.hasPermission("Apollo.SendPlayer")) {
            return true;
        }

        if (strings.length == 2) {
            Player p = Bukkit.getPlayer(strings[0]);
            if (p == null) {
                commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry, that player was not found.");
                return true;
            }

            plugin.getRedirectManager().sendPlayer(strings[0], strings[1]);
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.GREEN + "Attempting to send player " + p.getName() + " to server " + strings[1]);
            return true;


        } else {
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry, you entered that wrong. /send <player> <server>");
        }

        return true;
    }
}
