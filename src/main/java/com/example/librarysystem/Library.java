package com.example.librarysystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {

    private final List<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    public void addBook(String title, String author, String isbn, boolean isAvailable, String borrower) {
        books.add(new Book(title, author, isbn, isAvailable, borrower));
    }

    public List<Book> filterBooks(String query) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase()) || book.getAuthor().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> getBooks() {
        return books;
    }
}
