package net.inerva.command;

import net.inerva.apollo.Apollo;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Edwin on 1/30/2016.
 */
public class RCommand implements CommandExecutor {

    public RCommand(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length > 0) {

            int i = 0;
            String stringMessage = "";
            for (String str : strings) {
                if (i == 0) {
                    stringMessage = stringMessage + " " + str;
                } else {
                    i++;
                }
            }

            if (commandSender instanceof Player) {
                plugin.getMessageManager().replyMessage((Player) commandSender, stringMessage);
            } else {
                plugin.getMessageManager().replyMessage(commandSender, stringMessage);
            }

        } else {
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry, you entered that wrong. /r <message>");
        }

        return true;
    }
}
