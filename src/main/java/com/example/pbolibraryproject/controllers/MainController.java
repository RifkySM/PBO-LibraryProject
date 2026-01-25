package com.example.pbolibraryproject.controllers;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

    private static MainController instance;

    @FXML private VBox sidebar;
    @FXML private StackPane contentStack;
    @FXML private Label pageTitle;
    @FXML private Button btnDashboard;
    @FXML private Button btnMembers;
    @FXML private Button btnBooks;
    @FXML private Button btnLoans;
    @FXML private Button btnToggle;

    private boolean isSidebarCollapsed = false;
    private double sidebarExpandedWidth = 250;
    private double sidebarCollapsedWidth = 70;

    @FXML
    public void initialize() {
        instance = this;
        showDashboard();
    }

    public static MainController getInstance() {
        return instance;
    }

    @FXML
    public void showDashboard() {
        loadPage("dashboard.fxml", "Dashboard");
        setActiveButton(btnDashboard);
    }

    @FXML
    public void showMembers() {
        loadPage("member-list.fxml", "Members");
        setActiveButton(btnMembers);
    }

    @FXML
    public void showBooks() {
        loadPage("book-list.fxml", "Books");
        setActiveButton(btnBooks);
    }

    @FXML
    public void showLoans() {
        loadPage("loan-list.fxml", "Loans");
        setActiveButton(btnLoans);
    }

    @FXML
    private void toggleSidebar() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sidebar);

        if (isSidebarCollapsed) {
            // Expand sidebar
            sidebar.setPrefWidth(sidebarExpandedWidth);
            btnToggle.setText("◀");

            // Show button text
            btnDashboard.setText("Dashboard");
            btnMembers.setText("Members");
            btnBooks.setText("Books");
            btnLoans.setText("Loans");

            isSidebarCollapsed = false;
        } else {
            // Collapse sidebar
            sidebar.setPrefWidth(sidebarCollapsedWidth);
            btnToggle.setText("▶");

            // Hide button text (show only icons if you add them)
            btnDashboard.setText("D");
            btnMembers.setText("M");
            btnBooks.setText("B");
            btnLoans.setText("L");

            isSidebarCollapsed = true;
        }

        transition.play();
    }

    private void loadPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/pbolibraryproject/" + fxmlFile));
            Node page = loader.load();

            contentStack.getChildren().clear();
            contentStack.getChildren().add(page);
            pageTitle.setText(title);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading page: " + fxmlFile);
        }
    }

    private void setActiveButton(Button activeButton) {
        // Remove active class from all buttons
        btnDashboard.getStyleClass().remove("active");
        btnMembers.getStyleClass().remove("active");
        btnBooks.getStyleClass().remove("active");
        btnLoans.getStyleClass().remove("active");

        // Add active class to the clicked button
        if (!activeButton.getStyleClass().contains("active")) {
            activeButton.getStyleClass().add("active");
        }
    }
}
