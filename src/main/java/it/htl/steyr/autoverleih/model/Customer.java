package it.htl.steyr.autoverleih.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "mjaeger_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "lastname", nullable = false)
    private String name;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "zip", nullable = false)
    private int zipCode;

    @Column(name = "city", nullable = false)
    private String city;


    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<Rental> rentals;

    @Override
    public String toString() {
        return id + " | " + name;
    }
}
