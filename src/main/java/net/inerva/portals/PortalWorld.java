package net.inerva.portals;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Edwin on 1/30/2016.
 */
public class PortalWorld implements Portal {

    public PortalWorld(Location location, String portalName) {
        this.location = location;
        this.portalName = portalName;
    }

    protected Location location;

    protected String portalName;

    @Override
    public void teleportPlayer(Player player) {
        if (player.hasPermission("Apollo." + portalName)) {
            player.teleport(this.location);
        }
    }

    @Override
    public boolean isServerPortal() {
        return false;
    }


}
