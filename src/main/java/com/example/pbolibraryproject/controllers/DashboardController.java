package com.example.pbolibraryproject.controllers;

import com.example.pbolibraryproject.models.Loan;
import com.example.pbolibraryproject.service.BookService;
import com.example.pbolibraryproject.service.LoanService;
import com.example.pbolibraryproject.service.MemberService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private Label dateLabel;
    @FXML private Label totalBooksLabel;
    @FXML private Label availableBooksLabel;
    @FXML private Label borrowedBooksLabel;
    @FXML private Label totalMembersLabel;
    @FXML private Label activeMembersLabel;
    @FXML private Label inactiveMembersLabel;
    @FXML private Label totalLoansLabel;
    @FXML private Label activeLoansLabel;
    @FXML private Label overdueLoansLabel;
    @FXML private TableView<Loan> recentActivityTable;
    @FXML private TableColumn<Loan, String> colActivityType;
    @FXML private TableColumn<Loan, String> colActivityDescription;
    @FXML private TableColumn<Loan, String> colActivityDate;

    private BookService bookService;
    private MemberService memberService;
    private LoanService loanService;

    @FXML
    public void initialize() {
        bookService = new BookService();
        memberService = new MemberService();
        loanService = new LoanService();

        setupRecentActivityTable();
        loadStatistics();
        loadRecentActivity();
        updateDate();
    }

    private void updateDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        dateLabel.setText("Today: " + today.format(formatter));
    }

    private void loadStatistics() {
        // Books Statistics
        int totalBooks = bookService.getAllBooks().size();
        long availableBooks = bookService.getAvailableBooks().size();
        long borrowedBooks = totalBooks - availableBooks;

        totalBooksLabel.setText(String.valueOf(totalBooks));
        availableBooksLabel.setText(String.valueOf(availableBooks));
        borrowedBooksLabel.setText(String.valueOf(borrowedBooks));

        // Members Statistics
        int totalMembers = memberService.getAllMembers().size();
        long activeMembers = memberService.getAllMembers().stream()
                .filter(com.example.pbolibraryproject.models.Member::isActive)
                .count();
        long inactiveMembers = totalMembers - activeMembers;

        totalMembersLabel.setText(String.valueOf(totalMembers));
        activeMembersLabel.setText(String.valueOf(activeMembers));
        inactiveMembersLabel.setText(String.valueOf(inactiveMembers));

        // Loans Statistics
        int totalLoans = loanService.getAllLoans().size();
        int activeLoans = loanService.getActiveLoan().size();
        int overdueLoans = loanService.getOverdueLoans(14).size(); // 14 days due period

        totalLoansLabel.setText(String.valueOf(totalLoans));
        activeLoansLabel.setText(String.valueOf(activeLoans));
        overdueLoansLabel.setText(String.valueOf(overdueLoans));
    }

    private void setupRecentActivityTable() {
        colActivityType.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            String type = loan.getIsReturned() ? "Return" : "Loan";
            return new javafx.beans.property.SimpleStringProperty(type);
        });

        colActivityDescription.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            String description = loan.getBook().getTitle() + " - " + loan.getMember().getName();
            return new javafx.beans.property.SimpleStringProperty(description);
        });

        colActivityDate.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            LocalDate activityDate = loan.getIsReturned() && loan.getReturnDate() != null
                ? loan.getReturnDate()
                : loan.getLoanDate();
            return new javafx.beans.property.SimpleStringProperty(activityDate.toString());
        });
    }

    private void loadRecentActivity() {
        List<Loan> allLoans = loanService.getAllLoans();

        // Sort by most recent activity (return date if returned, loan date otherwise)
        List<Loan> recentLoans = allLoans.stream()
                .sorted((loan1, loan2) -> {
                    LocalDate date1 = loan1.getIsReturned() && loan1.getReturnDate() != null
                        ? loan1.getReturnDate()
                        : loan1.getLoanDate();
                    LocalDate date2 = loan2.getIsReturned() && loan2.getReturnDate() != null
                        ? loan2.getReturnDate()
                        : loan2.getLoanDate();
                    return date2.compareTo(date1); // Most recent first
                })
                .limit(10) // Show only the 10 most recent activities
                .collect(Collectors.toList());

        ObservableList<Loan> activityList = FXCollections.observableArrayList(recentLoans);
        recentActivityTable.setItems(activityList);
    }

    public void refreshDashboard() {
        loadStatistics();
        loadRecentActivity();
        updateDate();
    }

    @FXML
    private void navigateToBooks() {
        MainController mainController = MainController.getInstance();
        if (mainController != null) {
            mainController.showBooks();
        }
    }

    @FXML
    private void navigateToMembers() {
        MainController mainController = MainController.getInstance();
        if (mainController != null) {
            mainController.showMembers();
        }
    }

    @FXML
    private void navigateToLoans() {
        MainController mainController = MainController.getInstance();
        if (mainController != null) {
            mainController.showLoans();
        }
    }
}
