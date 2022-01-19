package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.*;
import it.htl.steyr.autoverleih.model.repositories.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class EditCarController implements IDialogConfirmedPublisher {

    public ComboBox<Model> modelComboBox;
    public TextField colorTextField;
    public TextField horsePowerTextField;
    public TextField licensePlateDistrictTextField;
    public TextField licensePlateIndividualTextField;
    public ComboBox<Transmission> transmissionComboBox;
    public ComboBox<Fuel> fuelComboBox;
    public Button deleteButton;
    public Label licensePlateErrorLabel;
    public ComboBox<Manufacturer> manufacturerFilterComboBox;

    IDialogConfirmedSubscriber carController;

    Car editable;

    Manufacturer selectedManufacturer;


    @Autowired
    ModelRepository modelRepository;

    @Autowired
    TransmissionRepository transmissionRepository;

    @Autowired
    FuelRepository fuelRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    public void initialize() {
        manufacturerFilterComboBox.getItems().setAll(manufacturerRepository.findAll());

        List<Model> models = modelRepository.findAll();
        modelComboBox.getItems().setAll(models);

        List<Transmission> transmissions = transmissionRepository.findAll();
        transmissionComboBox.getItems().setAll(transmissions);

        List<Fuel> fuels = fuelRepository.findAll();
        fuelComboBox.getItems().setAll(fuels);

        editable = null;
    }

    public void saveClicked(ActionEvent actionEvent) {
        Model model = modelComboBox.getValue();
        String color = colorTextField.getText();
        String horsePowerString = horsePowerTextField.getText();
        String licensePlate = (licensePlateDistrictTextField.getText()  + "-" + licensePlateIndividualTextField.getText()).toUpperCase();
        Transmission transmission = transmissionComboBox.getValue();
        Fuel fuel = fuelComboBox.getValue();

        if (editable == null) {
            editable = new Car();
        }

        if (model != null && !color.equals("") &&
                isNumeric(horsePowerString) &&
                licensePlateCorrectFormat(licensePlate) &&
                transmission != null &&
                fuel != null)
        {

            editable.setModel(model);
            editable.setColor(color);
            editable.setHorsePower(Integer.parseInt(horsePowerString));
            editable.setLicensePlate(licensePlate);
            editable.setTransmission(transmission);
            editable.setFuel(fuel);


            carRepository.save(editable);

            // Update TableView
            carController.windowConfirmed();

            closeWindow(actionEvent);
        } else if (licensePlateCorrectFormat(licensePlate) || licensePlate.equals("-")) {
            licensePlateErrorLabel.setVisible(false);
        } else {
            licensePlateErrorLabel.setVisible(true);
        }
    }

    public void cancelClicked(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    private void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Checks if a license plate is valid
     * (At least Austrian ones)
     *
     * @param licensePlate the license plate to be checked
     * @return boolean
     */
    private static boolean licensePlateCorrectFormat(String licensePlate) {
        if (licensePlate == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("([A-Z]{1,2})[-]((\\d{1,5} ?[A-Z]{1,3})|([A-Z]{1,6} ?\\d))");

        return pattern.matcher(licensePlate).matches();
    }

    private static boolean isNumeric(String numberString) {
        if (numberString == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\d+");

        return pattern.matcher(numberString).matches();
    }

    @Override
    public void addSubscriber(IDialogConfirmedSubscriber sub) {
        carController = sub;
    }

    public void editExistingCar(Car car) {
        this.editable = car;

        deleteButton.setVisible(true);

        modelComboBox.getSelectionModel().select(editable.getModel());
        colorTextField.setText(editable.getColor());
        horsePowerTextField.setText(String.valueOf(editable.getHorsePower()));
        licensePlateDistrictTextField.setText(editable.getLicensePlate().split("-")[0]);
        licensePlateIndividualTextField.setText(editable.getLicensePlate().split("-")[1]);
        transmissionComboBox.getSelectionModel().select(editable.getTransmission());
        fuelComboBox.getSelectionModel().select(editable.getFuel());
    }

    public void deleteClicked(ActionEvent actionEvent) {
        try {
            carRepository.delete(editable);
        } catch (Exception e) {
            // If a foreign key of this item still exists, an Exception is thrown

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText(null);
            alert.setContentText("Löschen fehlgeschlagen! Auto in Verwendung");

            alert.showAndWait();
        }

        // Update Table View
        carController.windowConfirmed();

        closeWindow(actionEvent);
    }

    public void manufacturerFilterChanged(ActionEvent actionEvent) {
        selectedManufacturer = manufacturerFilterComboBox.getSelectionModel().getSelectedItem();

        if (selectedManufacturer != null) {
            modelComboBox.getItems().setAll(modelRepository.findByManufacturer(selectedManufacturer));
        }
    }

    public void clearFilterClicked(ActionEvent actionEvent) {
        manufacturerFilterComboBox.getSelectionModel().clearSelection();
        selectedManufacturer = null;

        List<Model> models = modelRepository.findAll();
        modelComboBox.getItems().setAll(models);
    }
}
