package it.htl.steyr.autoverleih;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public abstract class Administration {
    /**
     * This method creates a Column for a TableView and assigns a property.
     *
     * @param columnName     header-name of the property stored in the column
     * @param propertyName   name of the property stored in the column
     * @param <Class>        class of the object stored in the TableView
     * @param <PropertyType> type of the property stored in the column
     * @return column for the TableView
     */
    protected static <Class, PropertyType> TableColumn<Class, PropertyType> createTableColumn(String columnName, String propertyName) {
        TableColumn<Class, PropertyType> tableColumn = new TableColumn<>(columnName);
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(propertyName));

        return tableColumn;
    }
}
