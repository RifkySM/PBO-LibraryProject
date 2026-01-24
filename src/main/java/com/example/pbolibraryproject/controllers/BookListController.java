package com.example.pbolibraryproject.controllers;

import com.example.pbolibraryproject.models.Book;
import com.example.pbolibraryproject.service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.geometry.Insets;

public class BookListController {

    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, String> colBookId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, String> colIsbn;
    @FXML private TableColumn<Book, String> colPublisher;
    @FXML private TableColumn<Book, String> colYear;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TableColumn<Book, Void> colActions;
    @FXML private TextField searchField;
    @FXML private Label totalBooksLabel;
    @FXML private Label availableBooksLabel;
    @FXML private Label borrowedBooksLabel;

    private BookService bookService;
    private ObservableList<Book> booksList;

    @FXML
    public void initialize() {
        bookService = new BookService();
        booksList = FXCollections.observableArrayList();

        setupTableColumns();
        loadBooks();
        setupSearch();
    }

    private void setupTableColumns() {
        colBookId.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBookId()));
        colTitle.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        colAuthor.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAuthor()));
        colIsbn.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIsbn()));
        colPublisher.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPublisher()));
        colYear.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getYear() > 0 ?
                String.valueOf(cellData.getValue().getYear()) : ""));
        colStatus.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus()));

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
                    Book book = getTableView().getItems().get(getIndex());
                    editBook(book);
                });

                deleteButton.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    deleteBook(book);
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
            filterBooks(newValue);
        });
    }

    private void loadBooks() {
        booksList = FXCollections.observableArrayList(bookService.getAllBooks());
        booksTable.setItems(booksList);
        updateSummary();
    }

    private void filterBooks(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            booksTable.setItems(booksList);
        } else {
            ObservableList<Book> filtered = FXCollections.observableArrayList();
            for (Book book : booksList) {
                if (book.getTitle().toLowerCase().contains(searchText.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(searchText.toLowerCase()) ||
                    book.getBookId().toLowerCase().contains(searchText.toLowerCase())) {
                    filtered.add(book);
                }
            }
            booksTable.setItems(filtered);
        }
        updateSummary();
    }

    @FXML
    private void addBook() {
        showBookDialog(null);
    }

    private void editBook(Book book) {
        showBookDialog(book);
    }

    private void showBookDialog(Book book) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle(book == null ? "Add New Book" : "Edit Book");
        dialog.setHeaderText(book == null ? "Enter book details" : "Update book details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField isbnField = new TextField();
        TextField publisherField = new TextField();
        TextField yearField = new TextField();
        TextField stockField = new TextField();

        if (book != null) {
            idField.setText(book.getBookId());
            idField.setDisable(true);
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            isbnField.setText(book.getIsbn());
            publisherField.setText(book.getPublisher());
            yearField.setText(book.getYear() > 0 ? String.valueOf(book.getYear()) : "");
            stockField.setText(String.valueOf(book.getStock()));
        } else {
            idField.setText(bookService.generateBookId());
            idField.setDisable(true);
        }

        grid.add(new Label("Book ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Author:"), 0, 2);
        grid.add(authorField, 1, 2);
        grid.add(new Label("ISBN:"), 0, 3);
        grid.add(isbnField, 1, 3);
        grid.add(new Label("Publisher:"), 0, 4);
        grid.add(publisherField, 1, 4);
        grid.add(new Label("Year:"), 0, 5);
        grid.add(yearField, 1, 5);
        grid.add(new Label("Stock:"), 0, 6);
        grid.add(stockField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    int year = yearField.getText().isEmpty() ? 0 : Integer.parseInt(yearField.getText());
                    int stock = Integer.parseInt(stockField.getText());

                    return new Book(
                        idField.getText(),
                        titleField.getText(),
                        authorField.getText(),
                        isbnField.getText(),
                        publisherField.getText(),
                        year,
                        stock
                    );
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText("Please enter valid numbers for Year and Stock");
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result != null) {
                if (book == null) {
                    bookService.addBook(result);
                } else {
                    bookService.updateBook(result);
                }
                loadBooks();
            }
        });
    }

    private void deleteBook(Book book) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Book");
        alert.setHeaderText("Delete " + book.getTitle() + "?");
        alert.setContentText("Are you sure you want to delete this book?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                bookService.deleteBook(book);
                loadBooks();
            }
        });
    }

    private void updateSummary() {
        int total = booksTable.getItems().size();
        long available = booksList.stream().filter(Book::isAvailable).count();
        long borrowed = total - available;

        totalBooksLabel.setText("Total Books: " + total);
        availableBooksLabel.setText("Available: " + available);
        borrowedBooksLabel.setText("Borrowed: " + borrowed);
    }
}
