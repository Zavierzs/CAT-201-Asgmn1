package com.example.librarysystem;

public class Book {

    private String title;
    private String author;
    private String isbn;
    private boolean availability;
    private String borrowerName;

    public Book(String title, String author, String isbn, boolean isAvailable, String borrower) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.availability = isAvailable;
        this.borrowerName = borrower;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() { return isbn; }

    public boolean isAvailable() { return availability; }

    public void setAvailable(boolean available) {
        availability = available;
    }

    public void setBorrower(String borrower) {
        this.borrowerName = borrower;
    }


}
