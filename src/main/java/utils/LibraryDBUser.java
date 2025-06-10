
package utils;
import com.example.bibliotheque.Model.Auteur;
import com.example.bibliotheque.Model.Livre;
import com.example.bibliotheque.Model.User;
import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.codec.digest.DigestUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class LibraryDBUser {
    private Connection connection = null;
    private String pseudo;
    private String lastName;
    private String fistName;
    public LibraryDBUser(String pseudo, String lastName, String firstName, String password, UserLevel userLevel) throws SQLException {
        //Connect(url, userName, password);
    }
    public void Connect(String url, String userName, String password) throws SQLException {
        connection = DriverManager.getConnection(url, userName, password);
        String a = DigestUtils.sha256Hex("");
    }
    public void Connect(String ipAdress, String database, String userName, String password) throws SQLException {
        Connect("jdbc:mysql://"+ ipAdress +":3306/" + database, userName, password);;
    }
}
