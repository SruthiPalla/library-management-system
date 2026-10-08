package com.airtribe.LibraryManagementSystem.service;

import com.airtribe.LibraryManagementSystem.exception.BookNotFoundException;
import com.airtribe.LibraryManagementSystem.model.Book;
import com.airtribe.LibraryManagementSystem.pattern.strategy.SearchStrategy;
import com.airtribe.LibraryManagementSystem.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalArgumentException(
                    "A book with ISBN already exists: " + book.getIsbn());
        }

        bookRepository.add(book);
        logger.info("Book added successfully: {}", book.getIsbn());
    }

    public void removeBook(String isbn) {
        bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with ISBN: " + isbn));

        bookRepository.remove(isbn);
        logger.info("Book removed successfully: {}", isbn);
    }

    public void updateBook(Book book) {
        bookRepository.findByIsbn(book.getIsbn())
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with ISBN: " + book.getIsbn()));

        bookRepository.update(book);
        logger.info("Book updated successfully: {}", book.getIsbn());
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBooks(SearchStrategy strategy, String keyword) {
        if (strategy == null) {
            throw new IllegalArgumentException("Search strategy must not be null");
        }
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword must not be blank");
        }

        return strategy.search(bookRepository.findAll(), keyword);
    }
}
