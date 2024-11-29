package com.example.librarysystem;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private List<Book> library = new ArrayList<>();

    public Library() {}

    public void addBook(String title, String author, String isbn) {
        Book newBook = new Book(title, author, isbn);
        newBook.setAvailability(true);
        newBook.setBorrower("");
        library.add(newBook);
    }

    public void addBook(String title, String author, String isbn, boolean availability, String borrower) {
        Book newBook = new Book(title, author, isbn);
        newBook.setAvailability(availability);
        newBook.setBorrower(borrower);
        library.add(newBook);
    }

    public List<Book> filterBooks(String query) {
        List<Book> searchResults = new ArrayList<>();
        for (Book book : library) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(book);
            }
        }
        return searchResults;
    }
}