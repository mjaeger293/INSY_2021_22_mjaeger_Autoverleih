package it.htl.steyr.autoverleih.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "mjaeger_model")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "dailyRate", nullable = false)
    private double dailyRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer")
    private Manufacturer manufacturer;

    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    private Set<Car> cars;


    public Model(String name, double dailyRate, Manufacturer manufacturer) {
        this.name = name;
        this.dailyRate = dailyRate;
        this.manufacturer = manufacturer;
    }

    public Model() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Set<Car> getCars() {
        return cars;
    }
}
