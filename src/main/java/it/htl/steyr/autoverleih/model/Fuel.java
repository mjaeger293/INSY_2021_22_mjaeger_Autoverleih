package it.htl.steyr.autoverleih.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "mjaeger_fuel")
public class Fuel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", nullable = false)
    private String fuelType;

    @OneToMany(mappedBy = "fuel", fetch = FetchType.LAZY)
    private Set<Car> cars;

    public int getId() {
        return id;
    }

    public String getFuelType() {
        return fuelType;
    }

    public Set<Car> getCars() {
        return cars;
    }

    @Override
    public String toString() {
        return fuelType;
    }
}
