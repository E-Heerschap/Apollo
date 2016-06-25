package net.inerva.lilypad.redirect;

import net.inerva.apollo.Apollo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Edwin on 1/24/2016.
 */
public class RedirectGUIHandler implements Listener {

    public RedirectGUIHandler(Apollo plugin, RedirectManager redirectManager) {
        this.plugin = plugin;
        this.redirectManager = redirectManager;
    }

    private Apollo plugin;

    private RedirectManager redirectManager;

    private Inventory redirectInventory;

    private HashMap<String, String> serverNames = new HashMap<>();

    /**
     * Sets the item meta for a item.
     *
     * @param item        Item to apply the ItemMeta to.
     * @param displayName Name to set onto the item.
     * @param lore        Lore to set onto the item.
     */
    private void setItemMeta(ItemStack item, String displayName, List<String> lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(displayName);
        im.setLore(lore);
        item.setItemMeta(im); //TEST REMOVING THIS C:
    }

    /**
     * Refreshes the redirect GUI
     */
    public void InitializeSelectorInventory() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (file.exists()) {
            FileConfiguration config = plugin.getConfig();
            int serverCount = config.getInt("InventorySize");
            redirectInventory = Bukkit.createInventory(null, serverCount, "Server Selector");
            Set<String> servers = config.getConfigurationSection("Servers").getKeys(false);

            for (String s : servers) {
                String displayName = config.getString("Servers." + s + ".DisplayName");
                String serverName = config.getString("Servers." + s + ".Server");
                serverNames.put(displayName, serverName);
                System.out.println(displayName);
                displayName = ChatColor.translateAlternateColorCodes('&', displayName);
                System.out.println(displayName);
                System.out.println(ChatColor.translateAlternateColorCodes('&', "&8Weird_Server"));
                String materialName = config.getString("Servers." + s + ".ItemName");
                int slot = config.getInt("Servers." + s + ".Slot");
                List<String> lore = config.getStringList("Servers." + s + ".Lore");

                for (String sLore : lore) {
                    sLore = ChatColor.translateAlternateColorCodes('&', sLore);
                }

                ItemStack item = new ItemStack(Material.getMaterial(materialName));
                System.out.println("Amount: " + item.getType());
                setItemMeta(item, displayName, lore);


                redirectInventory.setItem(slot, item);
                serverNames.put(displayName, serverName);
            }

        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (plugin.isServerSwitcher()) {
            if (event.getInventory().getName().equals("Server Selector")) {
                if (event.getCurrentItem().hasItemMeta()) {
                    Player p = (Player) event.getWhoClicked();
                    String name = serverNames.get(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                    System.out.println("Name: " + name);
                    boolean isApolloServer = false;
                    for (String s : plugin.getApolloServers().keySet()) {
                        if (s.equalsIgnoreCase(name)) {
                            isApolloServer = true;
                            break;
                        }
                    }
                    if (isApolloServer) {
                        redirectManager.redirectPlayer(p, name);
                    }
                }
                event.setCancelled(true);
            } else if (event.getCursor().hasItemMeta()) {
                if (event.getCursor().getItemMeta().getDisplayName().equalsIgnoreCase("Server Selector")) {
                    event.setCancelled(true);
                }
            } else if (event.getCurrentItem().hasItemMeta()) {
                if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Server Selector")) {
                    event.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void playerClickEvent(PlayerInteractEvent event) {
        if (plugin.isServerSwitcher()) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getPlayer().getItemInHand().hasItemMeta()) {
                    if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Server Selector")) {
                        event.setCancelled(true);
                        event.getPlayer().openInventory(redirectInventory);
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerDropEvent(PlayerDropItemEvent event) {
        if (plugin.isServerSwitcher()) {
            event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals("Server Selector");
            event.setCancelled(true);
        }
    }

}
