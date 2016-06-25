package net.inerva.lilypad.message;

import net.inerva.apollo.Apollo;
import net.inerva.lilypad.message.messages.*;
import org.bukkit.Bukkit;

/**
 * Created by Edwin on 1/14/2016.
 */
public class MessageSender {

    public MessageSender(Apollo adminChat) {
        this.plugin = adminChat;
    }

    private Apollo plugin;

    /**
     * Sends a messages to the admin chat on other servers.
     *
     * @param message Message to send to the other servers.
     */
    public void sendAdminChatMessage(String message) {

        Thread thread = new Thread(new AdminChatMessage(plugin, message));
        thread.start();

    }

    public void sendPlayerFound(String playerName, String serverName) {
        Thread thread = new Thread(new PlayerFoundMessage(plugin, serverName, playerName));
        thread.start();
    }

    public void sendPlayerNotFound(String playerName, String serverName) {
        Thread thread = new Thread(new PlayerNotFoundMessage(plugin, serverName, playerName));
        thread.start();
    }

    public void sendPlayerSearch(String playerName) {
        Thread thread = new Thread(new PlayerSearchMessage(playerName, plugin));
        thread.start();
    }

    public void sendInstallCheckAll() {
        Thread thread = new Thread(new InstallCheckAllMessage(plugin, Bukkit.getOnlinePlayers().size()));
        thread.start();
    }

    /**
     * Sends a messages to verify installation of Apollo on other servers.
     */
    public void sendInstallCheck(String serverToCheck) {

        Thread thread = new Thread(new InstallCheckMessage(plugin, serverToCheck));
        thread.start();

    }


}
