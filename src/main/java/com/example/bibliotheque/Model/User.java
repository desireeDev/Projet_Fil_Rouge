package com.example.bibliotheque.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class User {
    //Définition des attributs
    private final IntegerProperty id;
    private final StringProperty login;
    private final StringProperty motDePasse;
    private final StringProperty nom;
    private final StringProperty email;
    private final ObjectProperty<Role> role;
    //Définition des roles
    public enum Role {
        ADMIN, USER
    }
//Définition d'un constructeur vide
    public User() {
        id = new SimpleIntegerProperty(this, "id");
        login = new SimpleStringProperty(this, "login");
        motDePasse = new SimpleStringProperty(this, "motDePasse");
        nom = new SimpleStringProperty(this, "nom");
        email = new SimpleStringProperty(this, "email");
        role = new SimpleObjectProperty<>(this, "role");
    }
//Définition d'un constructeur non vide
    public User(int id, String login, String motDePasse, String nom, String email, Role role) {
        this();
        this.id.set(id);
        this.login.set(login);
        this.motDePasse.set(motDePasse);
        this.nom.set(nom);
        this.email.set(email);
        this.role.set(role);
    }

    // Getters, setters et property pour id
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    // Getters, setters et property pour login
    public String getLogin() { return login.get(); }
    public void setLogin(String login) { this.login.set(login); }
    public StringProperty loginProperty() { return login; }

    // Getters, setters et property pour motDePasse
    public String getMotDePasse() { return motDePasse.get(); }
    public void setMotDePasse(String motDePasse) { this.motDePasse.set(motDePasse); }
    public StringProperty motDePasseProperty() { return motDePasse; }

    // Getters, setters et property pour nom
    public String getNom() { return nom.get(); }
    public void setNom(String nom) { this.nom.set(nom); }
    public StringProperty nomProperty() { return nom; }

    // Getters, setters et property pour email
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    // Getters, setters et property pour role
    public Role getRole() { return role.get(); }
    public void setRole(Role role) { this.role.set(role); }
    public ObjectProperty<Role> roleProperty() { return role; }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + getId() +
                ", login=" + getLogin() +
                ", nom=" + getNom() +
                ", email=" + getEmail() +
                ", role=" + getRole() +
                '}';
    }
}
