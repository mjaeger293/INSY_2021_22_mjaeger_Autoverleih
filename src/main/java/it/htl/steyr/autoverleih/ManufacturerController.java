package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.model.Manufacturer;
import it.htl.steyr.autoverleih.model.ManufacturerRepository;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    }

    public void closeClicked(ActionEvent actionEvent) {
    }
}
