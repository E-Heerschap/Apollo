package net.inerva.portals;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.inerva.apollo.Apollo;
import net.inerva.lilypad.redirect.RedirectManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Edwin on 1/30/2016.
 */
public class PortalManager implements Listener {

    public PortalManager(Apollo plugin) {
        this.plugin = plugin;
        this.redirectManager = plugin.getRedirectManager();
    }

    private RedirectManager redirectManager;

    private Apollo plugin;

    //Key = PortalName
    //Value = World
    private HashMap<String, Portal> portals = new HashMap<>();

    /**
     * Checks is player is in a portal. Teleports them if appropriate.
     *
     * @param player Player to check if in portal.
     */
    public void isPlayerInPortal(Player player) {

        RegionManager regionManager = plugin.getWorldGaurd().getRegionManager(player.getWorld());

        System.out.println("isPlayerInPortal");
        portals.entrySet().forEach(portal -> {
            ProtectedRegion region = regionManager.getRegion(portal.getKey());
            System.out.println("KEY: " + portal.getKey());
            if (region.contains(player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ())) {
                System.out.println("");
                portal.getValue().teleportPlayer(player);

            }
        });

    }


    /**
     * Must be called on startup of Apollo server.
     */
    public void loadPortals() {
        try {
            File file = new File(plugin.getDataFolder(), "portals.yml");
            if (file.exists()) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                for (String s : config.getConfigurationSection("Portals").getKeys(false)) {
                    String portalName = s;
                    String regionName = config.getString("Portals." + s + ".RegionID");
                    boolean isServerPortal = config.getBoolean("Portals." + s + ".ServerPortal");
                    String serverWorld = null;
                    int x;
                    int y;
                    int z;
                    if (isServerPortal) {
                        serverWorld = config.getString("Portals." + s + ".ServerName");
                        portals.put(regionName, new PortalServer(portalName, serverWorld, this.redirectManager));
                    } else {
                        serverWorld = config.getString("Portals." + s + ".WorldName");
                        x = config.getInt("Portals." + s + ".LocX");
                        y = config.getInt("Portals." + s + ".LocY");
                        z = config.getInt("Portals." + s + ".LocZ");
                        portals.put(regionName, new PortalWorld(new Location(Bukkit.getWorld(serverWorld), x, y, z), portalName));
                    }
                }
            } else {
                file.createNewFile();
                InputStream stream = null;
                stream = PortalManager.class.getClassLoader().getResourceAsStream("PortalConfig.yml");
                if (stream == null) {
                    System.out.println("[Apollo] FAILED TO CREATE NEW PORTAL CONFIG");
                    return;
                } else {
                    int readBytes;
                    byte[] buffer = new byte[4096];
                    FileOutputStream fso = new FileOutputStream(file);
                    while ((readBytes = stream.read(buffer)) > 0) {
                        fso.write(buffer, 0, readBytes);
                    }
                    fso.close();
                    stream.close();
                }
            }
        } catch (IOException e) {

        } catch (NullPointerException e) {
            System.out.println("[Apollo] Failed load portals. None to load?");
        }
    }

    @EventHandler
    public void playerPortalEvent(PlayerPortalEvent event) {
        isPlayerInPortal(event.getPlayer());
        event.setCancelled(true);
    }

}
