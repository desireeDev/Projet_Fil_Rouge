package utils;

import java.sql.*;  // Import des classes pour la gestion de la base de données JDBC


/**
 * Classe utilitaire pour gérer la connexion à la base de données
 * et effectuer des opérations liées aux utilisateurs.
 */
public class DatabaseConnection {

    // URL de connexion à la base de données MySQL locale "projet_fil_rouge"
    private static final String URL = "jdbc:mysql://localhost:3306/projet_fil_rouge";

    // Identifiants de connexion à la base
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    /**
     * Constructeur par défaut.
     * Ici non utilisé car la classe fournit uniquement des méthodes statiques.
     */
    public DatabaseConnection(String s, String root, String root1) {
        // Constructeur vide ou inutile pour l'instant
    }

    /**
     * Méthode statique pour obtenir une connexion à la base de données.
     * @return une instance de Connection active
     * @throws SQLException si la connexion échoue
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Vérifie les informations de login d'un utilisateur.
     * Attention : mot de passe en clair (pas sécurisé, prévoir hashing !)
     * @param username Nom d'utilisateur
     * @param password Mot de passe
     * @return true si les informations sont correctes, false sinon
     */
    public static boolean login(String username, String password) {
        String query = "SELECT * FROM utilisateurs WHERE utilisateur_name = ? AND motDePasse = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Si une ligne est trouvée, login valide
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // En cas d'erreur SQL, retourner false
        }
    }

    /**
     * Enregistre un nouvel utilisateur dans la base.
     * @param nom Nom de famille
     * @param firstName Prénom
     * @param username Nom d'utilisateur
     * @param password Mot de passe (à sécuriser)
     * @param role Rôle de l'utilisateur (ex: admin, user, etc.)
     * @return true si l'insertion a réussi, false sinon
     */
    public static boolean register(String nom, String firstName, String username, String password, String role) {
        String query = "INSERT INTO utilisateurs (nom, firstName, utilisateur_name, motDePasse, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, firstName);
            stmt.setString(3, username);
            stmt.setString(4, password);
            stmt.setString(5, role);
            stmt.executeUpdate(); // Exécute l'insertion
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Erreur lors de l'insertion
        }
    }

    /**
     * Récupère le rôle d'un utilisateur donné.
     * @param username Nom d'utilisateur
     * @return le rôle sous forme de chaîne, ou null si utilisateur non trouvé
     */
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
        return null; // Rôle non trouvé ou erreur SQL
    }

    /**
     * Met à jour le mot de passe d'un utilisateur.
     * @param username Nom d'utilisateur
     * @param newPassword Nouveau mot de passe (à sécuriser)
     * @return true si la mise à jour a réussi, false sinon
     */
    public static boolean updatePassword(String username, String newPassword) {
        String query = "UPDATE utilisateurs SET motDePasse = ? WHERE utilisateur_name = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Retourne true si au moins une ligne modifiée
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Erreur lors de la mise à jour
        }
    }

    // METHODES LIVRES..

}
