package it.htl.steyr.autoverleih.model.repositories;

import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Integer>  {

    Model findByName(String name);

    List<Model> findByManufacturer(Manufacturer manufacturer);
}
