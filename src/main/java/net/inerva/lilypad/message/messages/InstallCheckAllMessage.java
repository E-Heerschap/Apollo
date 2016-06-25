package net.inerva.lilypad.message.messages;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.MessageResult;
import net.inerva.apollo.Apollo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Edwin on 1/17/2016.
 */
public class InstallCheckAllMessage implements Runnable {

    public InstallCheckAllMessage(Apollo plugin, int playerCount) {
        this.plugin = plugin;
        this.playerCount = playerCount;
    }

    private int playerCount;

    private Apollo plugin;

    @Override
    public void run() {
        synchronized (plugin.getConnect()) {


            Connect connect = plugin.getConnect();
            for (int i = 0; i <= 3; i++) {
                try {
                    MessageRequest messageRequest = new MessageRequest(Collections.EMPTY_LIST, "Apollo.InstallCheck.Check", plugin.getApolloVersion());
                    FutureResult<MessageResult> result = connect.request(messageRequest);
                    MessageResult mr = result.await(10000L);
                    if (mr.getStatusCode() != StatusCode.SUCCESS) {
                        System.out.println("[Apollo] Failed to check for servers install of Apollo!");
                        System.out.println("[Apollo] Trying again, " + i + " / 3");
                    } else {
                        break;
                    }

                } catch (RequestException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    System.out.println("[Apollo] Failed to use connect object. (NullPointerException). Try restarting Lilypad.");
                }
            }

            try {
                //Serializing the list of players on the server.
                byte[] byteArray = null;
                synchronized (plugin.getApolloServers()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream outputStream = new ObjectOutputStream(baos);
                    List<String> list = new ArrayList<>();
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        list.add(p.getName());
                    }
                    outputStream.writeObject(list);
                    outputStream.close();
                    byteArray = baos.toByteArray();
                }

                MessageRequest messageRequest = new MessageRequest(Collections.EMPTY_LIST, "Apollo.PlayerCount", byteArray);
                FutureResult<MessageResult> result = connect.request(messageRequest);
                MessageResult mr = result.await(10000L);
                if (mr.getStatusCode() != StatusCode.SUCCESS) {
                    System.out.println("[Apollo] Failed to send amount of online players.");
                }

            } catch (RequestException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("[Apollo] Failed to use connect object. (NullPointerException). Try restarting Lilypad.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
