package com.airtribe.LibraryManagementSystem.pattern.strategy;

import com.airtribe.LibraryManagementSystem.model.Book;

import java.util.List;

public interface SearchStrategy {

    List<Book> search(List<Book> books, String keyword);
}