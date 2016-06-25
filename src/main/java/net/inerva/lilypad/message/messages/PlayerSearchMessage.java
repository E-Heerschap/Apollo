package net.inerva.lilypad.message.messages;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.MessageResult;
import net.inerva.apollo.Apollo;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Edwin on 1/16/2016.
 */
public class PlayerSearchMessage implements Runnable {

    public PlayerSearchMessage(String playerName, Apollo plugin) {
        this.plugin = plugin;
        this.playerName = playerName;
    }

    private String playerName;

    private Apollo plugin;

    ArrayList<String> list = new ArrayList<>();

    @Override
    public void run() {
        synchronized (plugin.getConnect()) {
            Connect connect = plugin.getConnect();
            try {
                list.addAll(plugin.getApolloServers().keySet());
                System.out.println("Sending player search messages");
                MessageRequest messageRequest = new MessageRequest(list, "Apollo.playersearch.Search", playerName);
                FutureResult<MessageResult> result = connect.request(messageRequest);

                MessageResult mr = result.await(10000L);
                if (mr.getStatusCode() != StatusCode.SUCCESS) {
                    System.out.println("[Apollo] FAILED TO SEARCH FOR PLAYER: " + playerName);
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
    }
}
