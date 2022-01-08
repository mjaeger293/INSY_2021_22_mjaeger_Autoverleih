package it.htl.steyr.autoverleih.model.repositories;

import it.htl.steyr.autoverleih.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer>  {

}
