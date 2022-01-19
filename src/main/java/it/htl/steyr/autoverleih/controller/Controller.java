package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.Administration;
import it.htl.steyr.autoverleih.JavaFxApplication;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.*;
import it.htl.steyr.autoverleih.model.repositories.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

    Car selectedCar;
    Manufacturer selectedManufacturer;
    Model selectedModel;
    Customer selectedCustomer;
    Date rentalDate;
    Date returnDate;

    @Autowired
    CarRepository carRepository;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Autowired
    ModelRepository modelRepository;

    @Autowired
    RentalRepository rentalRepository;

    public void initialize() {
        manufacturerFilterComboBox.getItems().setAll(manufacturerRepository.findAll());

        availableCarsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

    public void manageRentalsClicked(ActionEvent actionEvent) {
        loadWindow("selectCustomerRental.fxml", "Kunde auswählen", actionEvent);
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
            rentalDate = Date.from(Instant.from(localRentalDate.atStartOfDay(ZoneId.systemDefault())));
            returnDate = Date.from(Instant.from(localReturnDate.atStartOfDay(ZoneId.systemDefault())));

            if (rentalDate.getTime() < returnDate.getTime()) {
                // Set return date time to 23:59:59
                returnDate.setTime(returnDate.getTime() + 86399000);

                availableCars = carRepository.getCarsNotRented(rentalDate, returnDate);

                availableCarsTableView.getItems().clear();

                if (availableCars.size() > 0) {
                    availableCarsTableView.getItems().addAll(availableCars);
                }

                if (selectedManufacturer != null) {
                    if (selectedModel != null) {
                        modelFilterChanged(null);
                    } else {
                        manufacturerFilterChanged(null);
                    }
                }
            } else {
                rentalDate = null;
                returnDate = null;

                availableCarsTableView.getItems().clear();
                availableCars = null;
            }
        }
    }

    public void manufacturerFilterChanged(ActionEvent actionEvent) {
        selectedManufacturer = manufacturerFilterComboBox.getSelectionModel().getSelectedItem();

        if (selectedManufacturer != null) {
            modelFilterComboBox.getItems().setAll(modelRepository.findByManufacturer(selectedManufacturer));
            modelFilterComboBox.setDisable(false);

            availableCarsTableView.getItems().clear();

            if (availableCars != null) {
                for (Car c : availableCars) {
                    if (c.getModel().getManufacturer().getId() == selectedManufacturer.getId()) {
                        availableCarsTableView.getItems().add(c);
                    }
                }
            }
        }
    }

    public void modelFilterChanged(ActionEvent actionEvent) {
        selectedModel = modelFilterComboBox.getSelectionModel().getSelectedItem();

        if (selectedModel != null) {
            availableCarsTableView.getItems().clear();

            if (availableCars != null) {
                for (Car c : availableCars) {
                    if (c.getModel().getId() == selectedModel.getId()) {
                        availableCarsTableView.getItems().add(c);
                    }
                }
            }
        }
    }

    public void clearFiltersClicked(ActionEvent actionEvent) {
        modelFilterComboBox.setDisable(true);
        modelFilterComboBox.getItems().clear();
        selectedModel = null;

        manufacturerFilterComboBox.getSelectionModel().clearSelection();
        selectedManufacturer = null;

        if (availableCars != null) {
            availableCarsTableView.getItems().setAll(availableCars);
        }
    }

    public void availableCarsTableViewClicked(MouseEvent mouseEvent) {
        selectedCar = availableCarsTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedCar != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("selectCustomer.fxml"));
                loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
                Parent root = loader.load();
                SelectCustomerController controller = loader.getController();

                controller.addSubscriber(new IDialogConfirmedSubscriber() {
                    @Override
                    public void windowConfirmed(Object... o) {
                        selectedCustomer = (Customer) o[0];

                        Rental newRental = new Rental(rentalDate, returnDate, selectedCustomer, selectedCar);
                        rentalRepository.save(newRental);

                        clearInputField();
                    }
                });

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Kunde auswählen");

                // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearInputField() {
        selectedManufacturer = null;
        selectedModel = null;
        selectedCar = null;
        selectedCustomer = null;
        rentalDate = null;
        returnDate = null;

        manufacturerFilterComboBox.getSelectionModel().clearSelection();
        modelFilterComboBox.getSelectionModel().clearSelection();

        rentalDatePicker.setValue(null);
        returnDatePicker.setValue(null);

        availableCarsTableView.getItems().clear();
    }
}
