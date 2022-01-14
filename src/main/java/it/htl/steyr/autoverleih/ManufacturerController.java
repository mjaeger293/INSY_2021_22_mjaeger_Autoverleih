package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.repositories.ManufacturerRepository;
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
public class ManufacturerController extends Administration {

    public TableView<Manufacturer> manufacturerTableView;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    public void initialize() {
        loadManufacturers();
    }

    public void addClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newManufacturer.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            EditManufacturerController controller = loader.getController();

            controller.addSubscriber(new IDialogConfirmedSubscriber() {
                @Override
                public void windowConfirmed(Object... o) {
                    loadManufacturers();
                }
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Hersteller hinzufügen");

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
        manufacturerTableView.getItems().clear();
        manufacturerTableView.getColumns().clear();


        TableColumn<Manufacturer, Integer> idColumn =
                createTableColumn("ID", "id");

        TableColumn<Manufacturer, String> manufacturerNameColumn =
                createTableColumn("Hersteller", "name");


        manufacturerTableView.getColumns().add(idColumn);
        manufacturerTableView.getColumns().add(manufacturerNameColumn);

        manufacturerTableView.getItems().addAll(manufacturerRepository.findAll());
    }

    public void tableViewClicked(MouseEvent mouseEvent) {
        Manufacturer selectedManufacturer = manufacturerTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedManufacturer != null) {
            openEditWindow(mouseEvent, selectedManufacturer);
        }
    }

    private void openEditWindow(MouseEvent mouseEvent, Manufacturer manufacturer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newManufacturer.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            EditManufacturerController controller = loader.getController();

            controller.addSubscriber(new IDialogConfirmedSubscriber() {
                @Override
                public void windowConfirmed(Object... o) {
                    loadManufacturers();
                }
            });

            controller.editExistingManufacturer(manufacturer);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Hersteller hinzufügen");

            // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
