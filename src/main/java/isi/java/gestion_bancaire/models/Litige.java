package isi.java.gestion_bancaire.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "litiges")
public class Litige {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motif;
    private String statut; // En cours, Résolu...
    private Date dateLitige;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}