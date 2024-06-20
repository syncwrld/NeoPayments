package me.toddydev.brpayments.core.api.qrcore;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.tr7zw.changeme.nbtapi.NBT;
import com.github.syncwrld.neopay.util.Text;
import me.toddydev.brpayments.bukkit.BukkitMain;
import me.toddydev.brpayments.core.api.placeholder.PlaceholderLoader;
import me.toddydev.brpayments.core.cache.Caching;
import me.toddydev.brpayments.core.database.tables.Tables;
import me.toddydev.brpayments.core.model.order.Order;
import me.toddydev.brpayments.core.player.User;
import me.toddydev.brpayments.core.utils.item.ItemBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.metadata.FixedMetadataValue;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImageCreator {
	
	public static void generateMap(String data, Player player) {
		try {
			BitMatrix matrix = new MultiFormatWriter().encode(
				new String(data.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8),
				BarcodeFormat.QR_CODE, 128, 128
			);
			
			MapView mapView = Bukkit.createMap(player.getWorld());
			mapView.setScale(MapView.Scale.CLOSEST);
			mapView.getRenderers().clear();
			mapView.addRenderer(new QRCodeMapRenderer(matrix));
			
			List<String> lore = Text.color(PlaceholderLoader.setPlaceholders(player, BukkitMain.getMessagesConfig().getStringList("item-qrcode-description")));
			ItemStack item = new ItemBuilder(Material.MAP, mapView.getId())
				.name(Text.color(PlaceholderLoader.setPlaceholders(player, BukkitMain.getMessagesConfig().getString("item-qrcode-name"))))
				.lore(lore)
				.build();
			
			NBT.modify(item, nbt -> {
				nbt.setBoolean("neopayments:order", true);
			});
			
			player.setMetadata("neopayments:order", new FixedMetadataValue(BukkitMain.getInstance(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1)));
			
			User user = Caching.getUserCache().find(player.getUniqueId());
			Order order = Caching.getOrdersCache().findByPayer(player.getUniqueId());
			
			String ticketLink = order.getTicketLink();
			TextComponent component = new TextComponent(
				PlaceholderLoader.setPlaceholders(player, BukkitMain.getMessagesConfig().getString("success-payment-link").replace("&", "§").replace("{nl}", "\n").replace("{ticket_link}", ticketLink))
			);
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(
				PlaceholderLoader.setPlaceholders(player, BukkitMain.getMessagesConfig().getString("success-payment-link-hover").replace("&", "§").replace("{nl}", "\n"))
			)));
			component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, ticketLink));
			player.spigot().sendMessage(component);
			
			ItemStack currentItem = player.getItemInHand();
			if (currentItem != null && currentItem.getType() != Material.AIR) {
				user.setItemInHand(currentItem);
			}
			user.setTotalOrders(user.getTotalOrders() + 1);
			Tables.getUsers().update(user);
			
			player.setItemInHand(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static class QRCodeMapRenderer extends MapRenderer {
		private final BitMatrix matrix;
		private boolean rendered = false;
		
		public QRCodeMapRenderer(BitMatrix matrix) {
			this.matrix = matrix;
		}
		
		@Override
		public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
			if (!rendered) {
				mapCanvas.drawImage(0, 0, MatrixToImageWriter.toBufferedImage(matrix));
				rendered = true;
			}
		}
	}
}
