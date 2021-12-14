package it.htl.steyr.autoverleih.model;

import javax.persistence.*;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model")
    private Model model;

    @OneToMany(mappedBy = "rental", fetch = FetchType.LAZY)
    private Set<Rental> rentals;


    public Car(String licensePlate, Model model) {
        this.licensePlate = licensePlate;
        this.model = model;
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
}
