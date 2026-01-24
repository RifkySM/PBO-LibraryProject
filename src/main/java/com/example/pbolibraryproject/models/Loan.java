package com.example.pbolibraryproject.models;

import java.time.LocalDate;

public class Loan implements TransactionProcess{
    private String loanId;
    private Book book;
    private Member member;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;

    public Loan(String loanId, Book book, Member member, LocalDate loanDate) {
        this.loanId = loanId;
        this.book = book;
        this.member = member;
        this.loanDate = loanDate;
        this.dueDate = loanDate.plusDays(14); // Default 14 days loan period
        this.returnDate = null;
        this.isReturned = false;
    }

    @Override
    public void processTransaction() {
        if (book.getStock() > 0 && member.isActive()) {
            book.setStock(book.getStock() - 1);
            System.out.println("Loan processed for: " + book.getTitle());
        } else {
            System.out.println("Transaction Failed: Stock empty or Member inactive.");
        }
    }


    @Override
    public void cancelTransaction() {
        if (!isReturned) {
            book.setStock(book.getStock() + 1);
            this.isReturned = true;
            this.returnDate = LocalDate.now();
            System.out.println("Book returned: " + book.getTitle());
        }
    }

    public String getLoanId() { return loanId; }
    public Book getBook() { return book; }
    public Member getMember() { return member; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public boolean getIsReturned() { return isReturned; }
    public void setReturned(boolean returned) { isReturned = returned; }

    public String getStatus() {
        if (isReturned) {
            return "Returned";
        } else if (LocalDate.now().isAfter(dueDate)) {
            return "Overdue";
        } else {
            return "Active";
        }
    }

    public boolean isOverdue() {
        return !isReturned && LocalDate.now().isAfter(dueDate);
    }
}
