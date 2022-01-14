package it.htl.steyr.autoverleih.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mjaeger_rental")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "rental_date", nullable = false)
    private Date rental_date;

    @Column(name = "return_date", nullable = true)
    private Date return_date;

    @Column(name = "active", nullable = false)
    private boolean active = true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car") // Tabellenname in Tabelle mjaeger_rental
    private Car car;

    public Rental() {
    }

    public Rental(Date rental_date, Date return_date, Customer customer, Car car) {
        this.rental_date = rental_date;
        this.return_date = return_date;
        this.customer = customer;
        this.car = car;
    }

    public int getId() {
        return id;
    }

    public Date getRental_date() {
        return rental_date;
    }

    public void setRental_date(Date renal_date) {
        this.rental_date = renal_date;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }


}
