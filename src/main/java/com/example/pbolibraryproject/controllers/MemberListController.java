package com.example.pbolibraryproject.controllers;

import com.example.pbolibraryproject.models.Member;
import com.example.pbolibraryproject.service.LoanService;
import com.example.pbolibraryproject.service.MemberService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.Insets;

public class MemberListController {

    @FXML private TableView<Member> membersTable;
    @FXML private TableColumn<Member, String> colMemberId;
    @FXML private TableColumn<Member, String> colName;
    @FXML private TableColumn<Member, String> colEmail;
    @FXML private TableColumn<Member, String> colPhone;
    @FXML private TableColumn<Member, String> colJoinDate;
    @FXML private TableColumn<Member, Void> colActions;
    @FXML private TextField searchField;
    @FXML private Label totalMembersLabel;

    private MemberService memberService;
    private LoanService loanService;
    private ObservableList<Member> membersList;

    @FXML
    public void initialize() {
        memberService = new MemberService();
        loanService = new LoanService();
        membersList = FXCollections.observableArrayList();

        setupTableColumns();
        loadMembers();
        setupSearch();
    }

    private void setupTableColumns() {
        colMemberId.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMemberId()));
        colName.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colEmail.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colPhone.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));
        colJoinDate.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getJoinDate() != null ?
                cellData.getValue().getJoinDate().toString() : ""));

        setupActionColumn();
    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.getStyleClass().add("secondary-button");
                deleteButton.getStyleClass().add("secondary-button");

                editButton.setOnAction(event -> {
                    Member member = getTableView().getItems().get(getIndex());
                    editMember(member);
                });

                deleteButton.setOnAction(event -> {
                    Member member = getTableView().getItems().get(getIndex());
                    deleteMember(member);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMembers(newValue);
        });
    }

    private void loadMembers() {
        membersList = FXCollections.observableArrayList(memberService.getAllMembers());
        membersTable.setItems(membersList);
        updateSummary();
    }

    private void filterMembers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            membersTable.setItems(membersList);
        } else {
            ObservableList<Member> filtered = FXCollections.observableArrayList();
            for (Member member : membersList) {
                if (member.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    member.getMemberId().toLowerCase().contains(searchText.toLowerCase())) {
                    filtered.add(member);
                }
            }
            membersTable.setItems(filtered);
        }
        updateSummary();
    }

    @FXML
    private void addMember() {
        showMemberDialog(null);
    }

    private void editMember(Member member) {
        showMemberDialog(member);
    }

    private void showMemberDialog(Member member) {
        Dialog<Member> dialog = new Dialog<>();
        dialog.setTitle(member == null ? "Add New Member" : "Edit Member");
        dialog.setHeaderText(member == null ? "Enter member details" : "Update member details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        DatePicker joinDatePicker = new DatePicker();
        CheckBox activeCheckBox = new CheckBox();

        if (member != null) {
            idField.setText(member.getId());
            idField.setDisable(true);
            nameField.setText(member.getName());
            emailField.setText(member.getEmail());
            phoneField.setText(member.getPhone());
            joinDatePicker.setValue(member.getJoinDate());
            activeCheckBox.setSelected(member.isActive());
        } else {
            idField.setText(memberService.generateMemberId());
            idField.setDisable(true);
            joinDatePicker.setValue(java.time.LocalDate.now());
            activeCheckBox.setSelected(true);
        }

        grid.add(new Label("Member ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Join Date:"), 0, 4);
        grid.add(joinDatePicker, 1, 4);
        grid.add(new Label("Active:"), 0, 5);
        grid.add(activeCheckBox, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Add validation
        javafx.scene.Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            String validationError = validateMemberInput(
                nameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                joinDatePicker.getValue(),
                member != null ? member.getId() : null
            );

            if (validationError != null) {
                event.consume();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Validation Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText(validationError);
                alert.showAndWait();
            }
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Member newMember = new Member(
                    idField.getText(),
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    phoneField.getText().trim(),
                    joinDatePicker.getValue()
                );
                newMember.setActive(activeCheckBox.isSelected());
                return newMember;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (member == null) {
                memberService.addMember(result);
            } else {
                memberService.updateMember(result);
            }
            loadMembers();
        });
    }

    private void deleteMember(Member member) {

        // 🔍 CEK PINJAMAN AKTIF
        if (loanService.memberHasActiveLoan(member.getMemberId())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Member Failed");
            alert.setHeaderText("Member masih meminjam buku");
            alert.setContentText(
                    "Member tidak dapat dihapus karena masih memiliki\n" +
                            "buku yang belum dikembalikan."
            );
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Member");
        alert.setHeaderText("Delete " + member.getName() + "?");
        alert.setContentText("Are you sure you want to delete this member?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                memberService.deleteMember(member);
                loadMembers();
            }
        });
    }

    private void updateSummary() {
        totalMembersLabel.setText("Total Members: " + membersTable.getItems().size());
    }

    private String validateMemberInput(String name, String email, String phone,
                                      java.time.LocalDate joinDate, String currentMemberId) {
        // Validate name
        if (name == null || name.trim().isEmpty()) {
            return "Name is required and cannot be empty.";
        }

        if (name.trim().length() < 2) {
            return "Name must be at least 2 characters long.";
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            return "Email is required and cannot be empty.";
        }

        if (!isValidEmail(email.trim())) {
            return "Please enter a valid email address.\nExample: user@example.com";
        }

        // Check for duplicate email
        if (memberService.isEmailExists(email.trim(), currentMemberId)) {
            return "This email address is already registered by another member.";
        }

        // Validate phone (optional, but if provided must be valid)
        if (phone != null && !phone.trim().isEmpty() && !isValidPhone(phone.trim())) {
            return "Please enter a valid phone number.\nExample: +1234567890 or 081234567890";
        }

        // Validate join date
        if (joinDate == null) {
            return "Join date is required.";
        }

        if (joinDate.isAfter(java.time.LocalDate.now())) {
            return "Join date cannot be in the future.";
        }

        return null;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        // Accept phone numbers with or without + prefix, spaces, dashes, or parentheses
        String phoneRegex = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,9}$";
        return phone.matches(phoneRegex);
    }
}
