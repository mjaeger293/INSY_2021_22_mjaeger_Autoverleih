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

    @Column(name = "iban", nullable = false)
    private String iban;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<Rental> rentals;

    public Customer(String name, String firstname, String email, String address, int zipCode, String city, String iban) {
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.iban = iban;
    }

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }



    @Override
    public String toString() {
        return id + " | " + name;
    }


}
