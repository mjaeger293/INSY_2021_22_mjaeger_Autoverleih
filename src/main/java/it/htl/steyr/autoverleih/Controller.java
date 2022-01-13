package it.htl.steyr.autoverleih;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Controller {

    public void CloseItemClicked(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void manageManufactersClicked(ActionEvent actionEvent) {
        loadWindow("manufacturer.fxml", "Hersteller verwalten", actionEvent);
    }

    public void manageModelsClicked(ActionEvent actionEvent) {
        loadWindow("model.fxml", "Modelle verwalten", actionEvent);
    }

    public void manageCarsClicked(ActionEvent actionEvent) {
        loadWindow("car.fxml", "Fahrzeuge verwalten", actionEvent);
    }

    public void manageCustomersClicked(ActionEvent actionEvent) {
        loadWindow("customer.fxml", "Kunden verwalten", actionEvent);
    }


    private void loadWindow(String layoutFileName, String title, ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(layoutFileName));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);

            // Hauptfenster soll inaktiv sein, solange Konto ausgewählt wird.
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow());

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
