package me.toddydev.brpayments.core.database.tables.users;

import me.syncwrld.booter.database.util.Async;
import me.toddydev.brpayments.core.Core;
import me.toddydev.brpayments.core.player.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Users {
    
    public void create() {
        String query = "CREATE TABLE IF NOT EXISTS users(uniqueId VARCHAR(36) PRIMARY KEY, name VARCHAR(16), totalOrders INT, totalPaid DOUBLE, totalRefunded DOUBLE, balance DOUBLE);";
        Async.run(() -> {
            try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement(query)) {
                ps.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public CompletableFuture<Boolean> create(User user) {
        String query = "INSERT INTO users(uniqueId, name, totalOrders, totalPaid, totalRefunded, balance) VALUES(?, ?, ?, ?, ?, ?);";
        return Async.run(() -> {
            try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement(query)) {
                ps.setString(1, user.getUniqueId().toString());
                ps.setString(2, user.getName());
                ps.setInt(3, user.getTotalOrders());
                ps.setDouble(4, user.getTotalPaid());
                ps.setDouble(5, user.getTotalRefunded());
                ps.setDouble(6, user.getBalance());
                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public CompletableFuture<User> find(UUID uniqueId) {
        String query = "SELECT * FROM users WHERE uniqueId = ?;";
        return Async.run(() -> {
            try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement(query)) {
                ps.setString(1, uniqueId.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new User(
                      UUID.fromString(rs.getString("uniqueId")),
                      rs.getString("name"),
                      rs.getInt("totalOrders"),
                      rs.getDouble("totalPaid"),
                      rs.getDouble("totalRefunded"),
                      rs.getDouble("balance"),
                      null
                    );
                }
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public CompletableFuture<User> find(String name) {
        String query = "SELECT * FROM users WHERE name = ?;";
        return Async.run(() -> {
            try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement(query)) {
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new User(
                      UUID.fromString(rs.getString("uniqueId")),
                      rs.getString("name"),
                      rs.getInt("totalOrders"),
                      rs.getDouble("totalPaid"),
                      rs.getDouble("totalRefunded"),
                      rs.getDouble("balance"),
                      null
                    );
                }
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public void update(User user) {
        String query = "UPDATE users SET name = ?, totalOrders = ?, totalPaid = ?, totalRefunded = ?, balance = ? WHERE uniqueId = ?;";
        Async.run(() -> {
            try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement(query)) {
                ps.setString(1, user.getName());
                ps.setInt(2, user.getTotalOrders());
                ps.setDouble(3, user.getTotalPaid());
                ps.setDouble(4, user.getTotalRefunded());
                ps.setDouble(5, user.getBalance());
                ps.setString(6, user.getUniqueId().toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
