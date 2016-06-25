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
 * Created by Edwin on 1/15/2016.
 */
public class AdminChatMessage implements Runnable {

    public AdminChatMessage(Apollo adminChat, String message) {
        this.plugin = adminChat;
        this.message = message;
    }

    private Apollo plugin;

    private String message;

    ArrayList list = new ArrayList<>();

    @Override
    public void run() {
        synchronized (plugin.getConnect()) {
            Connect connect = plugin.getConnect();

            try {
                list.addAll(plugin.getApolloServers().keySet());
                MessageRequest messageRequest = new MessageRequest(list, "Apollo.AdminChat", message);
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
