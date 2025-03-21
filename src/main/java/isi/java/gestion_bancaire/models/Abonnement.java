package isi.java.gestion_bancaire.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
@Table(name = "abonnements")
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Standard, Premium...
    private double tarif;
    private Date dateDebut;
    private Date dateFin;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
