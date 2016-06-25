package net.inerva.portals;

import net.inerva.lilypad.redirect.RedirectManager;
import org.bukkit.entity.Player;

/**
 * Created by Edwin on 1/30/2016.
 */
public class PortalServer implements Portal {

    public PortalServer(String portalName, String serverName, RedirectManager redirectManager) {
        this.portalName = portalName;
        this.serverName = serverName;
        this.redirectManager = redirectManager;
    }

    protected String portalName;

    protected String regionID;

    protected String serverName;

    protected RedirectManager redirectManager;

    @Override
    public void teleportPlayer(Player player) {
        if (player.hasPermission("Apollo." + portalName)) {
            redirectManager.redirectPlayer(player, serverName);
        }
    }

    @Override
    public boolean isServerPortal() {
        return true;
    }


}
