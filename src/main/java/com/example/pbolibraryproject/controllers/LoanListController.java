package com.example.pbolibraryproject.controllers;

import com.example.pbolibraryproject.models.Book;
import com.example.pbolibraryproject.models.Loan;
import com.example.pbolibraryproject.models.Member;
import com.example.pbolibraryproject.service.BookService;
import com.example.pbolibraryproject.service.LoanService;
import com.example.pbolibraryproject.service.MemberService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import java.time.LocalDate;

public class LoanListController {

    @FXML private TableView<Loan> loansTable;
    @FXML private TableColumn<Loan, String> colLoanId;
    @FXML private TableColumn<Loan, String> colMemberName;
    @FXML private TableColumn<Loan, String> colBookTitle;
    @FXML private TableColumn<Loan, String> colLoanDate;
    @FXML private TableColumn<Loan, String> colDueDate;
    @FXML private TableColumn<Loan, String> colReturnDate;
    @FXML private TableColumn<Loan, String> colStatus;
    @FXML private TableColumn<Loan, Void> colActions;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private Label totalLoansLabel;
    @FXML private Label activeLoansLabel;
    @FXML private Label overdueLoansLabel;
    @FXML private Label returnedLoansLabel;

    private LoanService loanService;
    private BookService bookService;
    private MemberService memberService;
    private ObservableList<Loan> loansList;

    @FXML
    public void initialize() {
        loanService = new LoanService();
        bookService = new BookService();
        memberService = new MemberService();
        loansList = FXCollections.observableArrayList();

        setupFilterComboBox();
        setupTableColumns();
        loadLoans();
        setupSearch();
    }

    private void setupFilterComboBox() {
        filterComboBox.setItems(FXCollections.observableArrayList(
            "All", "Active", "Overdue", "Returned"
        ));
        filterComboBox.setValue("All");
        filterComboBox.setOnAction(event -> filterLoans());
    }

