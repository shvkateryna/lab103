package ua.edu.ucu.apps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import java.sql.ResultSet;


public class CacheDocument {

    private Document document;

    public CacheDocument(Document document) {
        this.document = document;
    }

    private Connection connect() {
        String url = "jdbc:sqlite:database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean recordExists(String path) {
        String sql = "SELECT count(*) as count FROM documents WHERE path = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, path);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt("count") > 0;
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return false;
    }

    public void injectText(String text, String path) {
        String sql = "INSERT INTO documents (path, document) VALUES (?, ?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, path);
            pstmt.setString(2, text);
            pstmt.executeUpdate();
            System.out.println("Record inserted successfully");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    
    private static void handleSQLException(SQLException e) {
        System.err.println("SQL Exception: " + e.getMessage());
    }

    public void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS documents ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "path TEXT NOT NULL,"
                + "document TEXT NOT NULL);";

        try (Connection conn = this.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(createTableSQL)) {
            preparedStatement.executeUpdate();
            System.out.println("Database created");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static void main(String[] args) {
        CacheDocument app = new CacheDocument(new SmartDocument("gs://cv-examples/wiki.png"));
        app.createTable();
        String path = "database.db";
        if (app.recordExists(path)) {
            app.injectText("This record exists", path);
            System.out.println("Record updated successfully");
        } else {
            String text = app.document.parse();
            System.out.println("Record not found");
            app.injectText(text, path);
        }
    }
}