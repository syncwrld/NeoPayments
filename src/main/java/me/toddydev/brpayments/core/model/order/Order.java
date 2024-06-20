package me.toddydev.brpayments.core.model.order;

import lombok.*;
import me.toddydev.brpayments.core.model.order.gateway.Gateway;
import me.toddydev.brpayments.core.model.order.gateway.type.GatewayType;
import me.toddydev.brpayments.core.model.order.status.OrderStatus;

import java.util.UUID;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private UUID payerId;
    private String referenceId;
    private String paymentId;

    private String productId;

    private Gateway gateway;
    private OrderStatus status;

    private String code;
    private String ticketLink;

    private double cost;
}
