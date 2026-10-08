package com.airtribe.LibraryManagementSystem.service;

import com.airtribe.LibraryManagementSystem.exception.BookNotAvailableException;
import com.airtribe.LibraryManagementSystem.exception.BookNotFoundException;
import com.airtribe.LibraryManagementSystem.model.Book;
import com.airtribe.LibraryManagementSystem.model.Loan;
import com.airtribe.LibraryManagementSystem.model.Patron;
import com.airtribe.LibraryManagementSystem.pattern.observer.LibraryObserver;
import com.airtribe.LibraryManagementSystem.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LendingService {

    private static final Logger logger =
            LoggerFactory.getLogger(LendingService.class);

    private final BookRepository bookRepository;
    private final PatronService patronService;
    private final List<LibraryObserver> observers = new ArrayList<>();

    public LendingService(BookRepository bookRepository, PatronService patronService) {
        this.bookRepository = bookRepository;
        this.patronService = patronService;
    }

    public void addObserver(LibraryObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(LibraryObserver observer) {
        observers.remove(observer);
    }

    public void checkoutBook(String isbn, int patronId) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found with ISBN: " + isbn));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException(
                    "Book is currently borrowed: " + book.getTitle());
        }

        Patron patron = patronService.getPatronById(patronId);
        Loan loan = new Loan(book, patron);

        book.setAvailable(false);
        patron.addLoanToHistory(loan);

        logger.info("Book '{}' checked out by patron {}", book.getTitle(), patronId);
        observers.forEach(observer -> observer.onBookCheckedOut(book, patron));
    }

    public void returnBook(String isbn, int patronId) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found with ISBN: " + isbn));

        Patron patron = patronService.getPatronById(patronId);

        for (Loan loan : patron.getBorrowingHistory()) {
            if (loan.getBook().getIsbn().equalsIgnoreCase(isbn) && !loan.isReturned()) {
                loan.markAsReturned();
                book.setAvailable(true);

                logger.info("Book '{}' returned by patron {}", book.getTitle(), patronId);
                observers.forEach(observer -> observer.onBookReturned(book, patron));
                return;
            }
        }

        logger.error("No active loan found for ISBN {} and patron {}", isbn, patronId);
        throw new IllegalStateException("No active loan found for this book and patron.");
    }
}
