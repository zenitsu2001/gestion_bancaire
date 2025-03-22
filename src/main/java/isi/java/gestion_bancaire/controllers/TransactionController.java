package isi.java.gestion_bancaire.controllers;

import isi.java.gestion_bancaire.config.JPAUtil;
import isi.java.gestion_bancaire.models.Transaction;
import isi.java.gestion_bancaire.services.TransactionService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

public class TransactionController {

    // Composants FXML
    @FXML private TextField txtMontant;
    @FXML private ComboBox<String> comboType;
    @FXML private DatePicker dateTransaction;
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Double> colMontant;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, LocalDate> colDate;
    @FXML private TableColumn<Transaction, Void> colActions;

    // Services
    private final TransactionService transactionService = new TransactionService(JPAUtil.getEntityManager());

    // Transaction sélectionnée pour la modification
    private Transaction transactionSelectionnee;

    @FXML
    public void initialize() {
        // Configurer les colonnes de la TableView
        configurerTableView();

        // Charger les types de transactions dans la ComboBox
        comboType.getItems().addAll("Paiement", "Retrait", "Virement");

        // Charger les transactions
        chargerTransactions();
    }

    // Configurer les colonnes de la TableView
    private void configurerTableView() {
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateTransaction"));

        // Configurer la colonne "Actions"
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");

            {
                // Gérer l'action du bouton "Modifier"
                btnModifier.setOnAction(event -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    modifierTransaction(transaction);
                });

                // Gérer l'action du bouton "Supprimer"
                btnSupprimer.setOnAction(event -> {
                    Transaction transaction = getTableView().getItems().get(getIndex());
                    supprimerTransaction(transaction);
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

    // Charger les transactions dans la TableView
    private void chargerTransactions() {
        List<Transaction> transactions = transactionService.getTransactions();
        transactionsTable.getItems().setAll(transactions);
    }

    // Ajouter une transaction
    @FXML
    private void ajouterTransaction() {
        double montant = Double.parseDouble(txtMontant.getText());
        String type = comboType.getSelectionModel().getSelectedItem();
        LocalDate date = dateTransaction.getValue();

        if (montant <= 0 || type == null || date == null) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis correctement.");
            return;
        }

        Transaction transaction = new Transaction();
        transaction.setMontant(montant);
        transaction.setType(type);
        transaction.setDateTransaction(date);

        transactionService.ajouterTransaction(transaction);

        // Rafraîchir la TableView
        chargerTransactions();
        viderChamps();
    }

    // Modifier une transaction
    private void modifierTransaction(Transaction transaction) {
        if (transaction == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une transaction à modifier.");
            return;
        }

        // Remplir le formulaire avec les données de la transaction sélectionnée
        remplirFormulaire(transaction);
        transactionSelectionnee = transaction;
    }

    // Enregistrer les modifications
    @FXML
    private void enregistrerModifications() {
        if (transactionSelectionnee == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une transaction à modifier.");
            return;
        }

        // Mettre à jour les données de la transaction sélectionnée
        transactionSelectionnee.setMontant(Double.parseDouble(txtMontant.getText()));
        transactionSelectionnee.setType(comboType.getSelectionModel().getSelectedItem());
        transactionSelectionnee.setDateTransaction(dateTransaction.getValue());

        // Enregistrer les modifications
        transactionService.modifierTransaction(transactionSelectionnee);

        // Rafraîchir la TableView
        chargerTransactions();
        viderChamps();
    }

    // Supprimer une transaction
    private void supprimerTransaction(Transaction transaction) {
        if (transaction == null) {
            afficherAlerte("Erreur", "Veuillez sélectionner une transaction à supprimer.");
            return;
        }

        // Supprimer la transaction sélectionnée
        transactionService.supprimerTransaction(transaction);

        // Rafraîchir la TableView
        chargerTransactions();
        viderChamps();
    }

    // Remplir le formulaire avec les données de la transaction sélectionnée
    private void remplirFormulaire(Transaction transaction) {
        txtMontant.setText(String.valueOf(transaction.getMontant()));
        comboType.getSelectionModel().select(transaction.getType());
        dateTransaction.setValue(transaction.getDateTransaction());
    }

    // Vider les champs du formulaire
    private void viderChamps() {
        txtMontant.clear();
        comboType.getSelectionModel().clearSelection();
        dateTransaction.setValue(null);
        transactionSelectionnee = null;
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