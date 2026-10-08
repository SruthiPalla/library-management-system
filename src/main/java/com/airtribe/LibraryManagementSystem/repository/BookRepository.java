package com.airtribe.LibraryManagementSystem.repository;

import com.airtribe.LibraryManagementSystem.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    void add(Book book);
    void remove(String isbn);
    void update(Book book);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
}
