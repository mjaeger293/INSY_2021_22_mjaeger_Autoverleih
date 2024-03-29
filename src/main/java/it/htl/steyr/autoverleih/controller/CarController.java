package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.Administration;
import it.htl.steyr.autoverleih.JavaFxApplication;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.*;
import it.htl.steyr.autoverleih.model.repositories.CarRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CarController extends Administration {

    public TableView<Car> carTableView;

    @Autowired
    CarRepository carRepository;

    public void initialize() {
        carTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadCars();

        carTableView.setPlaceholder(new Label("Keine Fahrzeuge gefunden"));
    }

    public void addClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newCar.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            EditCarController controller = loader.getController();

            controller.addSubscriber(new IDialogConfirmedSubscriber() {
                @Override
                public void windowConfirmed(Object... o) {
                    loadCars();
                }
            });

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

    public void loadCars() {
        // Remove all columns
        carTableView.getItems().clear();
        carTableView.getColumns().clear();


        TableColumn<Car, Integer> idColumn =
                createTableColumn("ID", "id");
        idColumn.setMinWidth(30);
        idColumn.setMaxWidth(80);

        TableColumn<Car, Model> carModelTableColumn =
                createTableColumn("Modell", "model");

        TableColumn<Car, String> carColorTableColumn =
                createTableColumn("Farbe", "color");

        TableColumn<Car, Integer> carHorsePowerTableColumn =
                createTableColumn("PS", "horsePower");
        carHorsePowerTableColumn.setMinWidth(40);
        carHorsePowerTableColumn.setMaxWidth(60);

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

    public void tableViewClicked(MouseEvent mouseEvent) {
        Car selectedCar = carTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedCar != null) {
            openEditWindow(mouseEvent, selectedCar);
        }
    }

    private void openEditWindow(MouseEvent mouseEvent, Car car) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newCar.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            EditCarController controller = loader.getController();

            controller.addSubscriber(new IDialogConfirmedSubscriber() {
                @Override
                public void windowConfirmed(Object... o) {
                    loadCars();
                }
            });

            controller.editExistingCar(car);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Fahrzeug hinzufügen");

            // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
