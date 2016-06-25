package net.inerva.apollo;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import lilypad.client.connect.api.Connect;
import net.inerva.adminchat.AdminChatManager;
import net.inerva.bukkitlistener.PlayerListener;
import net.inerva.command.*;
import net.inerva.lilypad.message.MessageListener;
import net.inerva.lilypad.message.SendServerCheck;
import net.inerva.lilypad.message.ServerTimeChecker;
import net.inerva.lilypad.redirect.RedirectManager;
import net.inerva.messaging.MessageManager;
import net.inerva.playersearch.PlayerSearchManager;
import net.inerva.portals.PortalManager;
import net.inerva.staffhelp.StaffHelpManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Edwin on 1/14/2016.
 */
public class Apollo extends JavaPlugin {

    //Version of Apollo, used to verify compatibility with other servers.
    private String apolloVersion = "1.0.1";

    //Map of Servers to remove next Apollo check.
    //Key = Server name
    //Value = Time since last response
    private Map<String, ApolloServerInfo> apolloServers = new HashMap<>();

    private AdminChatManager adminChatManager = new AdminChatManager(this);

    private PlayerSearchManager playerSearchManager = new PlayerSearchManager(this);

    private RedirectManager redirectManager = new RedirectManager(this);

    private StaffHelpManager staffHelpManager = new StaffHelpManager();

    private boolean serverSwitcher;

    private MessageManager messageManager = new MessageManager(this);

    private WorldGuardPlugin worldGaurd = null;

    private PortalManager portalManager = new PortalManager(this);

    @Override
    public void onEnable() {
        Connect connect = this.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        connect.registerEvents(new MessageListener(this));

        this.getCommand("achat").setExecutor(new ACommand(this));
        this.getCommand("findplayer").setExecutor(new FindPlayerCommand(this));
        this.getCommand("server").setExecutor(new ServerCommand(this));
        this.getCommand("staff").setExecutor(new StaffCommand(this));
        this.getCommand("sr").setExecutor(new SRCommand(this));
        this.getCommand("send").setExecutor(new SendCommand(this));
        this.getCommand("glist").setExecutor(new GListCommand(this));
        this.getCommand("msg").setExecutor(new MSGCommand(this));
        this.getCommand("r").setExecutor(new RCommand(this));
        this.getCommand("w").setExecutor(new MSGCommand(this));
        this.getCommand("m").setExecutor(new MSGCommand(this));
        this.getCommand("tell").setExecutor(new MSGCommand(this));
        this.getCommand("t").setExecutor(new MSGCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(redirectManager.getRedirectGUIHandler(), this);
        this.getServer().getPluginManager().registerEvents(this.portalManager, this);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new ServerTimeChecker(this), 20L, 20L);
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new SendServerCheck(this), 100L, 100L);

        File file = new File(getDataFolder(), "config.yml");
        if (file.exists()) {
            FileConfiguration config = getConfig();
            this.serverSwitcher = config.getBoolean("ServerSwitcher");

        } else {
            saveDefaultConfig();
        }

        redirectManager.getRedirectGUIHandler().InitializeSelectorInventory();

        getWorldGaurd();
        this.portalManager.loadPortals();

    }

    @Override
    public void onDisable() {
        Connect connect = this.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        connect.unregisterEvents(new MessageListener(this));
    }

    public Connect getConnect() {
        return this.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
    }

    public AdminChatManager getAdminChatManager() {
        return adminChatManager;
    }


    public Map<String, ApolloServerInfo> getApolloServers() {
        return this.apolloServers;
    }

    public String getApolloVersion() {
        return this.apolloVersion;
    }

    public PlayerSearchManager getPlayerSearchManager() {
        return playerSearchManager;
    }

    public RedirectManager getRedirectManager() {
        return redirectManager;
    }

    public StaffHelpManager getStaffHelpManager() {
        return staffHelpManager;
    }

    public boolean isServerSwitcher() {
        return serverSwitcher;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    /**
     * Ensures the WorldGuard plugin is a instance of WorldGuard.
     *
     * @return WorldGuardPlugin reference.
     */
    public WorldGuardPlugin getWorldGaurd() {

        if (this.worldGaurd == null) {
            this.worldGaurd = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
            if (this.worldGaurd == null) {
                System.out.println("[Apollo] Failed to get WorldGuard plugin! Ensure it is installed correctly.");
                return null;
            }
        } else if (!(this.worldGaurd instanceof WorldGuardPlugin)) {
            System.out.println("[Apollo] WorldGuard plugin is not a instance of the legitimate WorldGuard!");
            return null;
        }

        return worldGaurd;
    }

}
