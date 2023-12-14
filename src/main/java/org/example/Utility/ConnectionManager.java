package org.example.Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/holidays";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "Cedacri1234567!";

    private ConnectionManager() {}

    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
    }
}
