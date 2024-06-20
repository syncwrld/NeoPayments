package me.toddydev.brpayments.core.model.product;

import lombok.*;
import me.toddydev.brpayments.core.model.order.gateway.Gateway;
import me.toddydev.brpayments.core.model.product.actions.Action;
import me.toddydev.brpayments.core.model.product.categories.Category;
import me.toddydev.brpayments.core.model.product.icon.Icon;
import me.toddydev.brpayments.core.model.product.rewards.Reward;
import me.toddydev.brpayments.core.model.order.gateway.type.GatewayType;

import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String id;
    private String name;

    private Icon icon;
    private Category category;

    private Reward rewards;

    private List<Action> actions;
    private List<Gateway> gateways;

    private double price;

}
