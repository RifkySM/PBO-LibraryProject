package com.example.pbolibraryproject.service;

import com.example.pbolibraryproject.models.Book;
import com.example.pbolibraryproject.util.CSVUtil;

import java.util.ArrayList;
import java.util.List;

public class BookService {
    private static final String CSV_FILE = "books.csv";
    private List<Book> books;

    public BookService() {
        this.books = new ArrayList<>();
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
                    Book book = new Book(row[0], row[1], row[2], row[3], row[4],
                        Integer.parseInt(row[5]), Integer.parseInt(row[6]));
                    books.add(book);
                }
            }
        }
    }

    private void saveToCSV() {
        List<String[]> data = new ArrayList<>();
        for (Book book : books) {
            String[] row = {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublisher(),
                String.valueOf(book.getYear()),
                String.valueOf(book.getStock())
            };
            data.add(row);
        }
        CSVUtil.writeCSV(CSV_FILE, data);
    }

    private void loadSampleData() {
        books.add(new Book("B001", "The Great Gatsby", "F. Scott Fitzgerald",
            "978-0-7432-7356-5", "Scribner", 1925, 5));
        books.add(new Book("B002", "To Kill a Mockingbird", "Harper Lee",
            "978-0-06-112008-4", "J.B. Lippincott & Co.", 1960, 3));
        books.add(new Book("B003", "1984", "George Orwell",
            "978-0-452-28423-4", "Secker & Warburg", 1949, 7));
        books.add(new Book("B004", "Pride and Prejudice", "Jane Austen",
            "978-0-14-143951-8", "T. Egerton", 1813, 4));
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    public Book getBookById(String bookId) {
        return books.stream()
                .filter(b -> b.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    public void addBook(Book book) {
        books.add(book);
        saveToCSV();
    }

    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId().equals(updatedBook.getBookId())) {
                books.set(i, updatedBook);
                break;
            }
        }
        saveToCSV();
    }

    public void deleteBook(Book book) {
        books.removeIf(b -> b.getBookId().equals(book.getBookId()));
        saveToCSV();
    }

    public List<Book> searchBooks(String query) {
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(lowerQuery) ||
                book.getAuthor().toLowerCase().contains(lowerQuery) ||
                book.getBookId().toLowerCase().contains(lowerQuery)) {
                results.add(book);
            }
        }

        return results;
    }

    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : books) {
            if (book.getStock() > 0) {
                available.add(book);
            }
        }
        return available;
    }

    public String generateBookId() {
        int maxId = 0;
        for (Book book : books) {
            String id = book.getBookId().substring(1); // Remove 'B' prefix
            int numId = Integer.parseInt(id);
            if (numId > maxId) {
                maxId = numId;
            }
        }
        return String.format("B%03d", maxId + 1);
    }
}
