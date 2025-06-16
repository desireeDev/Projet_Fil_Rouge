package utils;
import java.sql.*;
import com.example.bibliotheque.Model.Auteur;
import com.example.bibliotheque.Model.Livre;
import com.example.bibliotheque.Model.Emprunteur;

public class DatabaseConnection {
    private Connection connection;
    //Déclaration des éléments de connection
    //Constructeur vide
    public DatabaseConnection(){}
    public static final String host="root";
    public static final String url="";
    public static final String password="root";
    public static final String Requete= "SELECT * FROM users WHERE  name =? AND password=? ";
//Fonction to connect
    public void connect(){}
//Fonction to register
    public void register(){}


}
