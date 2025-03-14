module com.example.generalfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.xerial.sqlitejdbc;
    requires annotations;

    opens com.example.generalfx to javafx.fxml;
    exports com.example.generalfx;
    exports com.example.generalfx.Controllers;
    opens com.example.generalfx.Controllers to javafx.fxml;

    requires org.kordamp.ikonli.core;
}