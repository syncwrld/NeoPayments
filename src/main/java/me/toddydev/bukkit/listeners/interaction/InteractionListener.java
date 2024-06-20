package me.toddydev.bukkit.listeners.interaction;

import de.tr7zw.changeme.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Material.AIR;

public class InteractionListener implements Listener {
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		ItemStack item = event.getItemDrop().getItemStack();
		if (item.getType() != Material.MAP) return;
		
		String neoTag = NBT.get(item, readable -> {
			return readable.getString("neopayments:order");
		});
		
		if (neoTag == null) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item == null) return;
		if (item.getType() != Material.MAP) return;
		
		String neoTag = NBT.get(item, readable -> {
			return readable.getString("neopayments:order");
		});
		
		if (neoTag == null) return;
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		ItemStack item = event.getCurrentItem();
		if (item == null || item.getType() == AIR) return;
		if (item.getType() != Material.MAP) return;
		
		String neoTag = NBT.get(item, readable -> {
			return readable.getString("neopayments:order");
		});
		
		if (neoTag == null) return;
		event.setCancelled(true);
	}
}
