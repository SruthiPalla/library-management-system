package com.airtribe.LibraryManagementSystem;

import com.airtribe.LibraryManagementSystem.model.Book;
import com.airtribe.LibraryManagementSystem.model.Patron;
import com.airtribe.LibraryManagementSystem.pattern.observer.PatronNotificationObserver;
import com.airtribe.LibraryManagementSystem.pattern.strategy.AuthorSearchStrategy;
import com.airtribe.LibraryManagementSystem.pattern.strategy.IsbnSearchStrategy;
import com.airtribe.LibraryManagementSystem.pattern.strategy.SearchStrategy;
import com.airtribe.LibraryManagementSystem.pattern.strategy.TitleSearchStrategy;
import com.airtribe.LibraryManagementSystem.repository.BookRepository;
import com.airtribe.LibraryManagementSystem.repository.InMemoryBookRepository;
import com.airtribe.LibraryManagementSystem.repository.InMemoryPatronRepository;
import com.airtribe.LibraryManagementSystem.repository.PatronRepository;
import com.airtribe.LibraryManagementSystem.service.BookService;
import com.airtribe.LibraryManagementSystem.service.LendingService;
import com.airtribe.LibraryManagementSystem.service.PatronService;

import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for the Library Management System.
 *
 * This class is intentionally responsible only for interacting with the user
 * and delegating business operations to the service layer.
 */
public class Main {

    private final Scanner scanner;
    private final BookService bookService;
    private final PatronService patronService;
    private final LendingService lendingService;

