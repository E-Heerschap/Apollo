package net.inerva.command;

import net.inerva.apollo.Apollo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Edwin on 1/17/2016.
 */
public class FindPlayerCommand implements CommandExecutor {

    public FindPlayerCommand(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        System.out.println("Find player command entered");
        if (strings.length == 1) {
            plugin.getPlayerSearchManager().search(commandSender, strings[0]);
        } else {
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "You entered that wrong. /findplayer <player name>");
        }
        return true;
    }

}
