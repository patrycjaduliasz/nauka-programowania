package backend.bazadanych;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// polaczenie z MySQL

public class Baza {
    private static final String URL = ""; // wpisz swoj link
    private static final String USER = ""; // wpisz nazwe uzytkownika
    private static final String PASSWORD = ""; // wpisz haslo

    private Connection connection;

    public Baza() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}