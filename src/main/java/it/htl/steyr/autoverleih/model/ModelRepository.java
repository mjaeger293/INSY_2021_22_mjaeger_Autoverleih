package it.htl.steyr.autoverleih.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Integer>  {

    Model findByName(String name);

    List<Model> findByManufacturer(Manufacturer manufacturer);
}
