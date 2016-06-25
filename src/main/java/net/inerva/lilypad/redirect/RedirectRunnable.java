package net.inerva.lilypad.redirect;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.FutureResultListener;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.RedirectResult;
import net.inerva.apollo.Apollo;
import org.bukkit.entity.Player;

/**
 * Created by Edwin on 1/24/2016.
 */
public class RedirectRunnable implements Runnable {

    public RedirectRunnable(String server, Player player, Apollo plugin) {
        this.player = player;
        this.server = server;
        this.plugin = plugin;
    }

    private Apollo plugin;

    private String server;

    private Player player;

    @Override
    public void run() {
        Connect connect = plugin.getConnect();
        RedirectRequest redirectRequest = new RedirectRequest(server, player.getName());
        try {
            connect.request(redirectRequest).registerListener(new FutureResultListener<RedirectResult>() {
                @Override
                public void onResult(RedirectResult redirectResult) {
                    if (redirectResult.getStatusCode() != StatusCode.SUCCESS) {
                        System.out.println("[Apollo] Failed to transfer player: " + player.getName() + " to server: " + server);
                    }
                }
            });
        } catch (RequestException e) {
            System.out.println("[Apollo] Failed to transfer player: " + player.getName() + " to server: " + server);
        }
    }

}
