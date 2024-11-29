package com.example.librarysystem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Library {

    private final List<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public void saveBooksToCSV() {
        sortBooksByTitle();
        try (FileWriter writer = new FileWriter("C:\\Users\\junli\\IdeaProjects\\CAT-201-Asgmn1\\src\\main\\resources\\com\\example\\librarysystem\\Library System Data.csv", false)) { // 'false' to overwrite the file
            // Write the CSV header (optional)
            writer.append("Title,Author,ISBN,Available,Borrower\n");

            // Write data for each book in the books list
            for (Book book : books) {
                writer.append(book.getTitle()).append(",")
                        .append(book.getAuthor()).append(",")
                        .append(book.getIsbn()).append(",")
                        .append(book.isAvailable() ? "true" : "false").append(",")
                        .append("-").append("\n");
            }
            System.out.println("All books saved to CSV.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save books to CSV.");
        }
    }
    public void addBook(String title, String author, String isbn, boolean isAvailable, String borrower) {

        // Add book to the list
        books.add(new Book(title, author, isbn, isAvailable, borrower));

        // Sort books after adding
        sortBooksByTitle();
        saveBooksToCSV();
    }

    public List<Book> filterBooks(String query) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()) || book.getAuthor().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void sortBooksByTitle() {
        // Sort books by title
        books.sort(Comparator.comparing(Book::getTitle));
    }
}
