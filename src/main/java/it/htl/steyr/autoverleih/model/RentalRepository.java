package it.htl.steyr.autoverleih.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

    List<Rental> findAllByActive(boolean active);
}
