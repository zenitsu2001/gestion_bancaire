package isi.java.gestion_bancaire.services;

import isi.java.gestion_bancaire.config.JPAUtil;
import isi.java.gestion_bancaire.models.Transaction;
import isi.java.gestion_bancaire.models.CarteBancaire;
import jakarta.persistence.EntityManager;

import java.util.List;
public class TransactionService {

    private final EntityManager entityManager;

    public TransactionService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Ajouter une transaction
    public void ajouterTransaction(Transaction transaction) {
        entityManager.getTransaction().begin();
        entityManager.persist(transaction);

        // Mettre à jour le solde de la carte bancaire
        CarteBancaire carte = transaction.getCarte();
        if (carte != null) {
            if ("Paiement".equals(transaction.getType()) || "Retrait".equals(transaction.getType())) {
                carte.setSolde(carte.getSolde() - transaction.getMontant());
            } else if ("Virement".equals(transaction.getType())) {
                // Déduire le montant de la carte émettrice
                carte.setSolde(carte.getSolde() - transaction.getMontant());

                // Ajouter le montant à la carte destinataire
                CarteBancaire carteDestinataire = transaction.getCarteDestinataire();
                if (carteDestinataire != null) {
                    carteDestinataire.setSolde(carteDestinataire.getSolde() + transaction.getMontant());
                    entityManager.merge(carteDestinataire);
                }
            }
            entityManager.merge(carte);
        }

        entityManager.getTransaction().commit();
    }

    // Modifier une transaction (à implémenter si nécessaire)
    public void modifierTransaction(Transaction transaction) {
        entityManager.getTransaction().begin();
        entityManager.merge(transaction);
        entityManager.getTransaction().commit();
    }

    // Supprimer une transaction (à implémenter si nécessaire)
    public void supprimerTransaction(Transaction transaction) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(transaction) ? transaction : entityManager.merge(transaction));
        entityManager.getTransaction().commit();
    }

    // Récupérer toutes les transactions
    public List<Transaction> getTransactions() {
        return entityManager.createQuery("SELECT t FROM Transaction t", Transaction.class).getResultList();
    }

    // Récupérer les transactions d'une carte bancaire spécifique
    public List<Transaction> getTransactionsByCarte(CarteBancaire carte) {
        return entityManager.createQuery("SELECT t FROM Transaction t WHERE t.carte = :carte", Transaction.class)
                .setParameter("carte", carte)
                .getResultList();
    }
}