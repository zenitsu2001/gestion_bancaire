package isi.java.gestion_bancaire.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "programme_fidelite")
public class ProgrammeFidelite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pointsAccumules;
    private double cashback; // Montant remboursé

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
