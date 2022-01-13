package it.htl.steyr.autoverleih.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "mjaeger_transmission")
public class Transmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "transmission", fetch = FetchType.LAZY)
    private Set<Car> cars;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Set<Car> getCars() {
        return cars;
    }

    @Override
    public String toString() {
        return type;
    }
}
