package isi.java.gestion_bancaire.services;

import isi.java.gestion_bancaire.config.JPAUtil;
import isi.java.gestion_bancaire.models.Transaction;
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
        entityManager.getTransaction().commit();
    }

    // Modifier une transaction
    public void modifierTransaction(Transaction transaction) {
        entityManager.getTransaction().begin();
        entityManager.merge(transaction);
        entityManager.getTransaction().commit();
    }

    // Supprimer une transaction
    public void supprimerTransaction(Transaction transaction) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(transaction) ? transaction : entityManager.merge(transaction));
        entityManager.getTransaction().commit();
    }

    // Récupérer toutes les transactions
    public List<Transaction> getTransactions() {
        return entityManager.createQuery("SELECT t FROM Transaction t", Transaction.class).getResultList();
    }

    // Récupérer une transaction par son ID
    public Transaction getTransactionById(Long id) {
        return entityManager.find(Transaction.class, id);
    }
}