package me.toddydev.brpayments.core.database.tables.orders;

import me.syncwrld.booter.database.util.Async;
import me.toddydev.brpayments.core.Core;
import me.toddydev.brpayments.core.cache.Caching;
import me.toddydev.brpayments.core.model.order.Order;
import me.toddydev.brpayments.core.model.order.gateway.type.GatewayType;
import me.toddydev.brpayments.core.model.order.status.OrderStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class Orders {
	
	public void create() {
		String query = "create table if not exists orders(id INT AUTO_INCREMENT, payerId VARCHAR(36), referenceId VARCHAR(255), paymentId VARCHAR(255), productId VARCHAR(255), gateway VARCHAR(255), status VARCHAR(255), code VARCHAR(255), cost DOUBLE, PRIMARY KEY(id), FOREIGN KEY (payerId) REFERENCES users(uniqueId));";
		Async.run(() -> {
			try (PreparedStatement statement = Core.getDatabase().getConnection().prepareStatement(query)) {
				statement.execute();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	public void create(Order order) {
		final String query = "INSERT INTO orders(payerId, referenceId, paymentId, productId, gateway, status, code, cost) VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
		Async.run(() -> {
			try (PreparedStatement statement = Core.getDatabase().getConnection().prepareStatement(query)) {
				statement.setString(1, order.getPayerId().toString());
				statement.setString(2, order.getReferenceId());
				statement.setString(3, order.getPaymentId());
				statement.setString(4, order.getProductId());
				statement.setString(5, order.getGateway().getType().name());
				statement.setString(6, order.getStatus().name());
				statement.setString(7, order.getCode());
				statement.setDouble(8, order.getCost());
				statement.executeUpdate();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	public void update(Order order) {
		Async.run(() -> {
			try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement("UPDATE orders SET referenceId=?, paymentId=?, productId=?, gateway=?, status=?, code=?, cost=? WHERE payerId=? AND paymentId=?;")) {
				ps.setString(1, order.getReferenceId());
				ps.setString(2, order.getPaymentId());
				ps.setString(3, order.getProductId());
				ps.setString(4, order.getGateway().getType().name());
				ps.setString(5, order.getStatus().name());
				ps.setString(6, order.getCode());
				ps.setDouble(7, order.getCost());
				ps.setString(8, order.getPayerId().toString());
				ps.setString(9, order.getPaymentId());
				ps.executeUpdate();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	public Order find(UUID uniqueId) {
		final AtomicReference<Order> result = new AtomicReference<>(null);
		Async.run(() -> {
			try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement("SELECT * FROM orders WHERE payerId=?;")) {
				ps.setString(1, uniqueId.toString());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					result.set(Order.builder()
						.payerId(uniqueId)
						.referenceId(rs.getString("referenceId"))
						.paymentId(rs.getString("paymentId"))
						.productId(rs.getString("productId"))
						.gateway(Caching.getGatewaysCache().find(GatewayType.find(rs.getString("gateway"))))
						.code(rs.getString("code"))
						.status(OrderStatus.valueOf(rs.getString("status")))
						.cost(rs.getDouble("cost"))
						.build());
				}
				rs.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		return result.get();
	}
	
	public Order findByReferenceId(String referenceId) {
		final AtomicReference<Order> result = new AtomicReference<>(null);
		Async.run(() -> {
			try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement("SELECT * FROM orders WHERE referenceId=?;")) {
				ps.setString(1, referenceId);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					result.set(Order.builder()
						.payerId(UUID.fromString(rs.getString("payerId")))
						.referenceId(rs.getString("referenceId"))
						.paymentId(rs.getString("paymentId"))
						.productId(rs.getString("productId"))
						.gateway(Caching.getGatewaysCache().find(GatewayType.find(rs.getString("gateway"))))
						.code(rs.getString("code"))
						.status(OrderStatus.valueOf(rs.getString("status")))
						.cost(rs.getDouble("cost"))
						.build());
				}
				rs.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
		return result.get();
	}
	
	public List<Order> findAll() {
		CompletableFuture<List<Order>> future = new CompletableFuture<>();
		Async.run(() -> {
			List<Order> orders = new ArrayList<>();
			try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement("SELECT * FROM orders;")) {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					orders.add(Order.builder()
						.payerId(UUID.fromString(rs.getString("payerId")))
						.referenceId(rs.getString("referenceId"))
						.paymentId(rs.getString("paymentId"))
						.productId(rs.getString("productId"))
						.gateway(Caching.getGatewaysCache().find(GatewayType.find(rs.getString("gateway"))))
						.code(rs.getString("code"))
						.status(OrderStatus.valueOf(rs.getString("status")))
						.cost(rs.getDouble("cost"))
						.build());
				}
				rs.close();
			} catch (SQLException e) {
				future.completeExceptionally(e);
				throw new RuntimeException(e);
			}
			future.complete(orders);
		});
		return future.join();
	}
	
	public List<Order> findAllByStatus(OrderStatus status) {
		CompletableFuture<List<Order>> future = new CompletableFuture<>();
		Async.run(() -> {
			List<Order> orders = new ArrayList<>();
			try (PreparedStatement ps = Core.getDatabase().getConnection().prepareStatement("SELECT * FROM orders WHERE status=?;")) {
				ps.setString(1, status.name());
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					orders.add(Order.builder()
						.payerId(UUID.fromString(rs.getString("payerId")))
						.referenceId(rs.getString("referenceId"))
						.paymentId(rs.getString("paymentId"))
						.productId(rs.getString("productId"))
						.gateway(Caching.getGatewaysCache().find(GatewayType.find(rs.getString("gateway"))))
						.code(rs.getString("code"))
						.status(OrderStatus.valueOf(rs.getString("status")))
						.cost(rs.getDouble("cost"))
						.build());
				}
				rs.close();
			} catch (SQLException e) {
				future.completeExceptionally(e);
				throw new RuntimeException(e);
			}
			future.complete(orders);
		});
		return future.join();
	}
}
