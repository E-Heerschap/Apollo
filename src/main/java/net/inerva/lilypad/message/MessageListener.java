package net.inerva.lilypad.message;

import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import net.inerva.apollo.Apollo;
import net.inerva.apollo.ApolloServerInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Edwin on 1/14/2016.
 */
public class MessageListener {

    public MessageListener(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @EventListener
    public void onMessage(MessageEvent event) {

        if (plugin.getConnect().getSettings().getUsername().equalsIgnoreCase(event.getSender())) {
            return;
        }

        try {
            if (event.getChannel().equals("Apollo.AdminChat")) {
                plugin.getAdminChatManager().displayChatMessage(event.getMessageAsString());
            } else if (event.getChannel().equals("Apollo.playersearch.Search")) {
                MessageSender messageSender = new MessageSender(plugin);
                if (plugin.getPlayerSearchManager().isOnThisServer(event.getMessageAsString())) {
                    messageSender.sendPlayerFound(event.getMessageAsString(), event.getSender());
                } else {
                    messageSender.sendPlayerNotFound(event.getMessageAsString(), event.getSender());
                }
            } else if (event.getChannel().equals("Apollo.playersearch.Found")) {
                plugin.getPlayerSearchManager().successfulServerSearch(event.getMessageAsString(), event.getSender());
            } else if (event.getChannel().equals("Apollo.playersearch.NotFound")) {
                System.out.println("Player not found messages");
                plugin.getPlayerSearchManager().failedServerSearch(event.getMessageAsString(), event.getSender());
            } else if (event.getChannel().equals("Apollo.InstallCheck.Check")) {
                //Checking if the server is already linked.
                if (plugin.getApolloServers().keySet().contains(event.getSender())) {
                    plugin.getApolloServers().get(event.getSender()).setLastUpdate(1);
                    return;
                }
                //Checking that the version of Apollo is the same or compatible.
                if (event.getMessageAsString().equals(plugin.getApolloVersion())) {
                    //Apollo versions match, add it to the list.
                    plugin.getApolloServers().put(event.getSender(), new ApolloServerInfo());
                    System.out.println("[Apollo] Linked with server " + event.getSender());
                } else {
                    //Check if the versions are compatible.
                    String thisV = plugin.getApolloVersion(); //This version
                    String sentV = event.getMessageAsString(); //Version from other server

                    String mainV = thisV.substring(0, thisV.lastIndexOf('.')); //Main version i.e 1.1.x
                    String subV = thisV.substring(thisV.lastIndexOf('.') + 1, thisV.length()); //Sub version i.e x.x.15

                    //Checking if main versions match.
                    if (mainV.equals(sentV.substring(0, sentV.lastIndexOf('.')))) {
                        //Yes, Apollo sub versions are not the same but these are compatible.
                        //Add server to list
                        plugin.getApolloServers().put(event.getSender(), new ApolloServerInfo());

                        //Notifying console that versions do not match.
                        System.out.println("[Apollo] Server versions are different but still compatible.");
                        System.out.println("[Apollo] Server: " + event.getSender() + " is running: " + event.getMessageAsString());
                        System.out.println("[Apollo] This server is running: " + plugin.getApolloVersion());

                    } else {
                        //Apollo versions do not match and are not compatible.
                        //Notifying console that Apollo will not link with the other server.
                        System.out.println("[Apollo] Incompatible server versions of Apollo!");
                        System.out.println("[Apollo] Server: " + event.getSender() + " is running: " + event.getMessageAsString());
                        System.out.println("[Apollo] This server is running: " + plugin.getApolloVersion());
                    }

                }
            } else if (event.getChannel().equals("Apollo.PlayerCount")) {
                if (plugin.getApolloServers().containsKey(event.getSender())) {

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(event.getMessage());
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                    ArrayList<String> players = null;
                    try {
                        players = (ArrayList<String>) objectInputStream.readObject();
                    } catch (ClassNotFoundException e) {
                        System.out.println("[Apollo] Failed to get list of players from server: " + event.getSender());
                    }
                    plugin.getApolloServers().get(event.getSender()).setPlayers(players);
                }
            } else if (event.getChannel().equals("Apollo.MSG")) {
                plugin.getMessageManager().displayMessage(event.getMessageAsString());
            }

        } catch (UnsupportedEncodingException e) {
            System.out.println("[Apollo] Exception occoured (UnsupportedEncodingException)");
            System.out.println("[Apollo] Channel: " + event.getChannel());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
