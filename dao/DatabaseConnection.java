package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/marketplace_db"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=UTC"
            + "&characterEncoding=UTF-8";
    private static final String USER     = "root";
    private static final String PASSWORD = "daniel";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✔ Conexiune la MySQL reusita!");
            System.out.println("  Baza de date: marketplace_db");

        } catch (ClassNotFoundException e) {
            System.err.println("✘ Driver MySQL nu a fost gasit!");
            System.err.println("  → Adauga mysql-connector-j.jar in IntelliJ");
            throw new RuntimeException("Driver MySQL lipsa: " + e.getMessage(), e);

        } catch (SQLException e) {
            System.err.println("✘ Eroare la conectare!");
            System.err.println("  → Verifica: MySQL pornit? Parola corecta? BD existenta?");
            System.err.println("  → Detalii: " + e.getMessage());
            throw new RuntimeException("Conexiune esuata: " + e.getMessage(), e);
        }
    }

    public static DatabaseConnection getInstance() {
        try {
            if (instance == null || instance.connection.isClosed()) {
                instance = new DatabaseConnection();
            }
        } catch (SQLException e) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✔ Reconectare reusita!");
            }
        } catch (SQLException e) {
            System.err.println("✘ Eroare la reconectare: " + e.getMessage());
            throw new RuntimeException("Reconectare esuata: " + e.getMessage(), e);
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✔ Conexiunea MySQL a fost inchisa.");
            }
        } catch (SQLException e) {
            System.err.println("✘ Eroare la inchidere: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Test conexiune MySQL ===");
        DatabaseConnection db = DatabaseConnection.getInstance();
        Connection conn = db.getConnection();
        try {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✔ Conexiunea functioneaza perfect!");
                System.out.println("  Catalog : " + conn.getCatalog());
                System.out.println("  Driver  : " + conn.getMetaData().getDriverName());
            }
        } catch (SQLException e) {
            System.err.println("✘ Test esuat: " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }
}