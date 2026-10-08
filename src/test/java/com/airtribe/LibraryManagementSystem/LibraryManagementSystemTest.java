package com.airtribe.LibraryManagementSystem;

import com.airtribe.LibraryManagementSystem.exception.BookNotAvailableException;
import com.airtribe.LibraryManagementSystem.model.Book;
import com.airtribe.LibraryManagementSystem.model.Patron;
import com.airtribe.LibraryManagementSystem.repository.BookRepository;
import com.airtribe.LibraryManagementSystem.repository.InMemoryBookRepository;
import com.airtribe.LibraryManagementSystem.repository.InMemoryPatronRepository;
import com.airtribe.LibraryManagementSystem.repository.PatronRepository;
import com.airtribe.LibraryManagementSystem.service.BookService;
import com.airtribe.LibraryManagementSystem.service.LendingService;
import com.airtribe.LibraryManagementSystem.service.PatronService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryManagementSystemTest {

    private final BookRepository bookRepository = new InMemoryBookRepository();
    private final PatronRepository patronRepository = new InMemoryPatronRepository();
    private final BookService bookService = new BookService(bookRepository);
    private final PatronService patronService = new PatronService(patronRepository);
    private final LendingService lendingService =
            new LendingService(bookRepository, patronService);

    @Test
    void shouldAddSearchUpdateAndRemoveBook() {
        Book book = new Book("Clean Code", "Robert C. Martin",
                "9780132350884", 2008);

        bookService.addBook(book);

        assertEquals(1, bookService.getAllBooks().size());
        assertEquals(1, bookService.searchBooks(
                new com.airtribe.LibraryManagementSystem.pattern.strategy.TitleSearchStrategy(),
                "clean").size());

        book.setPublicationYear(2009);
        bookService.updateBook(book);
        assertEquals(2009, bookRepository.findByIsbn(book.getIsbn()).orElseThrow().getPublicationYear());

        bookService.removeBook(book.getIsbn());
        assertTrue(bookService.getAllBooks().isEmpty());
    }

    @Test
    void shouldCheckoutAndReturnBookAndTrackHistory() {
        Book book = new Book("Effective Java", "Joshua Bloch",
                "9780134685991", 2018);
        Patron patron = new Patron(101, "John Doe", "john@example.com");

        bookService.addBook(book);
        patronService.addPatron(patron);

        lendingService.checkoutBook(book.getIsbn(), patron.getPatronId());

        assertFalse(book.isAvailable());
        assertEquals(1, patron.getBorrowingHistory().size());
        assertFalse(patron.getBorrowingHistory().get(0).isReturned());

        lendingService.returnBook(book.getIsbn(), patron.getPatronId());

        assertTrue(book.isAvailable());
        assertTrue(patron.getBorrowingHistory().get(0).isReturned());
    }

    @Test
    void shouldRejectCheckoutWhenBookIsAlreadyBorrowed() {
        Book book = new Book("Clean Architecture", "Robert C. Martin",
                "9780134494166", 2017);
        Patron first = new Patron(1, "First Patron", "first@example.com");
        Patron second = new Patron(2, "Second Patron", "second@example.com");

        bookService.addBook(book);
        patronService.addPatron(first);
        patronService.addPatron(second);

        lendingService.checkoutBook(book.getIsbn(), first.getPatronId());

        assertThrows(BookNotAvailableException.class,
                () -> lendingService.checkoutBook(book.getIsbn(), second.getPatronId()));
    }
}
