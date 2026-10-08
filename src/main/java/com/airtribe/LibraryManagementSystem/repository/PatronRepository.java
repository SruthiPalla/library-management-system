package com.airtribe.LibraryManagementSystem.repository;

import com.airtribe.LibraryManagementSystem.model.Patron;

import java.util.List;
import java.util.Optional;

public interface PatronRepository {
    void add(Patron patron);
    void update(Patron patron);
    Optional<Patron> findById(int patronId);
    List<Patron> findAll();
}
