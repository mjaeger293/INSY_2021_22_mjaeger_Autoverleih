package it.htl.steyr.autoverleih.model;

import javax.persistence.*;

@Entity
@Table(name = "mjaeger_car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "licensePlate", nullable = false)
    private String licensePlate;

    @Column(name = "color", nullable = true)
    private String color;

    @Column(name = "rented", nullable = false)
    private boolean rented;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model")
    private Model model;


    public Car(String licensePlate, Model model) {
        this.licensePlate = licensePlate;
        this.model = model;
    }

    public Car() {
    }
}
