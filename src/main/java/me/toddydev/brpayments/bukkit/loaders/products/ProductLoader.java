package me.toddydev.brpayments.bukkit.loaders.products;

import com.cryptomorin.xseries.XSound;
import me.toddydev.brpayments.core.cache.Caching;
import me.toddydev.brpayments.core.model.order.gateway.Gateway;
import me.toddydev.brpayments.core.model.order.gateway.type.GatewayType;
import me.toddydev.brpayments.core.model.product.Product;
import me.toddydev.brpayments.core.model.product.actions.Action;
import me.toddydev.brpayments.core.model.product.actions.screen.Screen;
import me.toddydev.brpayments.core.model.product.actions.type.ActionType;
import me.toddydev.brpayments.core.model.product.categories.Category;
import me.toddydev.brpayments.core.model.product.icon.Icon;
import me.toddydev.brpayments.core.model.product.rewards.Reward;
import me.toddydev.brpayments.core.model.product.rewards.item.RewardItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Material.BARRIER;

public class ProductLoader {
	
	public static void load(JavaPlugin plugin) {
		File dir = new File(plugin.getDataFolder().getPath() + "/products");
		
		if (!dir.exists()) {
			dir.mkdirs();
			
			File file = new File(dir.getPath(), "example.yml");
			try {
				file.createNewFile();
				InputStream r = plugin.getResource("products/produto.yml");
				YamlConfiguration yaml = YamlConfiguration.loadConfiguration(r);
				yaml.save(file);
			} catch (Exception e) {
				plugin.getServer().getConsoleSender().sendMessage("§b[NeoPayments] Não foi possível criar um arquivo de exemplo.");
			}
			return;
		}
		
		File[] files = dir.listFiles();
		if (files == null) return;
		
		for (File f : files) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
			
			Category category = Caching.getCategoryCache().find(config.getString("category"));
			
			if (category == null) {
				plugin.getServer().getConsoleSender().sendMessage("§b[NeoPayments] Não foi possível encontrar a categoria " + config.getString("category") + " para o produto " + config.getString("name") + ".");
				continue;
			}
			
			Product product = Product.builder().name(config.getString("name")).id(config.getString("id")).price(config.getDouble("price")).category(category).rewards(Reward.builder().build()).build();
			Material material = Material.getMaterial(config.getString("icon.material"));
			
			if (material == null) {
				material = BARRIER;
				plugin.getServer().getConsoleSender().sendMessage("§b[NeoPayments] Não foi possível encontrar o material " + config.getString("icon.material") + " para o produto " + product.getName() + ". Portanto foi alterado para BARRIER.");
			}
			
			Icon icon = Icon.builder().name(config.getString("icon.name").replace("&", "§")).description(config.getStringList("icon.description")).material(material).id(config.getInt("icon.id")).build();
			
			product.setIcon(icon);
			
			List<Action> actions = new ArrayList<>();
			List<Gateway> gateways = new ArrayList<>();
			List<RewardItem> items = new ArrayList<>();
			List<String> commands = config.getStringList("rewards.commands");
			
			for (String rawGateway : config.getStringList("gateways")) {
				GatewayType gatewayType = GatewayType.find(rawGateway.toUpperCase());
				
				if (gatewayType == null) {
					plugin.getServer().getConsoleSender().sendMessage("§b[NeoPayments] Não foi possível encontrar a gateway " + rawGateway + " para o produto " + product.getName() + ".");
					continue;
				}
				
				gateways.add(Caching.getGatewaysCache().find(gatewayType));
			}
			
			for (String rawItem : config.getConfigurationSection("rewards.items").getKeys(false)) {
				
				Material type = Material.getMaterial(rawItem);
				
				if (type == null) {
					type = BARRIER;
				}
				
				RewardItem item = RewardItem.builder().material(type).amount(config.getInt("rewards.items." + rawItem + ".amount")).data((short) config.getInt("rewards.items." + rawItem + ".id")).build();
				
				items.add(item);
			}
			
			for (String rawAction : config.getConfigurationSection("actions").getKeys(false)) {
				ActionType type = ActionType.find(rawAction.toUpperCase());
				
				if (type == null) {
					plugin.getServer().getConsoleSender().sendMessage("§b[NeoPayments] Não foi possível encontrar a ação " + rawAction + " para o produto " + product.getName() + ".");
					continue;
				}
				
				Sound sound;
				
				try {
					sound = XSound.matchXSound(config.getString("actions." + rawAction + ".sound").toUpperCase()).orElse(XSound.ENTITY_PLAYER_LEVELUP).parseSound();
				} catch (Exception e) {
					sound = Sound.LEVEL_UP;
					config.set("actions." + rawAction + ".sound", "LEVEL_UP");
					Bukkit.getConsoleSender().sendMessage("§b[NeoPayments] Não foi possível encontrar o som " + config.getString("actions." + rawAction + ".sound") + " para o produto " + product.getName() + ". Portanto foi alterado para LEVEL_UP.");
				}
				
				Action action = Action.builder().type(type).sound(sound).actionBar(config.getString("actions." + rawAction + ".action-bar").replace("&", "§")).message(config.getString("actions." + rawAction + ".message").replace("&", "§")).screen(Screen.builder().title(config.getString("actions." + rawAction + ".screen.title")).subtitle(config.getString("actions." + rawAction + ".screen.subtitle")).build()).build();
				
				actions.add(action);
			}
			
			product.setActions(actions);
			product.getRewards().setCommands(commands);
			product.getRewards().setItems(items);
			product.setGateways(gateways);
			
			Caching.getProductCache().add(product);
			Bukkit.getConsoleSender().sendMessage("§b[NeoPayments] O produto " + product.getName() + " foi carregado com sucesso.");
		}
	}
}
