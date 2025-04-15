package utils;

import com.example.bibliotheque.Model.Auteur;
import com.example.bibliotheque.Model.Livre;
//import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.util.ArrayList;

//Bon, en théorie il y a un fichier nomé StringBuilderrequetes_sql.txt dans le dossier du projet.
//Après avoir créé une datbase mysql, execute le texte du fichier. Ensuite tu pourra te connecter avec cette classe
//en théorie il y a un compte root avec tout par defaut, utilise ça :
//LibraryMySql qs = new LibraryMySql("127.0.0.1","projet_fil_rouge", "root", "password");
//sinon en théorie le script a créé deux utilisateurs, un admin avec tous les ptn de privileges, un simple qui peut juste select
//il y a pleins de méthodes pour la gestion des utilisateurs de la db mysql, mais en vrai, on en a pas besoin, vu qu'on a notre table utilisateurs

//j'avais des erreurs à cause du xml si je rajoutais un getter et un setter dans Auteur et Livres,
// du coup j'ai rajouté deux methodes à la con equivalents : mettreIdBiblioteque et avoirIdBiblioteque
// c'était juste pour mes testes; Aterme faut remplacer avec des vrais getter et setter
// (surtout que vu l'etat de mon commit ça ne marche pas mdr)

public class LibraryMySql {
    private Connection connection = null;
    public LibraryMySql() {}
    public LibraryMySql(String url, String userName, String password) throws SQLException {
        Connect(url, userName, password);
    }
    public LibraryMySql(String ipAdress, String database, String userName, String password) throws SQLException {
        Connect(ipAdress, database,userName,password);
    }
    public void Connect(String url, String userName, String password) throws SQLException {
        connection = DriverManager.getConnection(url, userName, password);
    }
    public void Connect(String ipAdress, String database, String userName, String password) throws SQLException {
        Connect("jdbc:mysql://"+ ipAdress +":3306/" + database, userName, password);;
    }
    public void Disconnect() throws SQLException {
        if(connection == null || connection.isClosed()) return;
        connection.close();
    }
    public void createAdmin(String userName, String password) throws SQLException {
        createMySqlUser(userName, password, "%");
        garantePrivilageToMySqlUser(userName, password, "ALL PRIVILEGES");
    }
    public void createUser(String userName, String password) throws SQLException {
        createMySqlUser(userName, password, "%");
        garantePrivilageToMySqlUser(userName, password, "SELECT");
        //String q = DigestUtils.sha1Hex("");
    }
    public void createMySqlUser(String userName, String password, String host) throws SQLException {
        if(connection == null || connection.isClosed()) return;

        String sqlQuery = "CREATE USER ?@? IDENTIFIED BY ?;";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, userName);
        statement.setString(2, host);
        statement.setString(3, password);
        statement.execute();
        statement.close();
    }
    public void garantePrivilageToMySqlUser(String userName, String host, String privilage) throws SQLException {
        if(connection == null || connection.isClosed()) return;

        String sqlQuery = "GRANT ? ON *.* TO ?@?;";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, privilage);
        statement.setString(2, userName);
        statement.setString(3, host);
        statement.execute();
        statement.close();
    }
    public  void deleteUser(String userName) throws SQLException {
        deleteUser(userName, "%");
    }
    public  void deleteUser(String userName, String host) throws SQLException {
        String sqlQuery = "DROP USER ?@?;";
        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, userName);
        statement.setString(2, host);
        statement.execute();
        statement.close();
    }
    public  ArrayList<Livre> getLivres() throws SQLException {
        ArrayList<Livre> livresList = new ArrayList();
        //Livre[] livres = new Livre[0];
        if(connection == null || connection.isClosed()) return livresList;

        String sqlLivresQuery = "SELECT id_biblio_livre, titre, presentation, parution, " +
                                "non_disponible, path_image, nom, prenom, id_biblio_auteur " +
                                "FROM livres, auteurs WHERE id_auteur = fk_auteur;";
        Statement statementLivres = connection.prepareStatement(sqlLivresQuery);
        ResultSet resultLivres =  statementLivres.executeQuery(sqlLivresQuery);
        //resultLivres.get
        while (resultLivres.next()) {
            Livre livre = new Livre();
            Auteur auteur = new Auteur();
            livre.setTitre(resultLivres.getString("titre"));
            livre.mettreIdBiblioteque(resultLivres.getInt("id_biblio_livre"));
            livre.setPresentation(resultLivres.getString("presentation"));
            livre.setParution(resultLivres.getInt("parution"));
            livre.setEmprunte(resultLivres.getBoolean(("non_disponible")));
            livre.setPathImage((resultLivres.getString("path_image")));
            auteur.setNom(resultLivres.getString("nom"));
            auteur.setPrenom(resultLivres.getString("prenom"));
            auteur.mettreIdBiblioteque(resultLivres.getInt("id_biblio_auteur"));
            livre.setAuteur(auteur);
            livresList.add(livre);
        }
        return livresList;
    }

    /**
     * Efface toutes les anciennes données
     * @param livres
     * @throws SQLException
     */
    public  void setLivres(Livre[] livres) throws SQLException {
        if(connection == null || connection.isClosed()) return;

        int idAuteur;
        String truncateQuery = "SET FOREIGN_KEY_CHECKS = 0; ";
        // à quoi sert la dernière ligne un peu chelou ? bah lisez ce truc https://stackoverflow.com/questions/778534/mysql-on-duplicate-key-last-insert-id
        String auteursQuery = "INSERT INTO auteurs (id_biblio_auteur, prenom, nom) VALUES (?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE id_auteur = LAST_INSERT_ID(id_auteur);";
        //difference entre StringBuilder et String : String est immuable et convient pour des chaînes constantes,
        // tandis que StringBuilder est mutable et plus efficace pour des modifications fréquentes (il gere Mistral en explications)
        //En gros, vu que la requete pour les livres sera modifié à de nombreuses reprises, vaut mieux que ça soit un StringBuilder
        StringBuilder livresQuery = new StringBuilder("INSERT INTO livres (id_biblio_livre, titre, presentation, " +
                "parution, non_disponible, path_image, fk_auteur) VALUES ");
        PreparedStatement psTruncate = connection.prepareStatement(truncateQuery);
        PreparedStatement psAuteur = connection.prepareStatement(auteursQuery, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement psLivre;

        //on desactive le controle des clefs etrangeres, parce que sinon le moteur de la database te fait tout un scandale
        //si t'essaye d'affacer des lignes avec à l'interieur des clefs etrangeres, même si la table de base est vide
        psTruncate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        //bim bam boum, on vide les tables
        psTruncate.execute("TRUNCATE TABLE livres;");
        psTruncate.execute("TRUNCATE TABLE auteurs;");
        //on reactive le controle des clefs etrangeres
        psTruncate.execute("SET FOREIGN_KEY_CHECKS = 1;");

        for (int i=0; i<livres.length; i++) {
            livresQuery.append("(?, ?, ?, ?, ?, ?, ?) ");
            if(i != livres.length-1) {
                livresQuery.append(", ");
            }
        }
        psLivre = connection.prepareStatement(livresQuery.toString());

        for(int i=0, j=0; i< livres.length; i++, j+=7) {
            Auteur auteur = livres[i].getAuteur();

            psAuteur.setInt(1, auteur.avoirIdBiblioteque());
            psAuteur.setString(2, auteur.getPrenom());
            psAuteur.setString(3, auteur.getNom());
            psAuteur.executeUpdate();

            //on recupere l'id dans la base de donnée de l'autre pour pouvoir la foutre en clef etrangere au livre
            ResultSet rs = psAuteur.getGeneratedKeys();
            rs.next();
            idAuteur = rs.getInt(1);

            // Insertion du livre
            psLivre.setInt(j + 1, livres[i].avoirIdBiblioteque());
            psLivre.setString(j + 2, livres[i].getTitre());
            psLivre.setString(j + 3, livres[i].getPresentation());
            psLivre.setInt(j + 4, livres[i].getParution());
            psLivre.setBoolean(j + 5, livres[i].isEmprunte());
            psLivre.setString(j + 6, livres[i].getPathImage());
            psLivre.setInt(j + 7, idAuteur);
        }
        psLivre.executeUpdate();
    }

    /**
     * Ne met à jour que les livres modifiés et les nouveaux livres
     * @param livres
     * @throws SQLException
     */
    public  void setLivresModifies(Livre[] livres) throws SQLException {

    }
    public  void setLivre(Livre livre) throws SQLException {
        if(connection == null || connection.isClosed()) return;

        int idAuteur = 0;
        String auteurQuery = "INSERT INTO auteurs (id_biblio_auteur, prenom, nom) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE id_auteur = LAST_INSERT_ID(id_auteur), id_biblio_auteur = VALUES(id_biblio_auteur), " +
                "prenom = VALUES(prenom), nom = VALUES(nom);";
        String livreQuery = "UPDATE livres SET titre = ?, presentation = ?, parution = ?, " +
                "non_disponible = ?, path_image = ?, fk_auteur = ? WHERE id_biblio_livre = ?";
        PreparedStatement psAuteur = connection.prepareStatement(auteurQuery, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement psLivre = connection.prepareStatement(livreQuery);

        psAuteur.setInt(1, livre.getAuteur().avoirIdBiblioteque());
        psAuteur.setString(2, livre.getAuteur().getPrenom());
        psAuteur.setString(3, livre.getAuteur().getNom());
        psAuteur.executeUpdate();

        ResultSet resultLivres = psAuteur.getGeneratedKeys();
        resultLivres.next();
        idAuteur = resultLivres.getInt(1);

        //while (resultLivres.next()) {
        //    idAuteur = resultLivres.getInt("id_auteur");
        //}
        //psAuteur.setInt(1, livre.getAuteur().avoirIdBiblioteque());
        psLivre.setString(1, livre.getTitre());
        psLivre.setString(2, livre.getPresentation());
        psLivre.setInt(3, livre.getParution());
        psLivre.setBoolean(4, livre.isEmprunte());
        psLivre.setString(5, livre.getPathImage());
        psLivre.setInt(6, idAuteur);
        psLivre.setInt(7, livre.avoirIdBiblioteque());
        psLivre.executeUpdate();
    }
    //public void
    public void addLivre(Livre livre) throws SQLException {
        if(connection == null || connection.isClosed()) return;

        int idAuteur;
        Auteur auteur = livre.getAuteur();
        String auteurSqlQuery = "INSERT INTO auteurs (id_biblio_auteur, nom, prenom) VALUES (?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE id_auteur = LAST_INSERT_ID(id_auteur);";
        String livreQuery = "INSERT INTO livres (id_biblio_livre, titre, presentation, parution, " +
                "non_disponible, path_image, fk_auteur) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement psAuteur = connection.prepareStatement(auteurSqlQuery, Statement.RETURN_GENERATED_KEYS);
        psAuteur.setInt(1, auteur.avoirIdBiblioteque());
        psAuteur.setString(2, auteur.getPrenom());
        psAuteur.setString(3, auteur.getNom());
        psAuteur.executeUpdate();

        ResultSet rs = psAuteur.getGeneratedKeys();
        rs.next();
        idAuteur = rs.getInt(1);

        PreparedStatement psLivre = connection.prepareStatement(livreQuery);

        psLivre.setInt(1, livre.avoirIdBiblioteque());
        psLivre.setString(2, livre.getTitre());
        psLivre.setString(3, livre.getPresentation());
        psLivre.setInt(4, livre.getParution());
        psLivre.setBoolean(5, livre.isEmprunte());
        psLivre.setString(6, livre.getPathImage());
        psLivre.setInt(7, idAuteur);
        psLivre.executeUpdate();
    }
}
