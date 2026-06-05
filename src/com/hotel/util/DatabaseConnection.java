package com.hotel.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    private static final Properties props = new Properties();
    private static final String PROP_PATH = "database/db.properties";
    private static Connection connection;

    static {
        try {
            props.load(new FileInputStream(PROP_PATH));
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.err.println("Failed to load database config: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                    props.getProperty("url"),
                    props.getProperty("user"),
                    props.getProperty("password")
                );
            }
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }

    public static void initializeSchema() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             java.util.Scanner scanner = new java.util.Scanner(
                 new java.io.File("database/schema.sql")).useDelimiter(";")) {

            while (scanner.hasNext()) {
                String sql = scanner.next().trim();
                if (!sql.isEmpty()) {
                    try {
                        stmt.execute(sql);
                    } catch (Exception ignored) {}
                }
            }
            System.out.println("Database schema initialized.");
        } catch (Exception e) {
            System.out.println("Schema init error: " + e.getMessage());
        }
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (Exception ignored) {}
    }
}
