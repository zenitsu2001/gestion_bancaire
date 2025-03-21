package isi.java.gestion_bancaire.services;

import isi.java.gestion_bancaire.config.JPAUtil;
import isi.java.gestion_bancaire.models.Client;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClientService {

    // Ajouter un client
    public void ajouterClient(Client client) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(client);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Erreur lors de l'ajout du client", e);
        } finally {
            entityManager.close();
        }
    }

    // Modifier un client
    public void modifierClient(Client client) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(client);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Erreur lors de la modification du client", e);
        } finally {
            entityManager.close();
        }
    }

    // Supprimer un client
    public void supprimerClient(Long id) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Client client = entityManager.find(Client.class, id);
            if (client != null) {
                entityManager.remove(client);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Erreur lors de la suppression du client", e);
        } finally {
            entityManager.close();
        }
    }

    // Récupérer tous les clients
    public List<Client> getClients() {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            return entityManager.createQuery("SELECT c FROM Client c", Client.class).getResultList();
        } finally {
            entityManager.close();
        }
    }
}