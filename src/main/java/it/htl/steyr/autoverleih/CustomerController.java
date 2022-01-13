package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.repositories.CustomerRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomerController extends Administration implements IDialogConfirmedSubscriber {

    public TableView customerTableView;

    @Autowired
    CustomerRepository customerRepository;

    public void initialize() {
        loadManufacturers();
    }

    public void addClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newCustomer.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            NewCustomerController controller = loader.getController();

            controller.addSubscriber(this);

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


        TableColumn<Manufacturer, Integer> idColumn =
                createTableColumn("ID", "id");

        TableColumn<Manufacturer, String> lastnameColumn =
                createTableColumn("Nachname", "name");

        TableColumn<Manufacturer, String> firstnameColumn =
                createTableColumn("Vorname", "firstname");

        TableColumn<Manufacturer, String> emailColumn =
                createTableColumn("Email", "email");

        TableColumn<Manufacturer, String> streetColumn =
                createTableColumn("Strasse", "address");

        TableColumn<Manufacturer, String> zipColumn =
                createTableColumn("PLZ", "zipCode");

        TableColumn<Manufacturer, String> cityColumn =
                createTableColumn("Stadt", "city");

        TableColumn<Manufacturer, String> ibanColumn =
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

    @Override
    public void windowConfirmed(Object... o) {
        loadManufacturers();
    }
}
