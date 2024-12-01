package com.example.librarysystem;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.List;

public class MainController {

    // FXML elements
    @FXML
    private GridPane dynamicResults;
    @FXML
    private VBox SearchPage;
    @FXML
    private VBox addBookDialog;
    @FXML
    private VBox MoreInfoDialog;
    @FXML
    private VBox BorrowDialog;

    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label descriptionLabel;

    @FXML
    private Label BorrowBookLabel;

    @FXML
    private Button borrowButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addBookButton;
    @FXML
    private Button FindBook;
    @FXML
    private Button confirmBookButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button BookBorrowConfirmButton;

    @FXML
    private TextField searchField;
    @FXML
    private TextField bookNameField;
    @FXML
    private TextField bookAuthorField;
    @FXML
    private TextField bookIsbnField;

    @FXML
    private TextField borrowerNameField;

    private final Library library = new Library();

    // Initialises the controller
    public void initialize() {
        library.loadBooksFromCSV();
        addBookButton.setOnAction(e -> showAddBookDialog());
        FindBook.setOnAction(e -> onFindButtonClick());
        confirmBookButton.setOnAction(e -> ConfirmBook());
        searchButton.setOnAction(e->SearchBook());
    }

    // Search for books based on the query entered in the search field
    @FXML
    private void onFindButtonClick() {
        addBookDialog.setVisible(false);
        SearchPage.setVisible(true);
        String query = searchField.getText().trim();
        dynamicResults.getChildren().clear();

        List<Book> results = library.filterBooks(query);

        if (results.isEmpty()) {
            Label noResultsLabel = new Label("No books found");
            dynamicResults.add(noResultsLabel, 0, 0);
        } else {
            // Display search results
            for (Book book : results) {
                onSearchBookButtonClick(book);
            }
        }
    }

    // Shows the book details dialog when more info is requested
    private void showDialog(String title, String author, String description) {
        titleLabel.setText(title);
        authorLabel.setText("Author: " + author);
        descriptionLabel.setText(description);
        MoreInfoDialog.setVisible(true);
        BorrowDialog.setVisible(false);
    }

    @FXML
    private void showAddBookDialog() {
        addBookDialog.setVisible(true);  // Show Add Book dialog
        SearchPage.setVisible(false);  // Hide the details dialog
        MoreInfoDialog.setVisible(false);
        System.out.print("addbook button");
    }

    @FXML
    private void clearFields() {
        // Clear input fields
        bookNameField.clear();
        bookAuthorField.clear();
        bookIsbnField.clear();
    }

    @FXML
    private void ConfirmBook() {
        System.out.println("confirm button clicked!");
        String bookName = bookNameField.getText().trim();
        String author = bookAuthorField.getText().trim();
        String isbn = bookIsbnField.getText().trim();

        if (bookName.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required!");
            return;
        }
        if (!isValidISBN(isbn)) {
            showAlert(Alert.AlertType.ERROR, "Invalid ISBN format. Please enter a valid 9-digit ISBN.");
            return;
        }

        library.addBook(bookName, author, isbn, true, "");
        library.sortBooksByTitle();
        onSearchBookButtonClick(new Book(bookName, author, isbn, true, ""));
        clearFields();
        closeAddBookDialog();
        dynamicResults.getChildren().clear();
    }

    private boolean isValidISBN(String isbn) {
        return isbn.matches("\\d{9}");
    }

    // Displays each book in the search results
    private void onSearchBookButtonClick(Book book) {
        Circle resultStatus = new Circle(10, book.isAvailable() ? Color.GREEN : Color.RED);
        Label resultLabel = new Label(book.getTitle());
        Button MoreInfoButton = new Button("More Info");
        MoreInfoButton.setOnAction(e -> handleMoreInfo(book.getTitle(), book.getAuthor(), book.getIsbn(), book.isAvailable()));

        int rowIndex = dynamicResults.getChildren().size() / 3;
        dynamicResults.add(resultStatus, 0, rowIndex);
        dynamicResults.add(resultLabel, 1, rowIndex);
        dynamicResults.add(MoreInfoButton, 2, rowIndex);
        adjustRowHeight(dynamicResults, rowIndex, 40);
    }

