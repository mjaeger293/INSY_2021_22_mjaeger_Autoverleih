package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.model.*;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoverleihApplication {

    public static void main(String[] args) {
        //SpringApplication.run(AutoverleihApplication.class, args);
        Application.launch(JavaFxApplication.class, args);
    }

    /*@Override
    public void run(String... args) throws Exception {
        Manufacturer ma1 = new Manufacturer("Volkswagen");
        manufacturerRepository.save(ma1);

        Manufacturer ma2 = new Manufacturer("Audi");
        manufacturerRepository.save(ma2);


        Model m1 = new Model("Golf", 12.00, ma1);
        modelRepository.save(m1);

        Model m2 = new Model("A1", 15.00, ma2);
        modelRepository.save(m2);

        Model m3 = new Model("Polo", 9.00, ma1);
        modelRepository.save(m3);


        Car c1 = new Car("KI-123VW", m1);
        carRepository.save(c1);

        Car c2 = new Car("SE-825AU", m1);
        carRepository.save(c2);

        Car c3 = new Car("SE-BMD1", m2);
        carRepository.save(c3);
    }*/
}
