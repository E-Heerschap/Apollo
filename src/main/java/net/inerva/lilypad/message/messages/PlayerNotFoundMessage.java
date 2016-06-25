package net.inerva.lilypad.message.messages;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.MessageResult;
import net.inerva.apollo.Apollo;

import java.io.UnsupportedEncodingException;

/**
 * Created by Edwin on 1/17/2016.
 */
public class PlayerNotFoundMessage implements Runnable {
    public PlayerNotFoundMessage(Apollo plugin, String serverName, String playerName) {
        this.serverName = serverName;
        this.playerName = playerName;
        this.plugin = plugin;
    }

    private String serverName;

    private String playerName;

    private Apollo plugin;

    @Override
    public void run() {
        synchronized (plugin.getConnect()) {
            Connect connect = plugin.getConnect();
            try {
                MessageRequest messageRequest = new MessageRequest(serverName, "Apollo.playersearch.NotFound", playerName);
                FutureResult<MessageResult> result = connect.request(messageRequest);

                MessageResult mr = result.await(10000L);
                if (mr.getStatusCode() != StatusCode.SUCCESS) {
                    System.out.println("[Apollo] Failed to send not found messages");
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
