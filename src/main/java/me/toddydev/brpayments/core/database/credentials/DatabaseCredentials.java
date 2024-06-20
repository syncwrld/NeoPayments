package me.toddydev.brpayments.core.database.credentials;

import lombok.*;

@Data
@AllArgsConstructor
public class DatabaseCredentials {

    private String host, port, username, password, database;

}
