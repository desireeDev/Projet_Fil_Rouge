package com.example.bibliotheque.Model;

import javafx.beans.property.*;

public class User {
    private final StringProperty login = new SimpleStringProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty motDePasse = new SimpleStringProperty();
    private final ObjectProperty<Role> role = new SimpleObjectProperty<>();

    public enum Role {
        ADMIN,
        USER
    }

    public User() {
        // Constructeur vide (nécessaire pour les bindings FXML, etc.)
    }

    public User(String login, String nom, String firstName, String motDePasse, Role role) {
        this.login.set(login);
        this.nom.set(nom);
        this.firstName.set(firstName);
        this.motDePasse.set(motDePasse);
        this.role.set(role);
    }

    // Getters et setters avec propriétés JavaFX
    public String getLogin() {
        return login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public StringProperty loginProperty() {
        return login;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getMotDePasse() {
        return motDePasse.get();
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse.set(motDePasse);
    }

    public StringProperty motDePasseProperty() {
        return motDePasse;
    }

    public Role getRole() {
        return role.get();
    }

    public void setRole(Role role) {
        this.role.set(role);
    }

    public ObjectProperty<Role> roleProperty() {
        return role;
    }
}
