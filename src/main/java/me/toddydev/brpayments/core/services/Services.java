package me.toddydev.brpayments.core.services;

import lombok.Getter;
import me.toddydev.brpayments.core.services.mercadopago.MPService;

public class Services {

    @Getter
    private static MPService mercadoPagoService = new MPService();
}
