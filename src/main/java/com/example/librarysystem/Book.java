package com.example.librarysystem;

public class Book {

    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;
    private String borrower;

    public Book(String title, String author, String isbn, boolean isAvailable, String borrower) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
        this.borrower = borrower;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }


}
