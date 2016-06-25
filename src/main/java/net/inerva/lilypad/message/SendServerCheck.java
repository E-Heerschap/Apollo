package net.inerva.lilypad.message;

import net.inerva.apollo.Apollo;

/**
 * Created by Edwin on 1/21/2016.
 */
public class SendServerCheck implements Runnable {

    public SendServerCheck(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    @Override
    public void run() {

        MessageSender messageSender = new MessageSender(plugin);
        messageSender.sendInstallCheckAll();

    }
}
