package com.airtribe.LibraryManagementSystem.pattern.observer;

import com.airtribe.LibraryManagementSystem.model.Book;
import com.airtribe.LibraryManagementSystem.model.Patron;

public interface LibraryObserver {
    void onBookCheckedOut(Book book, Patron patron);
    void onBookReturned(Book book, Patron patron);
}
