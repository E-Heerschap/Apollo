package net.inerva.command;

import net.inerva.apollo.Apollo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Edwin on 1/28/2016.
 */
public class GListCommand implements CommandExecutor {

    public GListCommand(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        commandSender.sendMessage(ChatColor.BLUE + "Inerva Servers:");
        for (String server : plugin.getApolloServers().keySet()) {
            int players = plugin.getApolloServers().get(server).getPlayers().size();
            commandSender.sendMessage(ChatColor.GREEN + server + ChatColor.GOLD + " [" + Integer.toString(players) + "]");
        }

        return true;
    }
}
