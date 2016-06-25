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
public class InstallCheckMessage implements Runnable {

    public InstallCheckMessage(Apollo plugin, String serverToCheck) {
        this.plugin = plugin;
        this.serverToCheck = serverToCheck;
    }

    private String serverToCheck;

    private Apollo plugin;

    @Override
    public void run() {

        synchronized (plugin.getConnect()) {
            Connect connect = plugin.getConnect();
            for (int i = 0; i <= 3; i++) {
                try {
                    MessageRequest messageRequest = new MessageRequest(serverToCheck, "Apollo.InstallCheck.Check", plugin.getApolloVersion());
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
        }
    }

}
