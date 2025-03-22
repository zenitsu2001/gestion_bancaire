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
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

public class CarteBancaireController {

    // Composants FXML
    @FXML private ComboBox<Client> comboClients;
    @FXML private TextField txtNumero;
    @FXML private TextField txtType;
    @FXML private DatePicker dateExpiration;
    @FXML private TextField txtSolde;
    @FXML private TableView<CarteBancaire> cartesTable;
    @FXML private TableColumn<CarteBancaire, String> colNumero;
    @FXML private TableColumn<CarteBancaire, String> colType;
    @FXML private TableColumn<CarteBancaire, LocalDate> colDateExpiration;
    @FXML private TableColumn<CarteBancaire, Double> colSolde;
    @FXML private TableColumn<CarteBancaire, String> colClient;
    @FXML private TableColumn<CarteBancaire, Void> colActions;

    // Services
    private final EntityManager entityManager = JPAUtil.getEntityManager();
    private final CarteBancaireService carteBancaireService = new CarteBancaireService(entityManager);
    private final ClientService clientService = new ClientService();

    // Carte sélectionnée pour la modification
    private CarteBancaire carteSelectionnee;

    @FXML
    public void initialize() {
        // Charger les clients dans la ComboBox
        chargerClients();

        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDateExpiration.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));
        colClient.setCellValueFactory(cellData -> {
            CarteBancaire carte = cellData.getValue();
            return new SimpleStringProperty(carte.getClient().getNom() + " " + carte.getClient().getPrenom());
        });

        // Configurer la colonne "Actions"
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");

            {
                // Gérer l'action du bouton "Modifier"
                btnModifier.setOnAction(event -> {
                    CarteBancaire carte = getTableView().getItems().get(getIndex());
                    modifierCarteBancaire(carte);
                });

                // Gérer l'action du bouton "Supprimer"
                btnSupprimer.setOnAction(event -> {
                    CarteBancaire carte = getTableView().getItems().get(getIndex());
                    supprimerCarteBancaire(carte);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, btnModifier, btnSupprimer));
                }
            }
        });

        // Charger les cartes bancaires
        chargerCartesBancaires();

        // Charger les cartes bancaires
        chargerCartesBancaires();
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
        comboClients.setButtonCell(new ListCell<>() {
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

    // Configurer les colonnes de la TableView
    private void configurerTableView() {
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDateExpiration.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));
        colClient.setCellValueFactory(cellData -> {
            CarteBancaire carte = cellData.getValue();
            return new SimpleStringProperty(carte.getClient().getNom() + " " + carte.getClient().getPrenom());
        });

        // Configurer la colonne "Actions"
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");

            {
                // Gérer l'action du bouton "Modifier"
                btnModifier.setOnAction(event -> {
                    CarteBancaire carte = getTableView().getItems().get(getIndex());
                    modifierCarteBancaire(carte);
                });

                // Gérer l'action du bouton "Supprimer"
                btnSupprimer.setOnAction(event -> {
                    CarteBancaire carte = getTableView().getItems().get(getIndex());
                    supprimerCarteBancaire(carte);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, btnModifier, btnSupprimer));
                }
            }
        });
    }

    // Charger les cartes bancaires dans la TableView
    private void chargerCartesBancaires() {
        List<CarteBancaire> cartes = carteBancaireService.getCartesBancaires();
        cartesTable.getItems().setAll(cartes);
    }

    // Remplir le formulaire avec les données de la carte sélectionnée
    private void remplirFormulaire(CarteBancaire carte) {
        comboClients.getSelectionModel().select(carte.getClient());
        txtNumero.setText(carte.getNumero());
        txtType.setText(carte.getType());
        dateExpiration.setValue(carte.getDateExpiration());
        txtSolde.setText(String.valueOf(carte.getSolde()));
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
        LocalDate dateExpiration = this.dateExpiration.getValue();
        double solde = Double.parseDouble(txtSolde.getText());

        if (numero.isEmpty() || type.isEmpty() || dateExpiration == null) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        CarteBancaire carteBancaire = new CarteBancaire(numero, type, dateExpiration, solde, clientSelectionne);
        carteBancaireService.ajouterCarteBancaire(carteBancaire);

        // Rafraîchir la TableView
        chargerCartesBancaires();
        viderChamps();
    }

    // Modifier une carte bancaire
    private void modifierCarteBancaire(CarteBancaire carte) {
        if (carte == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une carte à modifier.");
            return;
        }

        // Remplir le formulaire avec les données de la carte sélectionnée
        remplirFormulaire(carte);
        carteSelectionnee = carte;
    }

    // Enregistrer les modifications
    @FXML
    private void enregistrerModifications() {
        if (carteSelectionnee == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une carte à modifier.");
            return;
        }

        // Mettre à jour les données de la carte sélectionnée
        carteSelectionnee.setNumero(txtNumero.getText());
        carteSelectionnee.setType(txtType.getText());
        carteSelectionnee.setDateExpiration(dateExpiration.getValue());
        carteSelectionnee.setSolde(Double.parseDouble(txtSolde.getText()));
        carteSelectionnee.setClient(comboClients.getSelectionModel().getSelectedItem());

        // Enregistrer les modifications
        carteBancaireService.modifierCarteBancaire(carteSelectionnee);

        // Rafraîchir la TableView
        chargerCartesBancaires();
        viderChamps();
    }

    // Supprimer une carte bancaire
    private void supprimerCarteBancaire(CarteBancaire carte) {
        if (carte == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une carte à supprimer.");
            return;
        }

        // Supprimer la carte sélectionnée
        carteBancaireService.supprimerCarteBancaire(carte);

        // Rafraîchir la TableView
        chargerCartesBancaires();
        viderChamps();
    }

    // Vider les champs du formulaire
    private void viderChamps() {
        comboClients.getSelectionModel().clearSelection();
        txtNumero.clear();
        txtType.clear();
        dateExpiration.setValue(null);
        txtSolde.clear();
        carteSelectionnee = null;
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