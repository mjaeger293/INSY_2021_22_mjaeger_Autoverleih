package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.ManufacturerRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewManufacturerController {

    public TextField manufacturerNameTextField;

    @Autowired
    ManufacturerRepository manufacturerRepository;


    public void saveClicked(ActionEvent actionEvent) {
        String name = manufacturerNameTextField.getText();

        if (!name.equals("")) {
            Manufacturer newManufacturer = new Manufacturer(name);

            manufacturerRepository.save(newManufacturer);

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
}
