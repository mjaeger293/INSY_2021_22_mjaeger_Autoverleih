package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.model.*;
import it.htl.steyr.autoverleih.model.repositories.CarRepository;
import it.htl.steyr.autoverleih.model.repositories.ManufacturerRepository;
import it.htl.steyr.autoverleih.model.repositories.ModelRepository;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class Controller extends Administration {

    public DatePicker rentalDatePicker;
    public DatePicker returnDatePicker;
    public ComboBox<Manufacturer> manufacturerFilterComboBox;
    public ComboBox<Model> modelFilterComboBox;
    public TableView<Car> availableCarsTableView;

    boolean manufacturerFilterVisible = false;
    boolean modelFilterVisible = false;
    List<Car> availableCars;

    Manufacturer selectedManufacturer;
    Model selectedModel;

    @Autowired
    CarRepository carRepository;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Autowired
    ModelRepository modelRepository;

    public void initialize() {
        manufacturerFilterComboBox.getItems().setAll(manufacturerRepository.findAll());

        availableCarsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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
        availableCarsTableView.getColumns().add(idColumn);
        availableCarsTableView.getColumns().add(carModelTableColumn);
        availableCarsTableView.getColumns().add(carColorTableColumn);
        availableCarsTableView.getColumns().add(carHorsePowerTableColumn);
        availableCarsTableView.getColumns().add(carLicensePlateColumn);
        availableCarsTableView.getColumns().add(carTransmissionTableColumn);
        availableCarsTableView.getColumns().add(carFuelTableColumn);
    }

    public void CloseItemClicked(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void manageManufactersClicked(ActionEvent actionEvent) {
        loadWindow("manufacturer.fxml", "Hersteller verwalten", actionEvent);
    }

    public void manageModelsClicked(ActionEvent actionEvent) {
        loadWindow("model.fxml", "Modelle verwalten", actionEvent);
    }

    public void manageCarsClicked(ActionEvent actionEvent) {
        loadWindow("car.fxml", "Fahrzeuge verwalten", actionEvent);
    }

    public void manageCustomersClicked(ActionEvent actionEvent) {
        loadWindow("customer.fxml", "Kunden verwalten", actionEvent);
    }


    private void loadWindow(String layoutFileName, String title, ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(layoutFileName));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);

            // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rentalTimeChanged(ActionEvent actionEvent) {
        LocalDate localRentalDate = rentalDatePicker.getValue();
        LocalDate localReturnDate = returnDatePicker.getValue();

        if (rentalDatePicker.getValue() != null && returnDatePicker.getValue() != null) {
            Date rentalDate = Date.from(Instant.from(localRentalDate.atStartOfDay(ZoneId.systemDefault())));
            Date returnDate = Date.from(Instant.from(localReturnDate.atStartOfDay(ZoneId.systemDefault())));

            availableCars = carRepository.getCarsNotRented(rentalDate, returnDate);

            availableCarsTableView.getItems().clear();

            if (availableCars.size() > 0) {
                availableCarsTableView.getItems().addAll(availableCars);
            }
        }
    }

    public void manufacturerFilterChanged(ActionEvent actionEvent) {
        selectedManufacturer = manufacturerFilterComboBox.getSelectionModel().getSelectedItem();

        if (selectedManufacturer != null) {
            modelFilterComboBox.getItems().setAll(modelRepository.findByManufacturer(selectedManufacturer));
            modelFilterComboBox.setDisable(false);

            availableCarsTableView.getItems().clear();

            for (Car c : availableCars) {
                if (c.getModel().getManufacturer().getId() == selectedManufacturer.getId()) {
                    availableCarsTableView.getItems().add(c);
                }
            }
        }
    }

    public void modelFilterChanged(ActionEvent actionEvent) {
        selectedModel = modelFilterComboBox.getSelectionModel().getSelectedItem();

        if (selectedModel != null) {
            availableCarsTableView.getItems().clear();

            for (Car c : availableCars) {
                if (c.getModel().getId() == selectedModel.getId()) {
                    availableCarsTableView.getItems().add(c);
                }
            }
        }
    }

    public void clearFiltersClicked(ActionEvent actionEvent) {
        modelFilterComboBox.setDisable(true);
        modelFilterComboBox.getItems().clear();

        availableCarsTableView.getItems().setAll(availableCars);
    }

    public void availableCarsTableViewClicked(MouseEvent mouseEvent) {
        Car selectedCar = availableCarsTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedCar != null) {
            //openEditWindow(mouseEvent, selectedCar);
        }
    }
}
