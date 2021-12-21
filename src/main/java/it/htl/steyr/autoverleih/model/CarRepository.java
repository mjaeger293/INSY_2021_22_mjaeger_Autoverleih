package it.htl.steyr.autoverleih.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {

    Car findByLicensePlate(String licensePlate);

    List<Car> findByModel(Model model);

    //@Query(value = "SELECT * FROM mjaeger_car WHERE id NOT in (SELECT car FROM mjaeger_transactions WHERE rental_date < ?1 AND return_date > ?2 GROUP BY owner)", nativeQuery = true)
    //List<Car> getCarsNotRented(Date rental_date, Date return_date);
}
