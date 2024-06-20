package me.toddydev.brpayments.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import me.toddydev.brpayments.bukkit.BukkitMain;
import me.toddydev.brpayments.core.database.credentials.DatabaseCredentials;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class Database {
	
	private Connection connection;
	private DatabaseCredentials credential;
	
	public void start(FileConfiguration configuration, DatabaseCredentials credential) {
		String string = configuration.getString("database.type");
		
		if (string.equalsIgnoreCase("mysql")) {
			this.credential = credential;
			connectMySQL();
			return;
		}
		
		if (string.equalsIgnoreCase("sqlite")) {
			connectSQLite(configuration);
			return;
		}
		
		throw new RuntimeException("Database type not found!");
	}
	
	private void connectSQLite(FileConfiguration configuration) {
		String path = configuration.getString("database.sqlite.file");
		File pathFile = new File(BukkitMain.getInstance().getDataFolder(), path);
		
		if (!pathFile.getParentFile().exists()) {
			pathFile.getParentFile().mkdirs();
		}
		
		if (!pathFile.exists()) {
			try {
				pathFile.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + pathFile.getAbsolutePath());
		} catch (Exception e) {
			BukkitMain.getInstance().getLogger().severe("Failed to connect to the database! Please check your credentials and try again. (" + e.getLocalizedMessage() + ")");
			Bukkit.getPluginManager().disablePlugin(BukkitMain.getInstance());
		}
	}
	
	private void connectMySQL() {
		HikariConfig config = new HikariConfig();
		
		config.setJdbcUrl("jdbc:mysql://" + credential.getHost() + ":" + credential.getPort() + "/" + credential.getDatabase());
		config.setUsername(credential.getUsername());
		config.setPassword(credential.getPassword());
		config.setDriverClassName("com.mysql.jdbc.Driver");
		
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		
		try {
			connection = new HikariDataSource(config).getConnection();
		} catch (Exception e) {
			BukkitMain.getInstance().getLogger().severe("Failed to connect to the database! Please check your credentials and try again. (" + e.getLocalizedMessage() + ")");
			Bukkit.getPluginManager().disablePlugin(BukkitMain.getInstance());
		}
	}
	
	
	public Connection getConnection() {
		try {
			return connection;
		} catch (Exception e) {
			throw new RuntimeException("Failed to get connection to the database! Please check your credentials and try again. (" + e.getLocalizedMessage() + ")");
		}
	}
	
	public void stop() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
