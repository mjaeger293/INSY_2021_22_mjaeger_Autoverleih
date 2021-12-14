package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.ManufacturerRepository;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoverleihApplication implements CommandLineRunner {

    @Autowired
    ManufacturerRepository manufacturerRepository;

    public static void main(String[] args) {
        SpringApplication.run(AutoverleihApplication.class, args);
        //Application.launch(JavaFxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Manufacturer m = new Manufacturer("Volkswagen");
        manufacturerRepository.save(m);
    }
}
