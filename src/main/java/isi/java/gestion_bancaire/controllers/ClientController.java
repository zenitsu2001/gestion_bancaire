package isi.java.gestion_bancaire.controllers;

import isi.java.gestion_bancaire.models.Client;
import isi.java.gestion_bancaire.services.ClientService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientController {
    @FXML private TableView<Client> tableClients;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colPrenom;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colTelephone;

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelephone;
    private Client clientSelectionne = null;
    @FXML private Button btnModifier;

    private ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        chargerClients();
        // Ajouter un écouteur de sélection
        tableClients.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> remplirFormulaire(newValue)
        );
        getClientSelectionne() ;
    }

    private void chargerClients() {
        tableClients.getItems().setAll(clientService.getClients());
    }
    public Client getClientSelectionne() {
        return tableClients.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void ajouterClient() {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();
        String telephone = txtTelephone.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty()) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        Client client = new Client(nom, prenom, email, telephone);
        clientService.ajouterClient(client);
        chargerClients();
        viderChamps();
    }

    @FXML
    private void supprimerClient() {
        Client client = tableClients.getSelectionModel().getSelectedItem();
        if (client == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un client à supprimer.");
            return;
        }

        clientService.supprimerClient(client.getId());
        chargerClients();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void viderChamps() {
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        txtTelephone.clear();
    }

    private void remplirFormulaire(Client client) {
        if (client != null) {
            txtNom.setText(client.getNom());
            txtPrenom.setText(client.getPrenom());
            txtEmail.setText(client.getEmail());
            txtTelephone.setText(client.getTelephone());
            btnModifier.setDisable(false); // Activer le bouton Modifier

        } else {
            viderChamps();
        }

    }
    @FXML
    private void modifierClient() {
        Client clientSelectionne = tableClients.getSelectionModel().getSelectedItem();
        if (clientSelectionne != null) {
            clientSelectionne.setNom(txtNom.getText());
            clientSelectionne.setPrenom(txtPrenom.getText());
            clientSelectionne.setEmail(txtEmail.getText());
            clientSelectionne.setTelephone(txtTelephone.getText());

            clientService.modifierClient(clientSelectionne);
            chargerClients();
            viderChamps();
        } else {
            afficherAlerte("Erreur", "Aucun client sélectionné pour la modification.");
        }
    }
}