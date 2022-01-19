package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.JavaFxApplication;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Customer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SelectCustomerRentalsController extends SelectCustomerControllerParent implements IDialogConfirmedPublisher {

    public void tableViewClicked(MouseEvent mouseEvent) {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedCustomer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("selectRental.fxml"));
                loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
                Parent root = loader.load();
                SelectRentalController controller = loader.getController();

                // Set the selected customer and load the table view. This cannot be done via initialize.
                controller.setSelectedCustomer(selectedCustomer);
                controller.loadRentals();

                controller.addSubscriber(new IDialogConfirmedSubscriber() {
                    @Override
                    public void windowConfirmed(Object... o) {
                        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                        stage.close();
                    }
                });

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Fahrzeug auswählen");

                // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
