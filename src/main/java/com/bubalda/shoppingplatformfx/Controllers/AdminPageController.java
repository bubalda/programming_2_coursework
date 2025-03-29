package com.bubalda.shoppingplatformfx.Controllers;

import com.bubalda.shoppingplatformfx.ControllerRoot;
import com.bubalda.shoppingplatformfx.SQLiteFetch;
import javafx.collections.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AdminPageController extends ControllerRoot {

    public Label h1;
    public TableView<Map<String, Object>> tableDisplay;
    public Label error;
    public ComboBox<String> allTables;
    public VBox tableResults;
    public VBox AddRow;
    public VBox UpdateRow;
    public VBox DeleteRow;

    private void loadData(String TB_NAME) { // load once per page, else I kill u
        List<LinkedHashMap<String, Object>> users = SQLiteFetch.getElements(TB_NAME);

        h1.setText("Table " + TB_NAME + ":");

        if (users.isEmpty()) {
            error.setText("No data found in table " + TB_NAME);
            tableResults.setManaged(false);
            tableResults.setVisible(false);
            error.setVisible(true);
            return;
        } else {
            tableResults.setManaged(true);
            tableResults.setVisible(true);
            error.setVisible(false);
        }

        tableDisplay.getColumns().clear();

        for (String key : users.getFirst().keySet()) {
            if (key.equals("UserPassword") || key.equals("UserSalt")) continue; // Some things can see some things cannot
            TableColumn<Map<String, Object>, Object> column = new TableColumn<>(key);
            column.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().get(key)));
            tableDisplay.getColumns().add(column);
        }
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList(users);
        tableDisplay.setItems(data);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> tableNames =
                SQLiteFetch.arbitraryGet("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%';")
                .stream()
                .map(map -> (String) map.get("name"))
                .collect(Collectors.toList());

        allTables.setItems(FXCollections.observableArrayList(tableNames));

        allTables.setOnAction(_ -> {
            String selectedTable = allTables.getSelectionModel().getSelectedItem();
            if (selectedTable != null) loadData(selectedTable);
        });
    }
}
