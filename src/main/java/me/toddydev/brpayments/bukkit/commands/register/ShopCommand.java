package me.toddydev.brpayments.bukkit.commands.register;

import me.toddydev.brpayments.bukkit.BukkitMain;
import me.toddydev.brpayments.bukkit.commands.BukkitCommand;
import me.toddydev.brpayments.bukkit.menus.IndexMenu;
import me.toddydev.brpayments.bukkit.menus.categories.CategoryMenu;
import me.toddydev.brpayments.bukkit.menus.products.ProductsMenu;
import me.toddydev.brpayments.core.api.placeholder.PlaceholderLoader;
import me.toddydev.brpayments.core.cache.Caching;
import me.toddydev.brpayments.core.model.product.categories.Category;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand extends BukkitCommand {
    public ShopCommand() {
        super(
                "shop",
                "",
                "loja", "comprar", "comprarpix",
                "pix"
        );
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        Player player = (!(sender instanceof Player)) ? null : (Player) sender;

        if (player == null) {
            sender.sendMessage("§cEste comando só pode ser executado in-game.");
            return false;
        }

        if (args.length == 0) {
            IndexMenu menu = new IndexMenu().init();
            menu.openInventory(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("category")) {
            if (args.length == 1) {
                player.sendMessage(PlaceholderLoader.setPlaceholders(player, BukkitMain.getMessagesConfig().getString("command-shop-category-usage").replace("&", "§")));
                return false;
            }

            Category category = Caching.getCategoryCache().findIgnoreCase(args[1]);
            if (category == null) {
                player.sendMessage(PlaceholderLoader.setPlaceholders(player, BukkitMain.getMessagesConfig().getString("command-shop-category-not-found").replace("&", "§")));
                return false;
            }

            ProductsMenu menu = new ProductsMenu(category).init();
            menu.openInventory(player);
            return true;
        } else if (args[0].equalsIgnoreCase("categories")) {
            CategoryMenu menu = new CategoryMenu().init();
            menu.openInventory(player);
            return true;
        } else {
            player.sendMessage(PlaceholderLoader.setPlaceholders(player, BukkitMain.getMessagesConfig().getString("command-shop-usage").replace("&", "§")));
        }

        return false;
    }
}
