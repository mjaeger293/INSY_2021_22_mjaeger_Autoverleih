package it.htl.steyr.autoverleih.model.repositories;

import it.htl.steyr.autoverleih.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

    Manufacturer findByName(String name);
}