    public Main() {
        BookRepository bookRepository = new InMemoryBookRepository();
        PatronRepository patronRepository = new InMemoryPatronRepository();

        this.bookService = new BookService(bookRepository);
        this.patronService = new PatronService(patronRepository);
        this.lendingService = new LendingService(bookRepository, patronService);
        this.lendingService.addObserver(new PatronNotificationObserver());
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("============================================");
        System.out.println("       LIBRARY MANAGEMENT SYSTEM");
        System.out.println("============================================");
        System.out.println("Welcome! Please choose an option from the menu.");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        addBook();
                        break;
                    case 2:
                        updateBook();
                        break;
                    case 3:
                        removeBook();
                        break;
                    case 4:
                        listBooks();
                        break;
                    case 5:
                        searchBooks();
                        break;
                    case 6:
                        addPatron();
                        break;
                    case 7:
                        updatePatron();
                        break;
                    case 8:
                        listPatrons();
                        break;
                    case 9:
                        checkoutBook();
                        break;
                    case 10:
                        returnBook();
                        break;
                    case 11:
                        viewBorrowingHistory();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Thank you for using the Library Management System!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please select an option from 0 to 11.");
                }
            } catch (RuntimeException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n---------------- MAIN MENU ----------------");
        System.out.println("BOOK MANAGEMENT");
        System.out.println("  1. Add book");
        System.out.println("  2. Update book");
        System.out.println("  3. Remove book");
        System.out.println("  4. List all books");
        System.out.println("  5. Search books");
        System.out.println("\nPATRON MANAGEMENT");
        System.out.println("  6. Add patron");
        System.out.println("  7. Update patron");
        System.out.println("  8. List all patrons");
        System.out.println("\nLENDING");
        System.out.println("  9. Checkout book");
        System.out.println(" 10. Return book");
        System.out.println(" 11. View borrowing history");
        System.out.println("\n  0. Exit");
        System.out.println("--------------------------------------------");
    }

    private void addBook() {
        System.out.println("\n--- Add Book ---");
        String title = readRequiredString("Title: ");
        String author = readRequiredString("Author: ");
        String isbn = readRequiredString("ISBN: ");
        int year = readInt("Publication year: ");

        bookService.addBook(new Book(title, author, isbn, year));
        System.out.println("Book added successfully.");
    }

    private void updateBook() {
        System.out.println("\n--- Update Book ---");
        String isbn = readRequiredString("Enter ISBN of the book to update: ");
        Book existing = bookService.getAllBooks().stream()
                .filter(book -> book.getIsbn().equalsIgnoreCase(isbn))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ISBN: " + isbn));

        System.out.println("Current book: " + existing);
        String title = readRequiredString("New title: ");
        String author = readRequiredString("New author: ");
        int year = readInt("New publication year: ");

        Book updated = new Book(title, author, existing.getIsbn(), year);
        updated.setAvailable(existing.isAvailable());
        bookService.updateBook(updated);
        System.out.println("Book updated successfully.");
    }

    private void removeBook() {
        System.out.println("\n--- Remove Book ---");
        String isbn = readRequiredString("Enter ISBN: ");
        bookService.removeBook(isbn);
        System.out.println("Book removed successfully.");
    }

    private void listBooks() {
        System.out.println("\n--- All Books ---");
        List<Book> books = bookService.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books found in the library.");
            return;
        }

        books.forEach(book -> System.out.println(formatBook(book)));
    }

    private void searchBooks() {
        System.out.println("\n--- Search Books ---");
        System.out.println("1. Search by title");
        System.out.println("2. Search by author");
        System.out.println("3. Search by ISBN");

        int choice = readInt("Choose search type: ");
        SearchStrategy strategy;

        switch (choice) {
            case 1:
                strategy = new TitleSearchStrategy();
                break;
            case 2:
                strategy = new AuthorSearchStrategy();
                break;
            case 3:
                strategy = new IsbnSearchStrategy();
                break;
            default:
                System.out.println("Invalid search option.");
                return;
        }

        String keyword = readRequiredString("Enter search value: ");
        List<Book> results = bookService.searchBooks(strategy, keyword);

        if (results.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println("\nSearch results:");
        results.forEach(book -> System.out.println(formatBook(book)));
    }

    private void addPatron() {
        System.out.println("\n--- Add Patron ---");
        int id = readInt("Patron ID: ");
        String name = readRequiredString("Name: ");
        String email = readRequiredString("Email: ");

        patronService.addPatron(new Patron(id, name, email));
        System.out.println("Patron added successfully.");
    }

    private void updatePatron() {
        System.out.println("\n--- Update Patron ---");
        int id = readInt("Enter patron ID: ");
        Patron existing = patronService.getPatronById(id);

        System.out.println("Current patron: " + existing);
        String name = readRequiredString("New name: ");
        String email = readRequiredString("New email: ");

        existing.setName(name);
        existing.setEmail(email);
        patronService.updatePatron(existing);
        System.out.println("Patron updated successfully.");
    }

    private void listPatrons() {
        System.out.println("\n--- All Patrons ---");
        List<Patron> patrons = patronService.getAllPatrons();

        if (patrons.isEmpty()) {
            System.out.println("No patrons found.");
            return;
        }

        patrons.forEach(System.out::println);
    }

    private void checkoutBook() {
        System.out.println("\n--- Checkout Book ---");
        String isbn = readRequiredString("Book ISBN: ");
        int patronId = readInt("Patron ID: ");

        lendingService.checkoutBook(isbn, patronId);
        System.out.println("Book checked out successfully.");
    }

    private void returnBook() {
        System.out.println("\n--- Return Book ---");
        String isbn = readRequiredString("Book ISBN: ");
        int patronId = readInt("Patron ID: ");

        lendingService.returnBook(isbn, patronId);
        System.out.println("Book returned successfully.");
    }

    private void viewBorrowingHistory() {
        System.out.println("\n--- Borrowing History ---");
        int patronId = readInt("Patron ID: ");
        Patron patron = patronService.getPatronById(patronId);

        if (patron.getBorrowingHistory().isEmpty()) {
            System.out.println("No borrowing history found for this patron.");
            return;
        }

        System.out.println("Borrowing history for " + patron.getName() + ":");
        patron.getBorrowingHistory().forEach(System.out::println);
    }

    private String formatBook(Book book) {
        return String.format(
                "Title: %s | Author: %s | ISBN: %s | Year: %d | Status: %s",
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.isAvailable() ? "Available" : "Borrowed");
    }

    private String readRequiredString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
