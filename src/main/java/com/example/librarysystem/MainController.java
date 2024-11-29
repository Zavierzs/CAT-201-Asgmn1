package com.example.librarysystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MainController {

    // FXML elements
    @FXML
    private GridPane dynamicResults;
    @FXML
    private VBox dialog;
    @FXML
    private VBox addBookDialog;

    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label descriptionLabel;

    @FXML
    private Button borrowButton;
    @FXML
    private Button returnButton;
    @FXML
    private Button editButton;
    @FXML
    private Button addBookButton;
    @FXML
    private Button searchBookButton;

    @FXML
    private TextField searchField;
    @FXML
    private TextField bookNameField;
    @FXML
    private TextField bookAuthorField;
    @FXML
    private TextField bookIsbnField;

    // Library instance
    private final Library library = new Library();

    // Initializes the controller
    public void initialize() {
        loadBooksFromFile("com/example/librarysystem/Library System Data.csv");

        // Set up button actions
        addBookButton.setOnAction(e -> showAddBookDialog());
        searchBookButton.setOnAction(e -> onSearchButtonClick());
        borrowButton.setOnAction(e -> System.out.println("Borrow button clicked!"));
        returnButton.setOnAction(e -> System.out.println("Return button clicked!"));
        editButton.setOnAction(e -> System.out.println("Edit button clicked!"));
    }

    // Loads books from the CSV file into the library
    private void loadBooksFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] bookData = line.split(",");
                if (bookData.length == 5) {
                    String title = bookData[0];
                    String author = bookData[1];
                    String isbn = bookData[2];
                    boolean isAvailable = Boolean.parseBoolean(bookData[3]);
                    String borrower = bookData[4];
                    library.addBook(title, author, isbn, isAvailable, borrower);
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error reading the CSV file: " + e.getMessage());
        }
    }

    // Search for books based on the query entered in the search field
    @FXML
    private void onSearchButtonClick() {
        addBookDialog.setVisible(false);  // Hide the add book dialog
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
                onAddBookButtonClick(book);
            }
        }
    }

    // Shows the book details dialog when more info is requested
    private void showDialog(String title, String author, String description) {
        titleLabel.setText(title);
        authorLabel.setText("Author: " + author);
        descriptionLabel.setText(description);
        dialog.setVisible(true);
        addBookDialog.setVisible(false);
    }

    // Show the Add Book dialog
    private void showAddBookDialog() {
        addBookDialog.setVisible(true);
        dialog.setVisible(false);
    }

    // Adds a new book to the library from the Add Book dialog
    @FXML
    private void addBook() {
        String bookName = bookNameField.getText().trim();
        String author = bookAuthorField.getText().trim();
        String isbn = bookIsbnField.getText().trim();

        // Validate input fields
        if (bookName.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required!");
            return;
        }

        // Add the book to the library
        library.addBook(bookName, author, isbn, true, "");
        onAddBookButtonClick(new Book(bookName, author, isbn));  // Add to UI
        closeAddBookDialog();
    }

    // Displays each book in the search results
    private void onAddBookButtonClick(Book book) {
        // Create UI components for each book
        Circle resultStatus = new Circle(10, book.getAvailablity() ? Color.GREEN : Color.RED);
        Label resultLabel = new Label(book.getTitle());
        Button resultButton = new Button("More Info");

        // Add an event handler for the "More Info" button
        resultButton.setOnAction(e -> handleMoreInfo(book.getTitle(), book.getAuthor(), book.getIsbn()));

        // Add components to the GridPane
        int rowIndex = dynamicResults.getChildren().size() / 3;
        dynamicResults.add(resultStatus, 0, rowIndex);
        dynamicResults.add(resultLabel, 1, rowIndex);
        dynamicResults.add(resultButton, 2, rowIndex);
    }

    // Opens the dialog to show more info about the selected book
    private void handleMoreInfo(String title, String author, String isbn) {
        showDialog(title, author, "ISBN: " + isbn);
    }

    // Closes the Add Book dialog
    private void closeAddBookDialog() {
        addBookDialog.setVisible(false);
    }

    // Displays an alert message
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.show();
    }

    // Placeholder for future search button event (if needed)
    public void onSearchBookButtonClick(ActionEvent actionEvent) {
        // Additional functionality can go here
    }
}
