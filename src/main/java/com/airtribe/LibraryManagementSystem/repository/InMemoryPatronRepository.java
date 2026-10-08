package com.airtribe.LibraryManagementSystem.repository;

import com.airtribe.LibraryManagementSystem.model.Patron;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryPatronRepository implements PatronRepository {

    private final Map<Integer, Patron> patrons = new LinkedHashMap<>();

    @Override
    public void add(Patron patron) {
        patrons.put(patron.getPatronId(), patron);
    }

    @Override
    public void update(Patron updatedPatron) {
        patrons.put(updatedPatron.getPatronId(), updatedPatron);
    }

    @Override
    public Optional<Patron> findById(int patronId) {
        return Optional.ofNullable(patrons.get(patronId));
    }

    @Override
    public List<Patron> findAll() {
        return new ArrayList<>(patrons.values());
    }
}
