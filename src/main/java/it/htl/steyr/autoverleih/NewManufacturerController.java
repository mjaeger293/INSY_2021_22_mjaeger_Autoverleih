package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.repositories.ManufacturerRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewManufacturerController implements IDialogConfirmedPublisher {

    ManufacturerController manufacturerController;

    public TextField manufacturerNameTextField;

    @Autowired
    ManufacturerRepository manufacturerRepository;


    public void saveClicked(ActionEvent actionEvent) {
        String name = manufacturerNameTextField.getText();

        if (!name.equals("")) {
            Manufacturer newManufacturer = new Manufacturer(name);

            manufacturerRepository.save(newManufacturer);

            // Update Table View
            manufacturerController.windowConfirmed();

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

    @Override
    public void addSubscriber(IDialogConfirmedSubscriber sub) {
        manufacturerController = (ManufacturerController) sub;
    }
}
