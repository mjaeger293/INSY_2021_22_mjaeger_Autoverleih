package it.htl.steyr.autoverleih.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "mjaeger_rental")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "rental_date", nullable = false)
    private Timestamp renal_date;

    @Column(name = "return_date", nullable = true)
    private Timestamp return_date;

    @Column(name = "active", nullable = false)
    private boolean active;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car")
    private Car car;
}