
📚 JavaFX Library Manager

> Application de gestion de bibliothèque développée avec Java 17, JavaFX et JAXB.
> Projet fil rouge intégrant les modules *Java Avancé*, *Agilité Scrum* et *DevOps*.

🧭 Présentation

L’objectif de cette application est de fournir une interface graphique intuitive permettant de gérer une bibliothèque numérique. Elle permet d’ajouter, modifier, supprimer et consulter des livres, avec une persistance des données en XML (via JAXB) et des options avancées telles que l’export Word et la gestion des emprunts.

 🧱 Fonctionnalités par Lot

✅ Lot 1 – Fondations de l’application

 📖 CRUD complet des livres dans un tableau interactif

✅ Lot 2 – Export & Gestion des Emprunts

 📤 Menu Export (Word):

 🔄 Ajout d’une gestion d’emprunt

   Possibilité d’emprunter un livre depuis l’IHM
   Mise à jour du fichier XML
    Adaptation du schéma XSD

### 🚧 **Lot 3 – Authentification & Synchronisation BDD**

## 🧰 Technologies

| Composant                | Usage                             |
| ------------------------ | --------------------------------- |
| Java 17+             | Langage principal                 |
| JavaFX               | Interface utilisateur (UI)        |
| JAXB                 | Lecture/écriture XML              |
| Maven                | Gestionnaire de dépendances       |
| SceneBuilder       | Conception visuelle des vues FXML |
| GitLab             | Suivi de version et collaboration |
| Apache POI  (Lot 2)| Génération de documents Word      |
| JDBC/SQL      (Lot 3)   | Connexion à la base de données    |


## 🗂️ Arborescence du projet

```
📦 bibliothèque-javafx
 ┣ 📂 src
 ┃ ┣ 📂 controller       → Logique de l’interface (JavaFX)
 ┃ ┣ 📂 model            → Représentation des entités (Livre, etc.)
 ┃ ┣ 📂 view             → Vues FXML et éléments UI
 ┃ ┗ 📜 Main.java        → Point d’entrée de l’application
 ┣ 📜 pom.xml            → Fichier de configuration Maven
 ┣ 📜 bibliothèque.xml   → Données persistées
 ┣ 📜 schema.xsd         → Schéma de validation XML
 ┗ 📜 README.md          → Documentation du projet
```

---

#👨‍💻 Équipe de développement

Syntiche Désirée Attoh
Asma Trouky
Isaac Gaston
Comin Leo


> Nécessite Java 17 et Maven installés


 ✨ Bon développement à toutes et à tous !

