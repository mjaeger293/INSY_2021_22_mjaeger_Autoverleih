package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.model.Customer;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class SelectCustomerController extends SelectCustomerControllerParent implements IDialogConfirmedPublisher {

    public void tableViewClicked(MouseEvent mouseEvent) {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedCustomer != null) {
            subscriber.windowConfirmed(selectedCustomer);

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
