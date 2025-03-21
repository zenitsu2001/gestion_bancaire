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
        loadView("/views/Clients.fxml",null);
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Pane view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadView(String fxml, Object controllerData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Pane view = loader.load();

            // Passer les données au contrôleur
            if (controllerData != null) {
                Object controller = loader.getController();
                if (controller instanceof CarteBancaireController) {
                    ((CarteBancaireController) controller).setClientSelectionne((Client) controllerData);
                }
            }

            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Client getClientSelectionne() {
        // Exemple : Récupérer le client sélectionné dans une TableView
        // return tableClients.getSelectionModel().getSelectedItem();

        return null; // Remplacez par la logique appropriée
    }
    public void loadCartesBancairesView() {
        Client clientSelectionne = getClientSelectionne();
        if (clientSelectionne == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner un client avant de gérer les cartes bancaires.");
            return;
        }
        loadView("/views/CartesBancaires.fxml", clientSelectionne);
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
