package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.Administration;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Customer;
import it.htl.steyr.autoverleih.model.repositories.CustomerRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class SelectCustomerControllerParent extends Administration implements IDialogConfirmedPublisher {

    public TableView<Customer> customerTableView;

    protected IDialogConfirmedSubscriber subscriber;

    @Autowired
    CustomerRepository customerRepository;

    public void initialize() {
        customerTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadManufacturers();

        customerTableView.setPlaceholder(new Label("Keine Kunden gefunden"));
    }

    private void loadManufacturers() {
        // Remove all columns
        customerTableView.getItems().clear();
        customerTableView.getColumns().clear();


        TableColumn<Customer, Integer> idColumn =
                createTableColumn("ID", "id");
        idColumn.setMinWidth(30);
        idColumn.setMaxWidth(80);

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
        zipColumn.setMinWidth(40);
        zipColumn.setMaxWidth(70);

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

    public abstract void tableViewClicked(MouseEvent mouseEvent);

    @Override
    public void addSubscriber(IDialogConfirmedSubscriber sub) {
        this.subscriber = sub;
    }
}
