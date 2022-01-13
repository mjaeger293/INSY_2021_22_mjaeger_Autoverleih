package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.*;
import it.htl.steyr.autoverleih.model.repositories.CarRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CarController extends Administration implements IDialogConfirmedSubscriber {

    public TableView carTableView;

    @Autowired
    CarRepository carRepository;

    public void initialize() {
        loadCars();
    }

    public void addClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newCar.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            NewCarController controller = loader.getController();

            controller.addSubscriber(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Fahrzeug hinzufügen");

            // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void loadCars() {
        // Remove all columns
        carTableView.getItems().clear();
        carTableView.getColumns().clear();


        TableColumn<Car, Integer> idColumn =
                createTableColumn("ID", "id");

        TableColumn<Car, Model> carModelTableColumn =
                createTableColumn("Modell", "model");

        TableColumn<Car, String> carColorTableColumn =
                createTableColumn("Farbe", "color");

        TableColumn<Car, Integer> carHorsePowerTableColumn =
                createTableColumn("PS", "horsePower");

        TableColumn<Car, String> carLicensePlateColumn =
                createTableColumn("Kennzeichen", "licensePlate");

        TableColumn<Car, Transmission> carTransmissionTableColumn =
                createTableColumn("Getriebe", "transmission");

        TableColumn<Car, Fuel> carFuelTableColumn =
                createTableColumn("Treibstoff", "fuel");


        // Add all columns
        carTableView.getColumns().add(idColumn);
        carTableView.getColumns().add(carModelTableColumn);
        carTableView.getColumns().add(carColorTableColumn);
        carTableView.getColumns().add(carHorsePowerTableColumn);
        carTableView.getColumns().add(carLicensePlateColumn);
        carTableView.getColumns().add(carTransmissionTableColumn);
        carTableView.getColumns().add(carFuelTableColumn);

        carTableView.getItems().addAll(carRepository.findAll());
    }

    @Override
    public void windowConfirmed(Object... o) {
        loadCars();
    }
}
