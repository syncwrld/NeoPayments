package me.toddydev.brpayments.bukkit.loaders.categories;

import com.cryptomorin.xseries.XMaterial;
import me.toddydev.brpayments.core.cache.Caching;
import me.toddydev.brpayments.core.model.product.categories.Category;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class CategoryLoader {
	
	public static void load(JavaPlugin plugin) {
		for (String s : plugin.getConfig().getConfigurationSection("categories").getKeys(false)) {
			Material material = XMaterial.matchXMaterial(plugin.getConfig().getString("categories." + s + ".icon.material")).orElse(XMaterial.BARRIER).parseMaterial();
			
			if (material == null) {
				material = Material.BARRIER;
				plugin.getServer().getConsoleSender().sendMessage("§b[NeoPayments] Invalid material for category " + s + "! Changed to BARRIER.");
			}
			
			Category category = Category.builder()
				.id(s)
				.name(plugin.getConfig().getString("categories." + s + ".name"))
				.description(plugin.getConfig().getStringList("categories." + s + ".description"))
				.material(material)
				.data(plugin.getConfig().getInt("categories." + s + ".icon.id"))
				.build();
			
			Caching.getCategoryCache().add(category);
		}
	}
	
}
