package it.htl.steyr.autoverleih.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

    Manufacturer findByName(String name);
}
