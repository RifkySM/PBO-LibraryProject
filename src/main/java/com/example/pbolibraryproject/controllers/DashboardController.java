package com.example.pbolibraryproject.controllers;

import com.example.pbolibraryproject.service.BookService;
import com.example.pbolibraryproject.service.LoanService;
import com.example.pbolibraryproject.service.MemberService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    @FXML private TableView<?> recentActivityTable;
    @FXML private TableColumn<?, ?> colActivityType;
    @FXML private TableColumn<?, ?> colActivityDescription;
    @FXML private TableColumn<?, ?> colActivityDate;

    private BookService bookService;
    private MemberService memberService;
    private LoanService loanService;

    @FXML
    public void initialize() {
        bookService = new BookService();
        memberService = new MemberService();
        loanService = new LoanService();

        loadStatistics();
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

    public void refreshDashboard() {
        loadStatistics();
        updateDate();
    }

    @FXML
    private void navigateToBooks() {
        // This will be handled by the main controller
        System.out.println("Navigate to Books");
    }

    @FXML
    private void navigateToMembers() {
        // This will be handled by the main controller
        System.out.println("Navigate to Members");
    }

    @FXML
    private void navigateToLoans() {
        // This will be handled by the main controller
        System.out.println("Navigate to Loans");
    }
}
