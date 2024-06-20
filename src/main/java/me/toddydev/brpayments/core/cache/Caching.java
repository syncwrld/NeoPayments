package me.toddydev.brpayments.core.cache;

import lombok.Getter;
import me.toddydev.brpayments.core.cache.categories.CategoryCache;
import me.toddydev.brpayments.core.cache.gateways.GatewaysCache;
import me.toddydev.brpayments.core.cache.orders.OrdersCache;
import me.toddydev.brpayments.core.cache.products.ProductCache;
import me.toddydev.brpayments.core.cache.users.UserCache;

public class Caching {

    @Getter
    private static UserCache userCache = new UserCache();
    
    @Getter
    private static OrdersCache ordersCache = new OrdersCache();

    @Getter
    private static ProductCache productCache = new ProductCache();

    @Getter
    private static CategoryCache categoryCache = new CategoryCache();

    @Getter
    private static GatewaysCache gatewaysCache = new GatewaysCache();

}
