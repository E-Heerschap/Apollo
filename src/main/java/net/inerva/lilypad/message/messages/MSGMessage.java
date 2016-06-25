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
import java.util.List;

/**
 * Created by Edwin on 1/30/2016.
 */
public class MSGMessage implements Runnable {

    public MSGMessage(String playerTo, String playerFormName, String playerFromDisplayName, String message, Apollo plugin) {
        this.message = message;
        this.playerFrom = playerFormName;
        this.playerTo = playerTo;
        this.plugin = plugin;
        this.playerFromDisplayName = playerFromDisplayName;
    }

    private Apollo plugin;

    private String playerFrom;

    private String message;

    private String playerTo;

    private String playerFromDisplayName;

    @Override
    public void run() {

        synchronized (plugin.getConnect()) {
            Connect connect = plugin.getConnect();
            synchronized (plugin.getApolloServers()) {
                try {
                    List<String> list = new ArrayList<>();
                    list.addAll(plugin.getApolloServers().keySet());
                    String sendMessage = playerTo + "." + playerFrom + "." + playerFromDisplayName + "." + message;
                    MessageRequest messageRequest = new MessageRequest(list, "Apollo.MSG", sendMessage);
                    FutureResult<MessageResult> result = connect.request(messageRequest);

                    MessageResult mr = result.await(10000L);
                    if (mr.getStatusCode() != StatusCode.SUCCESS) {
                        System.out.println("[Apollo] Failed to send messages via admin chat.");
                        plugin.getAdminChatManager().displayChatMessage(message);
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
}
