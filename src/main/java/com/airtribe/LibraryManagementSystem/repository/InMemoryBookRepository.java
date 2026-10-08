package com.airtribe.LibraryManagementSystem.repository;

import com.airtribe.LibraryManagementSystem.model.Book;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryBookRepository implements BookRepository {

    private final Map<String, Book> books = new LinkedHashMap<>();

    @Override
    public void add(Book book) {
        books.put(book.getIsbn(), book);
    }

    @Override
    public void remove(String isbn) {
        books.remove(isbn);
    }

    @Override
    public void update(Book updatedBook) {
        books.put(updatedBook.getIsbn(), updatedBook);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }
}
