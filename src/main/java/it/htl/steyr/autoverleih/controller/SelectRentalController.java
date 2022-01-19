package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.Administration;
import it.htl.steyr.autoverleih.JavaFxApplication;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Car;
import it.htl.steyr.autoverleih.model.Customer;
import it.htl.steyr.autoverleih.model.Rental;
import it.htl.steyr.autoverleih.model.repositories.RentalRepository;
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
import java.util.Date;

@Component
public class SelectRentalController extends Administration implements IDialogConfirmedPublisher {

    public TableView<Rental> rentalTableView;

    protected IDialogConfirmedSubscriber subscriber;

    Rental selectedRental;

    private Customer selectedCustomer;

    @Autowired
    RentalRepository rentalRepository;

    public void initialize() {
        rentalTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // The TableView must not be filled during initialize as the selected customer can only be
        // set after initialize at the earliest
        //loadRentals();
    }

    public void closeClicked(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    private void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void loadRentals() {
        // Remove all columns
        rentalTableView.getItems().clear();
        rentalTableView.getColumns().clear();


        TableColumn<Rental, Integer> idColumn =
                createTableColumn("ID", "id");

        TableColumn<Rental, Date> rentalDateColumn =
                createTableColumn("Verleihdatum", "rental_date");

        TableColumn<Rental, Date> returnDateColumn =
                createTableColumn("Rückgabedatum", "return_date");

        TableColumn<Rental, Car> carColumn =
                createTableColumn("Fahrzeug", "car");

        rentalTableView.getColumns().add(idColumn);
        rentalTableView.getColumns().add(rentalDateColumn);
        rentalTableView.getColumns().add(returnDateColumn);
        rentalTableView.getColumns().add(carColumn);

        rentalTableView.getItems().addAll(rentalRepository.findByCustomerAndActive(selectedCustomer, true));
    }

    public void tableViewClicked(MouseEvent mouseEvent) {
        selectedRental = rentalTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedRental != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("endRental.fxml"));
                loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
                Parent root = loader.load();
                EndRentalController controller = loader.getController();

                // Set the selected customer and load the table view. This cannot be done via initialize.
                controller.setSelectedRental(selectedRental);

                controller.addSubscriber(new IDialogConfirmedSubscriber() {
                    @Override
                    public void windowConfirmed(Object... o) {
                        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                        stage.close();
                    }
                });

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Fahrzeug zurückbringen");

                // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());

                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addSubscriber(IDialogConfirmedSubscriber sub) {
        this.subscriber = sub;
    }

    public void setSelectedCustomer(Customer customer) {
        selectedCustomer = customer;
    }
}
