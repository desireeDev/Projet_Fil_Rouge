package utils;
import java.sql.*;
import com.example.bibliotheque.Model.Auteur;
import com.example.bibliotheque.Model.Livre;
import com.example.bibliotheque.Model.Emprunteur;

//Essai de fichier de connexion

import java.sql.*;

public class DatabaseConnection {
//Déclaration des id de connexion
    
    private static final String URL = "jdbc:mysql://localhost:3306/projet_fil_rouge";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public DatabaseConnection(String s, String root, String root1) {
    }

    //Constructeur
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Vérifie login et mot de passe (attention, pas sécurisé, prévoir hashing)
    public static boolean login(String username, String password) {
        String query = "SELECT * FROM utilisateurs WHERE utilisateur_name = ? AND motDePasse = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // L'utilisateur existe
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Enregistre un nouvel utilisateur
    public static boolean register(String nom, String firstName, String username, String password, String role) {
        String query = "INSERT INTO utilisateurs (nom, firstName, utilisateur_name, motDePasse, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, firstName);
            stmt.setString(3, username);
            stmt.setString(4, password);
            stmt.setString(5, role);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupère le rôle d’un utilisateur
    public static String getRole(String username) {
        String query = "SELECT role FROM utilisateurs WHERE utilisateur_name = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean updatePassword(String username, String newPassword) {
        String query = "UPDATE utilisateurs SET motDePasse = ? WHERE utilisateur_name = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