    //Refresh the list displayed on screen after borrow and return of books
    private void refreshSearchList(List<Book> books) {
        dynamicResults.getChildren().clear();

        for (Book book : books) {
            Circle resultStatus = new Circle(10, book.isAvailable() ? Color.GREEN : Color.RED);
            Label resultLabel = new Label(book.getTitle());
            Button moreInfoButton = new Button("More Info >");

            moreInfoButton.setOnAction(e -> handleMoreInfo(book.getTitle(), book.getAuthor(), book.getIsbn(), book.isAvailable()));

            int rowIndex = dynamicResults.getChildren().size() / 3;
            dynamicResults.add(resultStatus, 0, rowIndex);
            dynamicResults.add(resultLabel, 1, rowIndex);
            dynamicResults.add(moreInfoButton, 2, rowIndex);

            adjustRowHeight(dynamicResults, rowIndex, 40);
        }
    }

    //Setting for grid pane
    private void adjustRowHeight(GridPane gridPane, int rowIndex, double height) {
        while (gridPane.getRowConstraints().size() <= rowIndex) {
            gridPane.getRowConstraints().add(new RowConstraints());
        }
        // Set the preferred height for the row
        RowConstraints rowConstraints = gridPane.getRowConstraints().get(rowIndex);
        rowConstraints.setPrefHeight(height);
        rowConstraints.setMinHeight(height);
        rowConstraints.setMaxHeight(height);
    }

    // Opens the dialog to show more info about the selected book
    private void handleMoreInfo(String title, String author, String isbn, boolean isAvailable) {
        showDialog(title, author, "ISBN: " + isbn);
        borrowButton.setOnAction(e -> onBorrowButtonClick(new Book(title, author, isbn, isAvailable, "")));
        returnButton.setOnAction(e -> onReturnButtonClick(new Book(title, author, isbn, isAvailable, "")));
        deleteButton.setOnAction(e -> onDeleteButtonClick(title));
    }

    // Closes the Add Book dialog
    private void closeAddBookDialog() {
        addBookDialog.setVisible(false);  // Hide Add Book dialog after adding the book
        SearchPage.setVisible(true);
        showAlert(Alert.AlertType.INFORMATION, "Book Added Successfully!");
    }

    // Displays an alert message
    public void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.show();
    }

    @FXML
    private void SearchBook() {
        System.out.println("Search book clicked");
        String query = searchField.getText().trim();

        dynamicResults.getChildren().clear(); // Clear previous search results

        // Get filtered list of books
        List<Book> results = library.filterBooks(query);

        if (results.isEmpty()) {
            Label noResultsLabel = new Label("No books found");
            dynamicResults.add(noResultsLabel, 0, 0);
        } else {
            // Display search results
            for (Book book : results) {
                onSearchBookButtonClick(book);
            }
        }
    }

    //Function called after borrow button is clicked
    private void onBorrowButtonClick(Book bookToBorrow) {
        BorrowDialog.setVisible(true);
        MoreInfoDialog.setVisible(false);
        BorrowBookLabel.setText("Borrow Book: " + bookToBorrow.getTitle());
        BookBorrowConfirmButton.setOnAction(e -> BookBorrowConfirm(bookToBorrow));
    }

    //Function called after borrower's name is input and confirmed to borrow
    private void BookBorrowConfirm(Book bookToBorrow) {
        String borrowerName = borrowerNameField.getText().trim();
        if (borrowerName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter the borrower's name.");
            return;
        }
        library.borrowBook(bookToBorrow, borrowerName);
        BorrowDialog.setVisible(false);
        borrowerNameField.clear();
        refreshSearchList(library.getLibrary());
    }

    //Function called after return button is clicked
    private void onReturnButtonClick(Book bookToReturn) {
        library.returnBook(bookToReturn);
        refreshSearchList(library.getLibrary());
    }

    //Function called after delete button is clicked
    private void onDeleteButtonClick(String title) {
        library.deleteBook(title);
        refreshSearchList(library.getLibrary());
    }
}