package net.inerva.lilypad.message;

import net.inerva.apollo.Apollo;
import net.inerva.apollo.ApolloServerInfo;

import java.util.Map;

/**
 * Created by Edwin on 1/21/2016.
 */
public class ServerTimeChecker implements Runnable {

    public ServerTimeChecker(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public void run() {
        for (Map.Entry<String, ApolloServerInfo> entry : plugin.getApolloServers().entrySet()) {

            entry.getValue().setLastUpdate(entry.getValue().getLastUpdate() + 1);

            if (entry.getValue().getLastUpdate() >= 15) {
                System.out.println("[Apollo] Server: " + entry.getKey() + " has disconnected.");
                plugin.getApolloServers().remove(entry.getKey());
            }


        }

    }

}
