package isi.java.gestion_bancaire.controllers;

import isi.java.gestion_bancaire.config.JPAUtil;
import isi.java.gestion_bancaire.models.CarteBancaire;
import isi.java.gestion_bancaire.models.Client;
import isi.java.gestion_bancaire.services.CarteBancaireService;
import isi.java.gestion_bancaire.services.ClientService;
import jakarta.persistence.EntityManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CarteBancaireController {
    @FXML
    private TableView<CarteBancaire> cartesTable;
    @FXML
    private TableColumn<CarteBancaire, String> colNumero;
    @FXML
    private TableColumn<CarteBancaire, String> colType;
    @FXML
    private TableColumn<CarteBancaire, LocalDate> colDateExpiration;
    @FXML
    private TableColumn<CarteBancaire, Double> colSolde;
    @FXML
    private TableColumn<CarteBancaire, String> colClient;
    // Composants FXML
    @FXML private ComboBox<Client> comboClients;
    @FXML private TextField txtNumero;
    @FXML private TextField txtType;
    @FXML private DatePicker dateExpiration;
    @FXML private TextField txtSolde;

    // Services
    EntityManager entityManager = JPAUtil.getEntityManager() ;
    private CarteBancaireService carteBancaireService = new CarteBancaireService(entityManager);
    private ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        // Charger les clients dans la ComboBox
        chargerClients();

        // Configurer les colonnes de la TableView
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDateExpiration.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));
        colClient.setCellValueFactory(cellData -> {
            CarteBancaire carte = cellData.getValue();
            return new SimpleStringProperty(carte.getClient().getNom() + " " + carte.getClient().getPrenom());
        });

        // Charger les cartes bancaires
        chargerCartesBancaires();
    }

    // Charger les cartes bancaires dans la TableView
    private void chargerCartesBancaires() {
        List<CarteBancaire> cartes = carteBancaireService.getCartesBancaires();
        cartesTable.getItems().setAll(cartes);
    }

    // Charger les clients dans la ComboBox
    private void chargerClients() {
        List<Client> clients = clientService.getClients();
        comboClients.getItems().setAll(clients);

        // Afficher le nom complet du client dans la ComboBox
        comboClients.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getNom() + " " + client.getPrenom());
                }
            }
        });

        // Afficher le nom complet du client dans la liste déroulante
        comboClients.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getNom() + " " + client.getPrenom());
                }
            }
        });
    }

    // Ajouter une carte bancaire
    @FXML
    private void ajouterCarteBancaire() {
        Client clientSelectionne = comboClients.getSelectionModel().getSelectedItem();
        if (clientSelectionne == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un client.");
            return;
        }

        String numero = txtNumero.getText();
        String type = txtType.getText();
        LocalDate localDate = dateExpiration.getValue();
        LocalDate dateExpiration = localDate;
        double solde = Double.parseDouble(txtSolde.getText());

        if (numero.isEmpty() || type.isEmpty() || dateExpiration == null) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        CarteBancaire carteBancaire = new CarteBancaire(numero, type, dateExpiration, solde, clientSelectionne);
        carteBancaireService.ajouterCarteBancaire(carteBancaire);

        // Vider les champs après l'ajout
        viderChamps();
    }

    // Vider les champs du formulaire
    private void viderChamps() {
        txtNumero.clear();
        txtType.clear();
        dateExpiration.setValue(null);
        txtSolde.clear();
    }

    // Afficher une alerte
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}