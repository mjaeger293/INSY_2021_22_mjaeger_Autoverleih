package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.Model;
import it.htl.steyr.autoverleih.model.ModelRepository;
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
public class ModelController implements IDialogConfirmedSubscriber{

    public TableView modelTableView;

    @Autowired
    ModelRepository modelRepository;

    public void initialize() {
        loadModels();
    }

    public void addClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newModel.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            NewModelController controller = loader.getController();

            controller.addSubscriber(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modell hinzufügen");

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

    public void loadModels() {
        // Remove all columns
        modelTableView.getItems().clear();
        modelTableView.getColumns().clear();


        TableColumn<Model, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Model, Manufacturer> modelManufacturerTableColumn = new TableColumn<>("Hersteller");
        modelManufacturerTableColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));

        TableColumn<Model, String> modelNameColumn = new TableColumn<>("Modell");
        modelNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Model, Double> modelDailyRateColumn = new TableColumn<>("Tagessatz");
        modelDailyRateColumn.setCellValueFactory(new PropertyValueFactory<>("dailyRate"));


        // Add all columns
        modelTableView.getColumns().add(idColumn);
        modelTableView.getColumns().add(modelManufacturerTableColumn);
        modelTableView.getColumns().add(modelNameColumn);
        modelTableView.getColumns().add(modelDailyRateColumn);

        modelTableView.getItems().addAll(modelRepository.findAll());
    }

    @Override
    public void windowConfirmed(Object... o) {
        loadModels();
    }
}
