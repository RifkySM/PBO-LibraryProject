package com.example.pbolibraryproject.controllers;

import com.example.pbolibraryproject.models.Member;
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
    private ObservableList<Member> membersList;

    @FXML
    public void initialize() {
        memberService = new MemberService();
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

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Member newMember = new Member(
                    idField.getText(),
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
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
        // TODO: Show confirmation dialog
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
}