    private void setupTableColumns() {
        colLoanId.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLoanId()));
        colMemberName.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMember().getName()));
        colBookTitle.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBook().getTitle()));
        colLoanDate.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getLoanDate() != null ?
                cellData.getValue().getLoanDate().toString() : ""));
        colDueDate.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDueDate() != null ?
                cellData.getValue().getDueDate().toString() : ""));
        colReturnDate.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getReturnDate() != null ?
                cellData.getValue().getReturnDate().toString() : ""));
        colStatus.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

        setupActionColumn();
    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button returnButton = new Button("Return");
            private final Button viewButton = new Button("View");

            {
                returnButton.getStyleClass().add("primary-button");
                viewButton.getStyleClass().add("secondary-button");

                returnButton.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    returnLoan(loan);
                });

                viewButton.setOnAction(event -> {
                    Loan loan = getTableView().getItems().get(getIndex());
                    viewLoan(loan);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Loan loan = getTableView().getItems().get(getIndex());
                    javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5);

                    // Only show return button if loan is not returned
                    if (!loan.getIsReturned()) {
                        buttons.getChildren().add(returnButton);
                    }
                    buttons.getChildren().add(viewButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterLoans();
        });
    }

    private void loadLoans() {
        loansList = FXCollections.observableArrayList(loanService.getAllLoans());
        loansTable.setItems(loansList);
        updateSummary();
    }

    private void filterLoans() {
        String searchText = searchField.getText();
        String statusFilter = filterComboBox.getValue();

        ObservableList<Loan> filtered = FXCollections.observableArrayList();

        for (Loan loan : loansList) {
            boolean matchesSearch = searchText == null || searchText.isEmpty() ||
                    loan.getLoanId().toLowerCase().contains(searchText.toLowerCase()) ||
                    loan.getMember().getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    loan.getBook().getTitle().toLowerCase().contains(searchText.toLowerCase());

            boolean matchesStatus = statusFilter.equals("All") ||
                    (statusFilter.equals("Active") && !loan.getIsReturned() && !loan.isOverdue()) ||
                    (statusFilter.equals("Overdue") && loan.isOverdue()) ||
                    (statusFilter.equals("Returned") && loan.getIsReturned());

            if (matchesSearch && matchesStatus) {
                filtered.add(loan);
            }
        }

        loansTable.setItems(filtered);
        updateSummary();
    }

    @FXML
    private void createLoan() {
        showLoanDialog();
    }

    private void showLoanDialog() {
        Dialog<Loan> dialog = new Dialog<>();
        dialog.setTitle("Create New Loan");
        dialog.setHeaderText("Enter loan details");

        ButtonType saveButtonType = new ButtonType("Create Loan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        idField.setText(loanService.generateLoanId());
        idField.setDisable(true);

        ComboBox<Member> memberCombo = new ComboBox<>();
        memberCombo.setItems(FXCollections.observableArrayList(memberService.getAllMembers()));
        memberCombo.setPromptText("Select Member");

        ComboBox<Book> bookCombo = new ComboBox<>();
        bookCombo.setItems(FXCollections.observableArrayList(bookService.getAvailableBooks()));
        bookCombo.setPromptText("Select Book");

        DatePicker loanDatePicker = new DatePicker(LocalDate.now());
        DatePicker dueDatePicker = new DatePicker(LocalDate.now().plusDays(14));

        grid.add(new Label("Loan ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Member:"), 0, 1);
        grid.add(memberCombo, 1, 1);
        grid.add(new Label("Book:"), 0, 2);
        grid.add(bookCombo, 1, 2);
        grid.add(new Label("Loan Date:"), 0, 3);
        grid.add(loanDatePicker, 1, 3);
        grid.add(new Label("Due Date:"), 0, 4);
        grid.add(dueDatePicker, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (memberCombo.getValue() == null || bookCombo.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText("Please select both member and book");
                    alert.showAndWait();
                    return null;
                }

                Loan loan = new Loan(
                    idField.getText(),
                    bookCombo.getValue(),
                    memberCombo.getValue(),
                    loanDatePicker.getValue()
                );
                loan.setDueDate(dueDatePicker.getValue());
                return loan;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result != null) {
                boolean success = loanService.createLoan(result);
                if (success) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Loan Created Successfully");
                    successAlert.setContentText("Book: " + result.getBook().getTitle() + "\nMember: " + result.getMember().getName());
                    successAlert.showAndWait();
                    loadLoans();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Loan Creation Failed");
                    errorAlert.setHeaderText("Unable to create loan");
                    String errorMessage = "";
                    if (result.getBook().getStock() <= 0) {
                        errorMessage = "The selected book is out of stock.";
                    } else if (!result.getMember().isActive()) {
                        errorMessage = "The selected member is not active.";
                    } else {
                        errorMessage = "Stock is empty or member is inactive.";
                    }
                    errorAlert.setContentText(errorMessage);
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private void returnLoan(Loan loan) {
        if (loan.getIsReturned()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Already Returned");
            alert.setHeaderText("This book has already been returned");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Return Book");
        alert.setHeaderText("Return: " + loan.getBook().getTitle());
        alert.setContentText("Member: " + loan.getMember().getName() + "\nDue Date: " + loan.getDueDate());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                loanService.returnLoan(loan);
                loadLoans();
            }
        });
    }

    private void viewLoan(Loan loan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Loan Details");
        alert.setHeaderText("Loan ID: " + loan.getLoanId());

        String content = "Member: " + loan.getMember().getName() + "\n" +
                        "Book: " + loan.getBook().getTitle() + "\n" +
                        "Loan Date: " + loan.getLoanDate() + "\n" +
                        "Due Date: " + loan.getDueDate() + "\n" +
                        "Return Date: " + (loan.getReturnDate() != null ? loan.getReturnDate() : "Not returned") + "\n" +
                        "Status: " + loan.getStatus();

        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateSummary() {
        int total = loansTable.getItems().size();
        long active = loansTable.getItems().stream()
                .filter(loan -> !loan.getIsReturned() && !loan.isOverdue())
                .count();
        long overdue = loansTable.getItems().stream()
                .filter(Loan::isOverdue)
                .count();
        long returned = loansTable.getItems().stream()
                .filter(Loan::getIsReturned)
                .count();

        totalLoansLabel.setText("Total Loans: " + total);
        activeLoansLabel.setText("Active: " + active);
        overdueLoansLabel.setText("Overdue: " + overdue);
        returnedLoansLabel.setText("Returned: " + returned);
    }
}
