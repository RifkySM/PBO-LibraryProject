package com.example.pbolibraryproject.models;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private int year;
    private int stock;
    private int maxStock;

    public Book(String bookId, String title, String author, String isbn, String publisher, int year, int stock, int maxStock) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.year = year;
        this.stock = stock;
        this.maxStock = maxStock;
    }

    public Book(String bookId, String title, String author, int stock, int maxStock) {
        this(bookId, title, author, "", "", 0, stock, maxStock);
    }

    // Getters and Setters
    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getMaxStock() { return maxStock; }
    public void setMaxStock(int maxStock) { this.maxStock = maxStock; }

    public boolean isAvailable() {
        return stock > 0;
    }

    public String getStatus() {
        return stock > 0 ? "Available" : "Out of Stock";
    }

    @Override
    public String toString() {
        return title + " (Stock: " + stock + "/" + maxStock + ")";
    }
}
