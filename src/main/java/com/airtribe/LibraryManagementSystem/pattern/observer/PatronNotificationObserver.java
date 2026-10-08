package com.airtribe.LibraryManagementSystem.pattern.observer;

import com.airtribe.LibraryManagementSystem.model.Book;
import com.airtribe.LibraryManagementSystem.model.Patron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatronNotificationObserver implements LibraryObserver {

    private static final Logger logger =
            LoggerFactory.getLogger(PatronNotificationObserver.class);

    @Override
    public void onBookCheckedOut(Book book, Patron patron) {
        logger.info("Notification: '{}' was checked out by patron {} ({})",
                book.getTitle(), patron.getPatronId(), patron.getEmail());
    }

    @Override
    public void onBookReturned(Book book, Patron patron) {
        logger.info("Notification: '{}' is available again after return by patron {} ({})",
                book.getTitle(), patron.getPatronId(), patron.getEmail());
    }
}
