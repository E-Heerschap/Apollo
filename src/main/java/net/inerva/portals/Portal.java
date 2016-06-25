package net.inerva.portals;

import org.bukkit.entity.Player;

/**
 * Created by Edwin on 1/30/2016.
 */
public interface Portal {

    void teleportPlayer(Player player);

    boolean isServerPortal();

}
