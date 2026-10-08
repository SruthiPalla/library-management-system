package com.airtribe.LibraryManagementSystem.service;

import com.airtribe.LibraryManagementSystem.exception.PatronNotFoundException;
import com.airtribe.LibraryManagementSystem.model.Patron;
import com.airtribe.LibraryManagementSystem.repository.PatronRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PatronService {

    private static final Logger logger =
            LoggerFactory.getLogger(PatronService.class);

    private final PatronRepository patronRepository;

    public PatronService(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    public void addPatron(Patron patron) {

        patronRepository.add(patron);

        logger.info("Patron added successfully: {}",
                patron.getPatronId());
    }

    public void updatePatron(Patron patron) {

        patronRepository.findById(patron.getPatronId())
                .orElseThrow(() ->
                        new PatronNotFoundException(
                                "Patron not found with ID: "
                                        + patron.getPatronId()));

        patronRepository.update(patron);

        logger.info("Patron updated successfully: {}",
                patron.getPatronId());
    }

    public Patron getPatronById(int patronId) {

        return patronRepository.findById(patronId)
                .orElseThrow(() ->
                        new PatronNotFoundException(
                                "Patron not found with ID: "
                                        + patronId));
    }

    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }
}