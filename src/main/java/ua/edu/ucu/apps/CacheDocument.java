package ua.edu.ucu.apps;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CacheDocument {

    private Connection connect() {
        String url = "jdbc:sqlite:my_database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean recordExists(String path) {
        String sql = "SELECT count(*) FROM your table WHERE path = ?";
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, path);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void injectText(String text, String path) {
        String sql = "INSERT INTO your_table SET your_column = ? WHERE path = ?";
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, path);
                pstmt.setString(2, text);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }

    public static void main(String[] args) {
        CacheDocument app = new CacheDocument();
        String path = "my_database.db";
        if (app.recordExists(path)) {
            app.injectText("Your text here", path);
            System.out.println("Record updated successfully");
        } else {
            System.out.println("Record not found");
        }
    }
}