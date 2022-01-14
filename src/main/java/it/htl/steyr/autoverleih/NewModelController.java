package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.repositories.ManufacturerRepository;
import it.htl.steyr.autoverleih.model.Model;
import it.htl.steyr.autoverleih.model.repositories.ModelRepository;
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
public class NewModelController implements IDialogConfirmedPublisher {

    public ComboBox<Manufacturer> manufacturerComboBox;
    public TextField modelNameTextField;
    public TextField dailyRateTextField;

    IDialogConfirmedSubscriber modelController;

    Model editable;

    @Autowired
    ModelRepository modelRepository;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    public void initialize() {
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();

        manufacturerComboBox.getItems().setAll(manufacturers);
    }

    public void saveClicked(ActionEvent actionEvent) {
        String name = modelNameTextField.getText();
        String dailyRateString = dailyRateTextField.getText();
        Manufacturer manufacturer = manufacturerComboBox.getValue();

        if (editable == null) {
            editable = new Model();
        }

        if (manufacturer != null && !name.equals("") && isNumeric(dailyRateString)) {
            editable.setName(name);
            editable.setDailyRate(Double.parseDouble(dailyRateString));
            editable.setManufacturer(manufacturer);

            modelRepository.save(editable);

            // Update TableView
            modelController.windowConfirmed();

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
     * Checks if string is valid number
     * Valid: 5; 35; 28.7; 47.7893
     * Invalid: 49.; .45
     *
     * @param numberString the string to be checked
     * @return boolean
     */
    private static boolean isNumeric(String numberString) {
        if (numberString == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");

        return pattern.matcher(numberString).matches();
    }

    @Override
    public void addSubscriber(IDialogConfirmedSubscriber sub) {
        modelController = sub;
    }

    public void editExistingModel(Model model) {
        this.editable = model;

        modelNameTextField.setText(editable.getName());
        dailyRateTextField.setText(String.valueOf(editable.getDailyRate()));
        manufacturerComboBox.getSelectionModel().select(editable.getManufacturer());

    }
}
