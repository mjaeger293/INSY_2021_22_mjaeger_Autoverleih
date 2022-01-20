package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.Closeable;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.repositories.ManufacturerRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditManufacturerController extends Closeable implements IDialogConfirmedPublisher {

    IDialogConfirmedSubscriber manufacturerController;

    public TextField manufacturerNameTextField;
    public Button deleteButton;

    Manufacturer editable;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    public void initialize() {
        editable = null;
    }

    public void saveClicked(ActionEvent actionEvent) {
        String name = manufacturerNameTextField.getText();

        if (editable == null) {
            editable = new Manufacturer();
        }

        if (!name.equals("")) {
            editable.setName(name);

            manufacturerRepository.save(editable);

            // Update Table View
            manufacturerController.windowConfirmed();

            closeWindow(actionEvent);
        }
    }

    @Override
    public void addSubscriber(IDialogConfirmedSubscriber sub) {
        manufacturerController = sub;
    }

    public void editExistingManufacturer(Manufacturer manufacturer) {
        this.editable = manufacturer;

        deleteButton.setVisible(true);

        manufacturerNameTextField.setText(editable.getName());
    }

    public void deleteClicked(ActionEvent actionEvent) {
        try {
            manufacturerRepository.delete(editable);
        } catch (Exception e) {
            // If a foreign key of this item still exists, an Exception is thrown

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText(null);
            alert.setContentText("Löschen fehlgeschlagen! Hersteller in Verwendung");

            alert.showAndWait();
        }

        // Update Table View
        manufacturerController.windowConfirmed();

        closeWindow(actionEvent);
    }
}
