package it.htl.steyr.autoverleih;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * Contains all variations to close a window
 */
public abstract class Closeable {
    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void cancelClicked(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    public void closeClicked(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }
}
