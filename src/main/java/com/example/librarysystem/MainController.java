package com.example.librarysystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController{

    @FXML
    private GridPane dynamicResults; // GridPane to hold the dynamic search results
    @FXML
    private VBox dialog; // Dialog for showing book details
    @FXML
    private VBox addBookDialog; // Dialog for adding a new book

    @FXML
    private Label titleLabel; // Title label in the dialog
    @FXML
    private Label authorLabel; // Author label in the dialog
    @FXML
    private Label descriptionLabel; // Description label in the dialog

    @FXML
    private Button borrowButton; // Borrow button
    @FXML
    private Button returnButton; // Return button
    @FXML
    private Button editButton; // Edit button
    @FXML
    private Button addBookButton; // Add book button
    @FXML
    private Button SearchBookButton;
    @FXML
    private TextField searchField; // Search field for user input

    // Add book input fields
    @FXML
    private TextField bookNameField; // Field for book name
    @FXML
    private TextField bookAuthorField; // Field for author name
    @FXML
    private TextField bookIsbnField; // Field for ISBN

    // Sample book data
    private List<Book> books = new ArrayList<>();

    Library library = new Library();

    // Search for books
    String query = searchField.getText();
    List<Book> searchResults = library.filterBooks(query);

    // Initialize method to populate sample books and set up event handlers
    public void initialize() {
        try (BufferedReader br = new BufferedReader(new FileReader("Library System Data.csv"))) {
            String line;
            br.readLine();
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
            e.printStackTrace();
        }


        // Add event listeners for buttons
        addBookButton.setOnAction(e -> showAddBookDialog());
        SearchBookButton.setOnAction(e -> closeDialog());
        borrowButton.setOnAction(e -> System.out.println("Borrow button clicked!"));
        returnButton.setOnAction(e -> System.out.println("Return button clicked!"));
        editButton.setOnAction(e -> System.out.println("Edit button clicked!"));
    }


    // Method to handle "Search" button click
    @FXML
    private void onSearchButtonClick() {
        // Get the search query from the search field
        addBookDialog.setVisible(false);
        String query = searchField.getText();

        // Clear previous search results
        dynamicResults.getChildren().clear();

        // Flag to check if we found any matches
        boolean foundMatch = false;

        // Filter books based on the search query
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                addBookRow(book); // Add book row dynamically if it matches
                foundMatch = true;
            }
        }

        // If no matches found, show a "No results found" message
        if (!foundMatch) {
            Label noResultsLabel = new Label("No books found");
            dynamicResults.add(noResultsLabel, 0, 0); // Add the message at the first row
        }
    }

    // Method to show a dialog with more information about the book
    private void showDialog(String title, String author, String description) {
        titleLabel.setText(title);
        authorLabel.setText("Author: " + author);
        descriptionLabel.setText(description);
        dialog.setVisible(true); // Show the dialog
        addBookDialog.setVisible(false);
    }

    // Method to close the dialog
    @FXML
    private void closeDialog() {
        addBookDialog.setVisible(false); // Hide the dialog
    }

    // Method to show the "Add Book" dialog
    private void showAddBookDialog() {
        addBookDialog.setVisible(true); // Show the add book dialog
        dialog.setVisible(false);
    }

    // Method to close the "Add Book" dialog
    @FXML
    private void closeAddBookDialog() {
        addBookDialog.setVisible(false); // Hide the add book dialog
    }

    // Method to add a book from the dialog form
    @FXML
    private void addBook() {
        String bookName = bookNameField.getText();
        String author = bookAuthorField.getText();
        String isbn = bookIsbnField.getText();

        if (bookName.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            // Display an error message if any field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required!", ButtonType.OK);
            alert.show();
            return;
        }

        // Create a new book and add it to the list
        Book newBook = new Book(bookName, author, isbn);
        books.add(newBook);

        // Add the book to the GridPane dynamically
        addBookRow(newBook);

        // Close the add book dialog
        closeAddBookDialog();
    }

    // Method to dynamically add a book row in the GridPane
    private void addBookRow(Book book) {

        // Create the Circle for borrowed status (Red for borrowed, Green for available)
        Circle resultStatus = new Circle(10);  // Radius of the circle
        resultStatus.setFill(Color.GREEN); // Set color based on borrowed status (default to available)

        // Create a Label for the book's title
        Label resultLabel = new Label(book.getTitle());

        // Create a Button for more info
        Button resultButton = new Button("More Info");

        // Handle the button click to show more info
        resultButton.setOnAction(e -> handleMoreInfo(book.getTitle(), book.getAuthor(), book.getIsbn()));

        // Add the status circle, label, and button directly to the GridPane
        int rowIndex = dynamicResults.getChildren().size() / 3; // Each row has 3 elements (Circle, Label, Button)
        dynamicResults.add(resultStatus, 0, rowIndex); // Add status circle in column 0
        dynamicResults.add(resultLabel, 1, rowIndex);   // Add book title in column 1
        dynamicResults.add(resultButton, 2, rowIndex);  // Add "More Info" button in column 2
    }

    // Method to handle "More Info" button click (show book details)
    private void handleMoreInfo(String title, String author, String isbn) {
        showDialog(title, author, "ISBN: " + isbn); // Show the dialog with the book details
    }

}




