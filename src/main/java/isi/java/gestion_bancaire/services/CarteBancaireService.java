package isi.java.gestion_bancaire.services;

import isi.java.gestion_bancaire.models.CarteBancaire;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CarteBancaireService {

    private EntityManager entityManager;

    public CarteBancaireService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Ajouter une carte bancaire
    public void ajouterCarteBancaire(CarteBancaire carteBancaire) {
        entityManager.getTransaction().begin();
        entityManager.persist(carteBancaire);
        entityManager.getTransaction().commit();
    }

    // Modifier une carte bancaire
    public void modifierCarteBancaire(CarteBancaire carteBancaire) {
        entityManager.getTransaction().begin();
        entityManager.merge(carteBancaire);
        entityManager.getTransaction().commit();
    }
    public void supprimerCarteBancaire(CarteBancaire carte) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.contains(carte) ? carte : entityManager.merge(carte));
        entityManager.getTransaction().commit();
    }

    // Récupérer toutes les cartes bancaires
    public List<CarteBancaire> getCartesBancaires() {
        TypedQuery<CarteBancaire> query = entityManager.createQuery("SELECT c FROM CarteBancaire c", CarteBancaire.class);
        return query.getResultList();
    }

    // Récupérer les cartes bancaires d'un client
    public List<CarteBancaire> getCartesBancairesByClient(Long clientId) {
        TypedQuery<CarteBancaire> query = entityManager.createQuery(
                "SELECT c FROM CarteBancaire c WHERE c.client.id = :clientId", CarteBancaire.class);
        query.setParameter("clientId", clientId);
        return query.getResultList();
    }
}