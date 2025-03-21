package isi.java.gestion_bancaire.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "cartes_bancaires")
public class CarteBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numero;

    @Column(nullable = false)
    private String type; // Visa, Mastercard...

    @Column(nullable = false)
    private LocalDate dateExpiration;

    @Column(nullable = false)
    private double solde;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public CarteBancaire() {}

    public CarteBancaire(String numero, String type, LocalDate dateExpiration, double solde, Client client) {
        this.numero = numero;
        this.type = type;
        this.dateExpiration = dateExpiration;
        this.solde = solde;
        this.client = client;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }

    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}
