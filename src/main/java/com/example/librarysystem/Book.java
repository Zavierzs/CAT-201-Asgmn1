package com.example.librarysystem;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable;
    private String borrower;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.isAvailable = true;
        this.borrower = "";
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public boolean getAvailablity() {
        return isAvailable;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setAvailability(boolean x){
        this.isAvailable = x;
    }

    public void setBorrower(String y){
        this.borrower = y;
    }
}
