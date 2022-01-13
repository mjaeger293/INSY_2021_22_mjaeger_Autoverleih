package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.*;
import it.htl.steyr.autoverleih.model.repositories.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class NewCarController implements IDialogConfirmedPublisher {

    public ComboBox<Model> modelComboBox;
    public TextField colorTextField;
    public TextField horsePowerTextField;
    public TextField licensePlateDistrictTextField;
    public TextField licensePlateIndividualTextField;
    public ComboBox<Transmission> transmissionComboBox;
    public ComboBox<Fuel> fuelComboBox;

    CarController carController;

    @Autowired
    ModelRepository modelRepository;

    @Autowired
    TransmissionRepository transmissionRepository;

    @Autowired
    FuelRepository fuelRepository;

    @Autowired
    CarRepository carRepository;

    public void initialize() {
        List<Model> models = modelRepository.findAll();
        modelComboBox.getItems().setAll(models);

        List<Transmission> transmissions = transmissionRepository.findAll();
        transmissionComboBox.getItems().setAll(transmissions);

        List<Fuel> fuels = fuelRepository.findAll();
        fuelComboBox.getItems().setAll(fuels);
    }

    public void saveClicked(ActionEvent actionEvent) {
        Model model = modelComboBox.getValue();
        String color = colorTextField.getText();
        String horsePowerString = horsePowerTextField.getText();
        String licensePlate = licensePlateDistrictTextField.getText()  + "-" + licensePlateIndividualTextField.getText();
        Transmission transmission = transmissionComboBox.getValue();
        Fuel fuel = fuelComboBox.getValue();

        if (model != null && !color.equals("") &&
                isNumeric(horsePowerString) &&
                licensePlateCorrectFormat(licensePlate) &&
                transmission != null &&
                fuel != null) {
            Car newCar = new Car(model, color, Integer.parseInt(horsePowerString), licensePlate, transmission, fuel);

            carRepository.save(newCar);

            // Update TableView
            carController.windowConfirmed();

            closeWindow(actionEvent);
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
     *
     * @param licensePlate the license plate to be checked
     * @return boolean
     */
    private static boolean licensePlateCorrectFormat(String licensePlate) {
        if (licensePlate == null) {
            return false;
        }

        // not working yet
        Pattern pattern = Pattern.compile(".*");

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
        carController = (CarController) sub;
    }
}
