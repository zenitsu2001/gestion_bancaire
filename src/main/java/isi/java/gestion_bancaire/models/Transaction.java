package isi.java.gestion_bancaire.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double montant;
    private String type; // Paiement, Retrait, Virement...
    private LocalDate dateTransaction;

    @ManyToOne
    @JoinColumn(name = "carte_id")
    private CarteBancaire carte;

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDate dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public CarteBancaire getCarte() {
        return carte;
    }

    public void setCarte(CarteBancaire carte) {
        this.carte = carte;
    }
}