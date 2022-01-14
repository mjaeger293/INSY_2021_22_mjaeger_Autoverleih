package it.htl.steyr.autoverleih.model.repositories;

import it.htl.steyr.autoverleih.model.Car;
import it.htl.steyr.autoverleih.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {

    Car findByLicensePlate(String licensePlate);

    List<Car> findByModel(Model model);

    @Query(value = "SELECT * FROM mjaeger_car WHERE id NOT in (SELECT car FROM mjaeger_rental WHERE (rental_date >= ?1 AND rental_date <= ?2) OR (return_date >= ?1 AND return_date <= ?2) OR (rental_date < ?1 AND return_date > ?2))", nativeQuery = true)
    List<Car> getCarsNotRented(Date rental_date, Date return_date);
}
