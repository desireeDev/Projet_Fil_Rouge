package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryMySql {
    private Connection connection = null;
    public LibraryMySql() {}
    public LibraryMySql(String url, String userName, String password) throws SQLException {
        connection = DriverManager.getConnection(url,userName,password);
    }
    public void a() throws SQLException {
        Statement statement = connection.createStatement();

    }
    public  void b() {
    }
}
