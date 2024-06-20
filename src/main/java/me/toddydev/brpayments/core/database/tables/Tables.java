package me.toddydev.brpayments.core.database.tables;

import lombok.Getter;
import me.toddydev.brpayments.core.database.tables.orders.Orders;
import me.toddydev.brpayments.core.database.tables.users.Users;

public class Tables {

    @Getter
    private static Users users = new Users();

    @Getter
    private static Orders orders = new Orders();
}
