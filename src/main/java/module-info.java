module com.bubalda.shoppingplatformfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;

//        requires org.controlsfx.controls;
//        requires com.dlsc.formsfx;
//        requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
//        requires org.kordamp.bootstrapfx.core;
    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires org.kordamp.ikonli.bootstrapicons;

    opens com.bubalda.shoppingplatformfx to javafx.fxml;
    exports com.bubalda.shoppingplatformfx;
    exports com.bubalda.shoppingplatformfx.Controllers;
    opens com.bubalda.shoppingplatformfx.Controllers to javafx.fxml;
}