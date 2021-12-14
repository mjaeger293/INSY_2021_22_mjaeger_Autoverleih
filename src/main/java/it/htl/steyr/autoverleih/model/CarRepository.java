package it.htl.steyr.autoverleih.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {

    Car findByLicensePlate(String licensePlate);

    List<Car> findByModel(Model model);
}
