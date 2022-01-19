package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.Rental;
import it.htl.steyr.autoverleih.model.repositories.ManufacturerRepository;
import it.htl.steyr.autoverleih.model.repositories.RentalRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

@Component
public class EndRentalController implements IDialogConfirmedPublisher {

    public TextField drivenKilometersTextField;

    IDialogConfirmedSubscriber subscriber;

    Rental selectedRental;

    @Autowired
    RentalRepository rentalRepository;

    public void submitClicked(ActionEvent actionEvent) {
        String drivenKilometersString = drivenKilometersTextField.getText();

        try {
            int drivenKilometers = Integer.parseInt(drivenKilometersString);

            if (drivenKilometers > 0) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyyy");
                double debt = drivenKilometers * 0.21 +
                        selectedRental.getCar().getModel().getDailyRate() * Math.round((selectedRental.getReturn_date().getTime() - selectedRental.getRental_date().getTime()) / 8.64e+7);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Zu zahlender Betrag");
                alert.setHeaderText(null);
                alert.setContentText("Der zu zahlende Betrag beträgt " + debt);

                alert.showAndWait();

                selectedRental.setActive(false);
                rentalRepository.save(selectedRental);

                subscriber.windowConfirmed();

                closeWindow(actionEvent);
            }
        } catch (NumberFormatException e) {
            // Do nothing
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
        subscriber = sub;
    }

    public void setSelectedRental(Rental rental) {
        selectedRental = rental;
    }
}
