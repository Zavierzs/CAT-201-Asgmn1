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

    // Library instance
    private final Library library = new Library();

    // Initializes the controller
    public void initialize() {
        library.loadBooksFromCSV("C:\\Users\\User\\IdeaProjects\\Test1Dec1\\CAT-201-Asgmn1\\src\\main\\resources\\com\\example\\librarysystem\\Library System Data.csv");

        // Set up button actions
        addBookButton.setOnAction(e -> showAddBookDialog());
        FindBook.setOnAction(e -> onFindButtonClick());
        confirmBookButton.setOnAction(e -> ConfirmBook());
        searchButton.setOnAction(e->SearchBook());
    }

    // Search for books based on the query entered in the search field
    @FXML
    private void onFindButtonClick() {
        addBookDialog.setVisible(false);// Hide the add book dialog
        SearchPage.setVisible(true);
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
    // Shows the book details dialog when more info is requested
    private void showDialog(String title, String author, String description) {
        titleLabel.setText(title);
        authorLabel.setText("Author: " + author);
        descriptionLabel.setText(description);
        MoreInfoDialog.setVisible(true);
        BorrowDialog.setVisible(false);
    }

    // Show the Add Book dialog
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

    // Adds a new book to the library from the Add Book dialog
    @FXML
    private void ConfirmBook() {
        System.out.println("confirm button clicked!");
        String bookName = bookNameField.getText().trim();
        String author = bookAuthorField.getText().trim();
        String isbn = bookIsbnField.getText().trim();

        // Validate input fields
        if (bookName.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required!");
            return;
        }

        // Validate ISBN format (e.g., 13-digit ISBN)
        if (!isValidISBN(isbn)) {
            showAlert(Alert.AlertType.ERROR, "Invalid ISBN format. Please enter a valid 9-digit ISBN.");
            return;
        }

        // Add the book to the library
        library.addBook(bookName, author, isbn, true, "");
        library.sortBooksByTitle();
        onSearchBookButtonClick(new Book(bookName, author, isbn, true, ""));  // Add to UI
        clearFields();
        closeAddBookDialog();
        dynamicResults.getChildren().clear();
    }

    private boolean isValidISBN(String isbn) {
        return isbn.matches("\\d{9}");
    }

    // Displays each book in the search results
    private void onSearchBookButtonClick(Book book) {
        // Create UI components for each book
        Circle resultStatus = new Circle(10, book.isAvailable() ? Color.GREEN : Color.RED);
        Label resultLabel = new Label(book.getTitle());
        Button MoreInfoButton = new Button("More Info");

        // Add an event handler for the "More Info" button
        MoreInfoButton.setOnAction(e -> handleMoreInfo(book.getTitle(), book.getAuthor(), book.getIsbn(), book.isAvailable()));

        // Add components to the GridPane
        int rowIndex = dynamicResults.getChildren().size() / 3;

        dynamicResults.add(resultStatus, 0, rowIndex);
        dynamicResults.add(resultLabel, 1, rowIndex);
        dynamicResults.add(MoreInfoButton, 2, rowIndex);

        adjustRowHeight(dynamicResults, rowIndex, 40); // Example height set to 40px
    }

    private void refreshSearchList(List<Book> books) {
        dynamicResults.getChildren().clear();

        for (Book book : books) {
            // Create UI elements for each book
            Circle resultStatus = new Circle(10, book.isAvailable() ? Color.GREEN : Color.RED);
            Label resultLabel = new Label(book.getTitle());
            Button moreInfoButton = new Button("More Info");

            // Add event handlers for buttons
            moreInfoButton.setOnAction(e -> handleMoreInfo(book.getTitle(), book.getAuthor(), book.getIsbn(), book.isAvailable()));

            // Add components to the GridPane
            int rowIndex = dynamicResults.getChildren().size() / 3;
            dynamicResults.add(resultStatus, 0, rowIndex);
            dynamicResults.add(resultLabel, 1, rowIndex);
            dynamicResults.add(moreInfoButton, 2, rowIndex);

            // Adjust row height (optional)
            adjustRowHeight(dynamicResults, rowIndex, 40);
        }
    }

    private void adjustRowHeight(GridPane gridPane, int rowIndex, double height) {
        // Ensure there are enough row constraints
        while (gridPane.getRowConstraints().size() <= rowIndex) {
            gridPane.getRowConstraints().add(new RowConstraints());
        }

        // Set the preferred height for the row
        RowConstraints rowConstraints = gridPane.getRowConstraints().get(rowIndex);
        rowConstraints.setPrefHeight(height);  // Set preferred height
        rowConstraints.setMinHeight(height);   // Optional: Set minimum height
        rowConstraints.setMaxHeight(height);   // Optional: Set maximum height
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

    private void onBorrowButtonClick(Book bookToBorrow) {
        BorrowDialog.setVisible(true);
        MoreInfoDialog.setVisible(false);
        BorrowBookLabel.setText("Borrow Book: " + bookToBorrow.getTitle());
        BookBorrowConfirmButton.setOnAction(e -> BookBorrowConfirm(bookToBorrow));
    }

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

    private void onReturnButtonClick(Book bookToReturn) {
        library.returnBook(bookToReturn);
        refreshSearchList(library.getLibrary());
    }

    private void onDeleteButtonClick(String title) {
        library.deleteBook(title);
        refreshSearchList(library.getLibrary());
    }
}