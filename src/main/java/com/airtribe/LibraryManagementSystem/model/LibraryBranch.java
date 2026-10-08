package com.airtribe.LibraryManagementSystem.model;

import java.util.ArrayList;
import java.util.List;

public class LibraryBranch {

    private final int branchId;
    private String branchName;
    private String location;
    private final List<Book> books;

    public LibraryBranch(int branchId, String branchName, String location) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.location = location;
        this.books = new ArrayList<>();
    }

    public int getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    @Override
    public String toString() {
        return "LibraryBranch{" +
                "branchId=" + branchId +
                ", branchName='" + branchName + '\'' +
                ", location='" + location + '\'' +
                ", booksCount=" + books.size() +
                '}';
    }
}