package com.example.pbolibraryproject.service;


import com.example.pbolibraryproject.models.Book;
import com.example.pbolibraryproject.models.Loan;
import com.example.pbolibraryproject.models.Member;
import com.example.pbolibraryproject.util.CSVUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanService {
    private static final String CSV_FILE = "loans.csv";
    private List<Loan> loans;
    private BookService bookService;
    private MemberService memberService;

    public LoanService() {
        this.loans = new ArrayList<>();
        this.bookService = new BookService();
        this.memberService = new MemberService();
        loadFromCSV();
    }

    private void loadFromCSV() {
        if (!CSVUtil.fileExists(CSV_FILE)) {
            loadSampleData();
            saveToCSV();
        } else {
            List<String[]> data = CSVUtil.readCSV(CSV_FILE);
            for (String[] row : data) {
                if (row.length >= 7) {
                    String loanId = row[0];
                    String bookId = row[1];
                    String memberId = row[2];
                    LocalDate loanDate = LocalDate.parse(row[3]);
                    LocalDate dueDate = LocalDate.parse(row[4]);
                    String returnDateStr = row[5];
                    boolean isReturned = Boolean.parseBoolean(row[6]);

                    Book book = bookService.getBookById(bookId);
                    Member member = memberService.getMemberById(memberId);

                    if (book != null && member != null) {
                        Loan loan = new Loan(loanId, book, member, loanDate);
                        loan.setDueDate(dueDate);
                        if (!returnDateStr.isEmpty() && !returnDateStr.equals("null")) {
                            loan.setReturnDate(LocalDate.parse(returnDateStr));
                        }
                        loan.setReturned(isReturned);
                        loans.add(loan);
                    }
                }
            }
        }
    }

    private void saveToCSV() {
        List<String[]> data = new ArrayList<>();
        for (Loan loan : loans) {
            String[] row = {
                loan.getLoanId(),
                loan.getBook().getBookId(),
                loan.getMember().getId(),
                loan.getLoanDate().toString(),
                loan.getDueDate().toString(),
                loan.getReturnDate() != null ? loan.getReturnDate().toString() : "",
                String.valueOf(loan.getIsReturned())
            };
            data.add(row);
        }
        CSVUtil.writeCSV(CSV_FILE, data);
    }

    private void loadSampleData() {
        // Sample loans
        List<Book> books = bookService.getAllBooks();
        List<Member> members = memberService.getAllMembers();

        if (!books.isEmpty() && !members.isEmpty()) {
            loans.add(new Loan("L001", books.get(0), members.get(0), LocalDate.now().minusDays(5)));
            loans.add(new Loan("L002", books.get(1), members.get(1), LocalDate.now().minusDays(10)));
        }
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public Loan getLoanById(String loanId) {
        return loans.stream()
                .filter(l -> l.getLoanId().equals(loanId))
                .findFirst()
                .orElse(null);
    }

    public boolean createLoan(Loan loan) {
        boolean success = loan.processTransaction();
        if (success) {
            loans.add(loan);
            saveToCSV();
            // Save updated book stock to CSV
            bookService.updateBook(loan.getBook());
        }
        return success;
    }

    public void returnLoan(Loan loan) {
        loan.cancelTransaction();
        saveToCSV();
        // Save updated book stock to CSV
        bookService.updateBook(loan.getBook());
    }

    public void deleteLoan(Loan loan) {
        loans.removeIf(l -> l.getLoanId().equals(loan.getLoanId()));
        saveToCSV();
    }

    public List<Loan> getActiveLoan() {
        List<Loan> active = new ArrayList<>();
        for (Loan loan : loans) {
            if (!loan.getIsReturned()) {
                active.add(loan);
            }
        }
        return active;
    }

    public List<Loan> getReturnedLoans() {
        List<Loan> returned = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getIsReturned()) {
                returned.add(loan);
            }
        }
        return returned;
    }

    public List<Loan> getOverdueLoans(int dueDays) {
        List<Loan> overdue = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (Loan loan : loans) {
            if (!loan.getIsReturned() &&
                loan.getLoanDate().plusDays(dueDays).isBefore(now)) {
                overdue.add(loan);
            }
        }
        return overdue;
    }

    public List<Loan> getLoansByMember(Member member) {
        List<Loan> memberLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getMember().getId().equals(member.getId())) {
                memberLoans.add(loan);
            }
        }
        return memberLoans;
    }

    public List<Loan> getLoansByBook(Book book) {
        List<Loan> bookLoans = new ArrayList<>();
        for (Loan loan : loans) {
            if (loan.getBook().getBookId().equals(book.getBookId())) {
                bookLoans.add(loan);
            }
        }
        return bookLoans;
    }

    public String generateLoanId() {
        int maxId = 0;
        for (Loan loan : loans) {
            String id = loan.getLoanId().substring(1); // Remove 'L' prefix
            int numId = Integer.parseInt(id);
            if (numId > maxId) {
                maxId = numId;
            }
        }
        return String.format("L%03d", maxId + 1);
    }

    public boolean memberHasActiveLoan(String memberId) {
        for (Loan loan : getAllLoans()) {
            if (
                    loan.getMember().getMemberId().equals(memberId) &&
                            !loan.getIsReturned()
            ) {
                return true;
            }
        }
        return false;
    }
}
