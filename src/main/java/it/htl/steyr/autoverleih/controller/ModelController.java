package it.htl.steyr.autoverleih.controller;

import it.htl.steyr.autoverleih.Administration;
import it.htl.steyr.autoverleih.JavaFxApplication;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.Model;
import it.htl.steyr.autoverleih.model.repositories.ModelRepository;
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
public class ModelController extends Administration {

    public TableView<Model> modelTableView;

    @Autowired
    ModelRepository modelRepository;

    public void initialize() {
        modelTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        loadModels();
    }

    public void addClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newModel.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            EditModelController controller = loader.getController();

            controller.addSubscriber(new IDialogConfirmedSubscriber() {
                @Override
                public void windowConfirmed(Object... o) {
                    loadModels();
                }
            });

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


        TableColumn<Model, Integer> idColumn =
                createTableColumn("ID", "id");

        TableColumn<Model, Manufacturer> modelManufacturerTableColumn =
                createTableColumn("Hersteller", "manufacturer");

        TableColumn<Model, String> modelNameColumn =
                createTableColumn("Modell", "name");

        TableColumn<Model, Double> modelDailyRateColumn =
                createTableColumn("Tagessatz", "dailyRate");


        // Add all columns
        modelTableView.getColumns().add(idColumn);
        modelTableView.getColumns().add(modelManufacturerTableColumn);
        modelTableView.getColumns().add(modelNameColumn);
        modelTableView.getColumns().add(modelDailyRateColumn);

        modelTableView.getItems().addAll(modelRepository.findAll());
    }

    public void tableViewClicked(MouseEvent mouseEvent) {
        Model selectedModel = modelTableView.getSelectionModel().getSelectedItem();

        if (mouseEvent.getClickCount() == 2 && selectedModel != null) {
            openEditWindow(mouseEvent, selectedModel);
        }
    }

    private void openEditWindow(MouseEvent mouseEvent, Model model) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newModel.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();
            EditModelController controller = loader.getController();

            controller.addSubscriber(new IDialogConfirmedSubscriber() {
                @Override
                public void windowConfirmed(Object... o) {
                    loadModels();
                }
            });

            controller.editExistingModel(model);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modell hinzufügen");

            // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
