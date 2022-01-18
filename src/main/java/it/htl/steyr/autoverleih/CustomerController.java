package it.htl.steyr.autoverleih;

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
public class CustomerController extends Administration {

    public TableView<Customer> customerTableView;

    @Autowired
    CustomerRepository customerRepository;

    public void initialize() {
        customerTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadManufacturers();
    }

    public void addClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newCustomer.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            EditCustomerController controller = loader.getController();

            controller.addSubscriber(new IDialogConfirmedSubscriber() {
                @Override
                public void windowConfirmed(Object... o) {
                    loadManufacturers();
                }
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Kunde hinzufügen");

            // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
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

    public void tableViewClicked(MouseEvent mouseEvent) {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedCustomer != null) {
            openEditWindow(mouseEvent, selectedCustomer);
        }
    }

    private void openEditWindow(MouseEvent mouseEvent, Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newCustomer.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            EditCustomerController controller = loader.getController();

            controller.addSubscriber(new IDialogConfirmedSubscriber() {
                @Override
                public void windowConfirmed(Object... o) {
                    loadManufacturers();
                }
            });

            controller.editExistingCustomer(customer);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Kunde hinzufügen");

            // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
