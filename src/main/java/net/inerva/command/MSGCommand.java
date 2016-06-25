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
public class MSGCommand implements CommandExecutor {

    public MSGCommand(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length > 1) {

            String msg = "";
            int i = 0;
            for (String str : strings) {
                if (i == 1) {
                    msg = msg + " " + str;
                } else {
                    i++;
                }
            }
            if (commandSender instanceof Player) {
                plugin.getMessageManager().sendMessage(strings[0], (Player) commandSender, msg);
            } else {
                plugin.getMessageManager().sendMessage(strings[0], commandSender, msg);
            }

        } else {
            commandSender.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry, you entered that wrong. /msg <player> <message>");
        }

        return true;
    }
}
