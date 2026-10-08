package com.airtribe.LibraryManagementSystem.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Patron {

    private final int patronId;
    private String name;
    private String email;
    private final List<Loan> borrowingHistory;

    public Patron(int patronId, String name, String email) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
        this.borrowingHistory = new ArrayList<>();
    }

    public int getPatronId() {
        return patronId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Loan> getBorrowingHistory() {
        return Collections.unmodifiableList(borrowingHistory);
    }

    public void addLoanToHistory(Loan loan) {
        borrowingHistory.add(loan);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "patronId=" + patronId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}