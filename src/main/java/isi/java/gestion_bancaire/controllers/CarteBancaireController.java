package isi.java.gestion_bancaire.controllers;

import isi.java.gestion_bancaire.models.CarteBancaire;
import isi.java.gestion_bancaire.models.Client;
import isi.java.gestion_bancaire.services.CarteBancaireService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class CarteBancaireController {

    @FXML private TableView<CarteBancaire> tableCartesBancaires;
    @FXML private TableColumn<CarteBancaire, String> colNumero;
    @FXML private TableColumn<CarteBancaire, String> colType;
    @FXML private TableColumn<CarteBancaire, Date> colDateExpiration;
    @FXML private TableColumn<CarteBancaire, Double> colSolde;

    @FXML private TextField txtNumero;
    @FXML private TextField txtType;
    @FXML private DatePicker dateExpiration;
    @FXML private TextField txtSolde;

    private CarteBancaireService carteBancaireService;
    private Client clientSelectionne;

    public void setClientSelectionne(Client client) {
        this.clientSelectionne = client;
        chargerCartesBancaires();
    }

    @FXML
    public void initialize() {
        // Lier les colonnes aux propriétés du modèle
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDateExpiration.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));

        // Ajouter un écouteur de sélection
        tableCartesBancaires.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> remplirFormulaire(newValue)
        );
    }

    private void chargerCartesBancaires() {
        if (clientSelectionne != null) {
            List<CarteBancaire> cartes = carteBancaireService.getCartesBancairesByClient(clientSelectionne.getId());
            tableCartesBancaires.getItems().setAll(cartes);
        }
    }

    private void remplirFormulaire(CarteBancaire carteBancaire) {
        if (carteBancaire != null) {
            txtNumero.setText(carteBancaire.getNumero());
            txtType.setText(carteBancaire.getType());
            dateExpiration.setValue(carteBancaire.getDateExpiration());
            txtSolde.setText(String.valueOf(carteBancaire.getSolde()));
        } else {
            viderChamps();
        }
    }

    private void viderChamps() {
        txtNumero.clear();
        txtType.clear();
        dateExpiration.setValue(null);
        txtSolde.clear();
    }

    @FXML
    private void ajouterCarteBancaire() {
        if (clientSelectionne == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un client.");
            return;
        }

        String numero = txtNumero.getText();
        String type = txtType.getText();
        LocalDate dateExpiration = (this.dateExpiration.getValue());
        double solde = Double.parseDouble(txtSolde.getText());

        CarteBancaire carteBancaire = new CarteBancaire(numero, type, dateExpiration, solde, clientSelectionne);
        carteBancaireService.ajouterCarteBancaire(carteBancaire);
        chargerCartesBancaires();
        viderChamps();
    }


    @FXML
    private void modifierCarteBancaire() {
        CarteBancaire carteSelectionnee = tableCartesBancaires.getSelectionModel().getSelectedItem();
        if (carteSelectionnee == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une carte à modifier.");
            return;
        }

        carteSelectionnee.setNumero(txtNumero.getText());
        carteSelectionnee.setType(txtType.getText());
        carteSelectionnee.setDateExpiration(dateExpiration.getValue());
        carteSelectionnee.setSolde(Double.parseDouble(txtSolde.getText()));

        carteBancaireService.modifierCarteBancaire(carteSelectionnee);
        chargerCartesBancaires();
        viderChamps();
    }

    @FXML
    private void supprimerCarteBancaire() {
        CarteBancaire carteSelectionnee = tableCartesBancaires.getSelectionModel().getSelectedItem();
        if (carteSelectionnee != null) {
            carteBancaireService.supprimerCarteBancaire(carteSelectionnee.getId());
            chargerCartesBancaires();
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}