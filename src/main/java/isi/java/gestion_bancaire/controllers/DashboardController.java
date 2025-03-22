package isi.java.gestion_bancaire.controllers;

import isi.java.gestion_bancaire.models.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
public class DashboardController {

    @FXML
    private StackPane contentArea;

    public void loadClientsView() {
        loadView("/views/Clients.fxml", "Gestion des Clients");
    }

    public void loadCartesBancairesView() {
        loadView("/views/CartesBancaires.fxml", "Gestion des Cartes Bancaires");
    }

    private void loadView(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Pane view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            showErrorAlert("Erreur de chargement", "Impossible de charger la vue : " + title);
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}