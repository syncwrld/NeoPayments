package me.toddydev.brpayments.core;

import lombok.Getter;
import lombok.Setter;
import me.toddydev.brpayments.core.database.Database;
import me.toddydev.brpayments.discord.Discord;

public class Core {

    @Getter @Setter
    private static Discord discord;

    @Getter
    private static Database database = new Database();

}
