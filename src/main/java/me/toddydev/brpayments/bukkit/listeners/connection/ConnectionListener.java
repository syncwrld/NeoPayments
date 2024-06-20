package me.toddydev.brpayments.bukkit.listeners.connection;

import me.toddydev.brpayments.core.cache.Caching;
import me.toddydev.brpayments.core.database.tables.Tables;
import me.toddydev.brpayments.core.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

public class ConnectionListener implements Listener {
	
	@EventHandler
	public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
		CompletableFuture<User> user = Tables.getUsers().find(event.getUniqueId());
		
		user.thenAccept(u -> {
			if (u == null) {
				u = new User(event.getUniqueId(), event.getName());
				Tables.getUsers().create(u);
			}
			Caching.getUserCache().add(u);
		});
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Caching.getUserCache().remove(Caching.getUserCache().find(event.getPlayer().getUniqueId()));
	}
}
