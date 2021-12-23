package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.ManufacturerRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ManufacturerController {

    public TableView manufacturerTableView;

    @Autowired
    ManufacturerRepository manufacturerRepository;

    public void initialize() {
        TableColumn<Manufacturer, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Manufacturer, String> manufacturerNameColumn = new TableColumn<>("Hersteller");
        manufacturerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        manufacturerTableView.getColumns().add(idColumn);
        manufacturerTableView.getColumns().add(manufacturerNameColumn);

        manufacturerTableView.getItems().addAll(manufacturerRepository.findAll());
    }

    public void addClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newManufacturer.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            NewManufacturerController controller = loader.getController();

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
}
