package me.toddydev.brpayments.bukkit;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import lombok.Getter;
import me.toddydev.brpayments.bukkit.loaders.categories.CategoryLoader;
import me.toddydev.brpayments.bukkit.loaders.commands.BukkitCommandLoader;
import me.toddydev.brpayments.bukkit.loaders.gateways.GatewayLoader;
import me.toddydev.brpayments.bukkit.loaders.listeners.ListenerLoader;
import me.toddydev.brpayments.bukkit.loaders.products.ProductLoader;
import me.toddydev.brpayments.bukkit.task.PayTask;
import me.toddydev.brpayments.core.Core;
import me.toddydev.brpayments.core.database.credentials.DatabaseCredentials;
import me.toddydev.brpayments.core.database.tables.Tables;
import me.toddydev.brpayments.core.plugin.BukkitPlugin;
import me.toddydev.brpayments.core.utils.metrics.Metrics;
import me.toddydev.brpayments.discord.Discord;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class BukkitMain extends BukkitPlugin {
	
	@Getter
	private static YamlConfiguration messagesConfig;
	
	@Override
	public void load() {
		saveDefaultConfig();
		loadConfig();
		
		Core.getDatabase().start(
			getConfig(),
			new DatabaseCredentials(
				getConfig().getString("database.host"),
				getConfig().getString("database.port"),
				getConfig().getString("database.username"),
				getConfig().getString("database.password"),
				getConfig().getString("database.database")
			)
		);
	}
	
	@Override
	public void enable() {
		Tables.getUsers().create();
		Tables.getOrders().create();
		
		GatewayLoader.load(this);
		CategoryLoader.load(this);
		ProductLoader.load(this);
		
		ListenerLoader.load(this);
		
		InventoryManager.enable(this);
		
		BukkitCommandLoader.load(this);
		
		Core.setDiscord(new Discord(this));
		
		new Metrics(this, 19978);
		new PayTask().runTaskTimerAsynchronously(this, 0, 20 * 60);
	}
	
	private void loadConfig() {
		File f = new File(getDataFolder(), "messages.yml");
		
		if (!f.exists()) {
			saveResource("messages.yml", false);
		}
		
		messagesConfig = YamlConfiguration.loadConfiguration(f);
	}
	
	@Override
	public void disable() {
		Core.getDatabase().stop();
	}
}
