package net.inerva.messaging;

import net.inerva.apollo.Apollo;
import net.inerva.lilypad.message.messages.MSGMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Edwin on 1/30/2016.
 */
public class MessageManager {

    public MessageManager(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    //Key = Player that will do /r
    //Value = Player that last sent a message to them.
    private ConcurrentHashMap<String, String> playerResponds = new ConcurrentHashMap<>();

    private boolean found = false;

    /**
     * Adds a a message to the playerResponds hashmap if the receiver name is valid.
     * @param senderName Name of the sender
     * @param partialReceiverName Partial name of the receiver
     */
    public void addToPlayerResponds(String senderName, String partialReceiverName){
        Player player = Bukkit.getPlayer(partialReceiverName);

        if(player != null){

            playerResponds.put(player.getName(), senderName);
        }
    }

    /**
     * Sends a message to a player if on another server.
     *
     * @param playerTo   Name of player to send the message to.
     * @param playerFrom Player that sent the message
     * @param message    Message to send to the player.
     */
    public void sendMessage(String playerTo, Player playerFrom, String message) {

        this.found = false;

        Bukkit.getOnlinePlayers().parallelStream().forEach(player -> {
            if (player.getName().equalsIgnoreCase(playerTo)) {

                playerResponds.put(playerTo, playerFrom.getName());

                playerFrom.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "Me" + ChatColor.YELLOW + " -> " + ChatColor.RED +
                        player.getName() + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);

                player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + playerFrom.getDisplayName() + ChatColor.YELLOW + " -> " + ChatColor.RED +
                        "Me" + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);

                this.found = true;
            }
        });

        if (!found) {
            plugin.getApolloServers().entrySet().parallelStream().forEach(entry -> {
                for (String s : entry.getValue().getPlayers()) {
                    if (s.equalsIgnoreCase(playerTo)) {
                        playerFrom.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "Me" + ChatColor.YELLOW + " -> " + ChatColor.RED +
                                s + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);
                        Thread thread = new Thread(new MSGMessage(s, playerFrom.getName(), playerFrom.getDisplayName(), message, plugin));
                        thread.start();
                        this.found = true;
                    }
                }

            });
        }

        if (!found) {
            playerFrom.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry, that player was not found on the Inerva Network.");
        }

    }

    /**
     * Sends a message to a player if on another server.
     *
     * @param playerTo   Name of player to send the message to.
     * @param playerFrom Player that sent the message
     * @param message    Message to send to the player.
     */
    public void sendMessage(String playerTo, CommandSender playerFrom, String message) {

        Bukkit.getOnlinePlayers().parallelStream().forEach(player -> {
            if (player.getName().equalsIgnoreCase(playerTo)) {

                playerResponds.put(playerTo, playerFrom.getName());

                playerFrom.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "Me" + ChatColor.YELLOW + " -> " + ChatColor.RED +
                        player.getName() + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);

                player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + playerFrom + ChatColor.YELLOW + " -> " + ChatColor.RED +
                        "Me" + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);

                this.found = true;
            }
        });

        plugin.getApolloServers().entrySet().parallelStream().forEach(entry -> {
            for (String s : entry.getValue().getPlayers()) {

                if (s.equalsIgnoreCase(playerTo)) {
                    playerFrom.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "Me" + ChatColor.YELLOW + " -> " + ChatColor.RED +
                            s + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);
                    Thread thread = new Thread(new MSGMessage(s, playerFrom.getName(), playerFrom.getName(), message, plugin));
                    thread.start();
                    return;
                }
            }
        });

    }

    /**
     * Sends a reply to a player.
     *
     * @param player  Player that is sending the reply
     * @param message Message to send as a reply.
     */
    public void replyMessage(Player player, String message) {

        this.found = false;

        if (playerResponds.containsKey(player.getName())) {
            String playerTo = playerResponds.get(player.getName());
            Bukkit.getOnlinePlayers().parallelStream().forEach(p -> {
                if (p.getName().equalsIgnoreCase(playerTo)) {

                    playerResponds.put(playerTo, player.getName());

                    p.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + player.getDisplayName() + ChatColor.YELLOW + " -> " + ChatColor.RED +
                            "Me" + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);

                    this.found = true;
                }
            });
                player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "Me" + ChatColor.YELLOW + " -> " + ChatColor.RED +
                        playerTo + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);

            if(!found) {
                Thread thread = new Thread(new MSGMessage(playerTo, player.getName(), player.getDisplayName(), message, plugin));
                thread.start();
            }
        } else {
            player.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry, someone has not messaged you or is offline.");
        }

    }

    public void removePlayerMessaging(String playerName){
        if(this.playerResponds.containsValue(playerName)){
            playerResponds.entrySet().parallelStream().forEach(entry ->{
                if(entry.getValue() == playerName){
                    this.playerResponds.remove(entry.getKey());
                }
                if(entry.getKey() == playerName){
                    this.playerResponds.remove(entry.getKey());
                }
            });
        }
    }

    public void replyMessage(CommandSender player, String message) {

        this.found = false;

        if (playerResponds.containsKey(player.getName())) {
            String playerTo = playerResponds.get(player.getName());

            Bukkit.getOnlinePlayers().parallelStream().forEach(p -> {
                if (p.getName().equalsIgnoreCase(playerTo)) {

                    playerResponds.put(playerResponds.get(player.getName()), player.getName());

                    p.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + player.getName() + ChatColor.YELLOW + " -> " + ChatColor.RED +
                            "Me" + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);

                    this.found = true;

                }
            });

                player.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "Me" + ChatColor.YELLOW + " -> " + ChatColor.RED +
                        playerTo + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);

            if(!found) {
                Thread thread = new Thread(new MSGMessage(playerTo, player.getName(), ChatColor.DARK_PURPLE + player.getName(), message, plugin));
                thread.start();
            }
        } else {
            player.sendMessage(ChatColor.BLUE + "Inerva: " + ChatColor.RED + "Sorry, someone has not messaged you or is offline.");
        }
    }

    /**
     * Attempts to display a message to a player.
     *
     * @param messageListenerString String recieved from the Lilypad Apollo.MSG channel.
     */
    public void displayMessage(String messageListenerString) {

        StringTokenizer stringTokenizer = new StringTokenizer(messageListenerString, ".");
        String playerTo = stringTokenizer.nextToken();
        String playerFrom = stringTokenizer.nextToken();
        String playerDisplayName = stringTokenizer.nextToken();
        String message = stringTokenizer.nextToken();

        Player p = Bukkit.getPlayer(playerTo);

        if (p != null) {

            p.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + playerDisplayName + ChatColor.YELLOW + " -> " + ChatColor.RED +
                    "Me" + ChatColor.YELLOW + "] " + ChatColor.WHITE + message);
            playerResponds.put(playerTo, playerFrom);
        }
    }

}
