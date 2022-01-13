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
public class CarController implements IDialogConfirmedSubscriber {

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


        TableColumn<Car, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Car, Model> carModelTableColumn = new TableColumn<>("Modell");
        carModelTableColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Car, String> carColorTableColumn = new TableColumn<>("Farbe");
        carColorTableColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        TableColumn<Car, Integer> carHorsePowerTableColumn = new TableColumn<>("PS");
        carHorsePowerTableColumn.setCellValueFactory(new PropertyValueFactory<>("horsePower"));

        TableColumn<Car, String> carLicensePlateColumn = new TableColumn<>("Kennzeichen");
        carLicensePlateColumn.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));

        TableColumn<Car, Transmission> carTransmissionTableColumn = new TableColumn<>("Getriebe");
        carTransmissionTableColumn.setCellValueFactory(new PropertyValueFactory<>("transmission"));

        TableColumn<Car, Fuel> carFuelTableColumn = new TableColumn<>("Treibstoff");
        carFuelTableColumn.setCellValueFactory(new PropertyValueFactory<>("fuel"));


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
