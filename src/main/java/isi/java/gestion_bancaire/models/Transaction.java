package isi.java.gestion_bancaire.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double montant;
    private String type; // Paiement, Retrait, Virement...
    private Date dateTransaction;

    @ManyToOne
    @JoinColumn(name = "carte_id")
    private CarteBancaire carte;
}
