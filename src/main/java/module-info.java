module isi.java.gestion_bancaire {
    // Modules JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Modules pour Hibernate et JPA
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires static lombok;


    // Ouvre les packages pour Hibernate, ByteBuddy et Spring
    opens isi.java.gestion_bancaire.models to org.hibernate.orm.core, spring.core;
    opens isi.java.gestion_bancaire.controllers to javafx.fxml, spring.core;
    opens isi.java.gestion_bancaire.services to spring.core;

    // Ouvre le package principal pour JavaFX et Spring
    opens isi.java.gestion_bancaire to javafx.fxml, spring.core;

    // Exporte le package principal
    exports isi.java.gestion_bancaire;
    exports isi.java.gestion_bancaire.controllers;
    exports isi.java.gestion_bancaire.services;
    exports isi.java.gestion_bancaire.models;
}