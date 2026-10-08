package com.airtribe.LibraryManagementSystem.pattern.strategy;

import com.airtribe.LibraryManagementSystem.model.Book;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorSearchStrategy implements SearchStrategy {

    @Override
    public List<Book> search(List<Book> books, String keyword) {

        return books.stream()
                .filter(book -> book.getAuthor()
                        .toLowerCase()
                        .contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}