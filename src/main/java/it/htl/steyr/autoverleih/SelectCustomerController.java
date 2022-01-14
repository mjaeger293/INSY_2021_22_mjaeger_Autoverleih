package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Customer;
import it.htl.steyr.autoverleih.model.repositories.CustomerRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SelectCustomerController extends Administration implements IDialogConfirmedPublisher {

    public TableView<Customer> customerTableView;

    private IDialogConfirmedSubscriber subscriber;

    @Autowired
    CustomerRepository customerRepository;

    public void initialize() {
        loadManufacturers();
    }

    public void closeClicked(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    private void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private void loadManufacturers() {
        // Remove all columns
        customerTableView.getItems().clear();
        customerTableView.getColumns().clear();


        TableColumn<Customer, Integer> idColumn =
                createTableColumn("ID", "id");

        TableColumn<Customer, String> lastnameColumn =
                createTableColumn("Nachname", "name");

        TableColumn<Customer, String> firstnameColumn =
                createTableColumn("Vorname", "firstname");

        TableColumn<Customer, String> emailColumn =
                createTableColumn("Email", "email");

        TableColumn<Customer, String> streetColumn =
                createTableColumn("Strasse", "address");

        TableColumn<Customer, String> zipColumn =
                createTableColumn("PLZ", "zipCode");

        TableColumn<Customer, String> cityColumn =
                createTableColumn("Stadt", "city");

        TableColumn<Customer, String> ibanColumn =
                createTableColumn("IBAN", "iban");

        customerTableView.getColumns().add(idColumn);
        customerTableView.getColumns().add(lastnameColumn);
        customerTableView.getColumns().add(firstnameColumn);
        customerTableView.getColumns().add(emailColumn);
        customerTableView.getColumns().add(streetColumn);
        customerTableView.getColumns().add(zipColumn);
        customerTableView.getColumns().add(cityColumn);
        customerTableView.getColumns().add(ibanColumn);

        customerTableView.getItems().addAll(customerRepository.findAll());
    }

    public void tableViewClicked(MouseEvent mouseEvent) {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedCustomer != null) {
            subscriber.windowConfirmed(selectedCustomer);

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @Override
    public void addSubscriber(IDialogConfirmedSubscriber sub) {
        this.subscriber = sub;
    }
}
