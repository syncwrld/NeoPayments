package me.toddydev.brpayments.core.model.order.gateway.type;

public enum GatewayType {

    MERCADO_PAGO,
    PIC_PAY,
    STRIPE,
    PAG_SEGURO;

    public static GatewayType find(String value) {
        value  = value.replace("-", "_");
        for (GatewayType gatewayType : values()) {
            if (gatewayType.name().equalsIgnoreCase(value)) {
                return gatewayType;
            }
        }
        return null;
    }

}
