package net.inerva.bukkitlistener;

import net.inerva.apollo.Apollo;
import net.inerva.lilypad.message.MessageSender;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

/**
 * Created by Edwin on 1/15/2016.
 */
public class PlayerListener implements Listener {

    public PlayerListener(Apollo plugin) {
        this.plugin = plugin;
    }

    private Apollo plugin;

    private boolean checkedServers = false;

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().hasPermission("Apollo.AdminChat")) {
            plugin.getAdminChatManager().addAdminToChat(event.getPlayer());
            if (!checkedServers) {
                MessageSender messageSender = new MessageSender(plugin);
                messageSender.sendInstallCheckAll();
                checkedServers = true;
            }
        }

        if (event.getPlayer().hasPermission("Apollo.HelpStaff")) {
            plugin.getStaffHelpManager().addToStaffToNotify(event.getPlayer().getUniqueId());
        }

        if (plugin.isServerSwitcher()) {
            ItemStack item = new ItemStack(Material.COMPASS);
            setItemMeta(item, "Server Selector", Collections.EMPTY_LIST);
            event.getPlayer().getInventory().setItem(0, item);
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (event.getPlayer().hasPermission("Apollo.AdminChat")) {
            plugin.getAdminChatManager().removeAdminFromChat(event.getPlayer());
        }

        if (event.getPlayer().hasPermission("Apollo.HelpStaff")) {
            plugin.getStaffHelpManager().removeFromStaffToNotify(event.getPlayer().getUniqueId());
        }

        plugin.getMessageManager().removePlayerMessaging(event.getPlayer().getName());

    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("Apollo.AdminChat")) {
            if (plugin.getAdminChatManager().isToggled(event.getPlayer())) {
                event.setCancelled(true);
                String message = ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "Admin Chat" + ChatColor.DARK_GRAY + "] "
                        + ChatColor.GOLD + event.getPlayer().getName() + ChatColor.DARK_GRAY + ": " + ChatColor.WHITE
                        + event.getMessage();
                plugin.getAdminChatManager().sendChatMessage(message);
            }
        }
    }

}
