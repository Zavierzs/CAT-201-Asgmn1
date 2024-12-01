package com.example.librarysystem;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Library {

    private final List<Book> library;

    public Library() {
        library = new ArrayList<>();
    }

    private static final MainController controller = new MainController();

    public List<Book> getLibrary() {
        return Collections.unmodifiableList(library);
    }

    // Loads books from the CSV file into the library
    public void loadBooksFromCSV() {
        String fileName = "src/main/resources/com/example/librarysystem/Library System Data.csv";
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
                    addBook(title, author, isbn, isAvailable, borrower);
                }
            }
        } catch (IOException e) {
            controller.showAlert(Alert.AlertType.ERROR, "Error reading the CSV file: " + e.getMessage());
        }
    }

    public void saveBooksToCSV() {
        sortBooksByTitle();

        try (FileWriter writer = new FileWriter("src/main/resources/com/example/librarysystem/Library System Data.csv", false)) {
            // Write the CSV header (optional)
            writer.append("Title,Author,ISBN,Available,Borrower\n");

            // Write data for each book in the books list
            for (Book book : library) {
                writer.append(book.getTitle()).append(",")
                        .append(book.getAuthor()).append(",")
                        .append(book.getIsbn()).append(",")
                        .append(book.isAvailable() ? "true" : "false").append(",")
                        .append("-").append("\n");
            }
            System.out.println("All books saved to CSV.");
        } catch (IOException e) {
            controller.showAlert(Alert.AlertType.ERROR, "Failed to save books to CSV: " + e.getMessage());
        }
    }

    public void addBook(String title, String author, String isbn, boolean isAvailable, String borrower) {
        // Add book to the list
        library.add(new Book(title, author, isbn, isAvailable, borrower));
        // Sort books after adding
        sortBooksByTitle();
        saveBooksToCSV();
    }

    public void borrowBook(Book book, String borrowerName) {
        if (book.isAvailable()) {
            book.setAvailable(false);
            book.setBorrower(borrowerName);
            // Update the book in the library list
            for (int i = 0; i < library.size(); i++) {
                if (library.get(i).getTitle().equals(book.getTitle())) {
                    library.set(i, book);
                    break;
                }
            }
            saveBooksToCSV();
            controller.showAlert(Alert.AlertType.INFORMATION, "Book borrowed successfully.");
        } else {
            controller.showAlert(Alert.AlertType.WARNING, "Book is already borrowed.");
        }
    }

    public void returnBook(Book book) {
        if (!book.isAvailable()) {
            book.setAvailable(true);
            book.setBorrower("");
            for (int i = 0; i < library.size(); i++) {
                if (library.get(i).getTitle().equals(book.getTitle())) {
                    library.set(i, book);
                    break;
                }
            }
            saveBooksToCSV();
            //updateBookStatusInUI(bookToReturn);
            controller.showAlert(Alert.AlertType.INFORMATION, "Book returned successfully.");
        } else {
            controller.showAlert(Alert.AlertType.WARNING, "Book is already available.");
        }
    }

    public void deleteBook(String bookTitle) {
        Book bookToDelete = null;
        for (Book book : library) {
            if (book.getTitle().equals(bookTitle)) {
                bookToDelete = book;
                break;
            }
        }
        if (bookToDelete != null) {
            library.remove(bookToDelete);
            saveBooksToCSV();
            controller.showAlert(Alert.AlertType.INFORMATION, "Book deleted successfully.");
        } else {
            controller.showAlert(Alert.AlertType.WARNING, "Book not found.");
        }
    }

    public List<Book> filterBooks(String query) {
        return library.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase())
                        || book.getAuthor().toLowerCase().contains(query.toLowerCase())
                        || book.getIsbn().equals(query))
                .collect(Collectors.toList());
    }

    public void sortBooksByTitle() {
        library.sort(Comparator.comparing(Book::getTitle));
    }
}
