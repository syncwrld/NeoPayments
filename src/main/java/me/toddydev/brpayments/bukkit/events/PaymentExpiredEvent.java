package me.toddydev.brpayments.bukkit.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.toddydev.brpayments.core.model.order.Order;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@AllArgsConstructor
public class PaymentExpiredEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    Player player;
    Order order;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
