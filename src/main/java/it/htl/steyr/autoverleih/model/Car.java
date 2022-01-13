package it.htl.steyr.autoverleih.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Set;

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

    @Column(name = "horsePower", nullable = false)
    private int horsePower;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model")
    private Model model;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY) //mappedBy: name der Variable in Rental
    private Set<Rental> rentals;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transmission")
    private Transmission transmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuel")
    private Fuel fuel;


    public Car(Model model, String color, int horsePower, String licensePlate, Transmission transmission, Fuel fuel) {
        this.color = color;
        this.model = model;
        this.horsePower = horsePower;
        this.licensePlate = licensePlate;
        this.transmission = transmission;
        this.fuel = fuel;
    }

    public Car() {
    }

    public int getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getColor() {
        return color;
    }

    public Model getModel() {
        return model;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return model + ", " + color + ", " + transmission + ", " + fuel + ", " + horsePower + ", " + licensePlate;
    }
}
