package it.htl.steyr.autoverleih.model.repositories;

import it.htl.steyr.autoverleih.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

    List<Rental> findAllByActive(boolean active);
}
