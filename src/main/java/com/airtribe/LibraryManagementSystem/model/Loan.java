package com.airtribe.LibraryManagementSystem.model;

import java.time.LocalDate;

public class Loan {

    private final Book book;
    private final Patron patron;
    private final LocalDate checkoutDate;
    private LocalDate returnDate;

    public Loan(Book book, Patron patron) {
        this.book = book;
        this.patron = patron;
        this.checkoutDate = LocalDate.now();
    }

    public Book getBook() {
        return book;
    }

    public Patron getPatron() {
        return patron;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void markAsReturned() {
        this.returnDate = LocalDate.now();
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "book=" + book.getTitle() +
                ", patron=" + patron.getName() +
                ", checkoutDate=" + checkoutDate +
                ", returnDate=" + returnDate +
                '}';
    }
}