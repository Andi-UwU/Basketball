module mpp.basketproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens mpp.basketproject to javafx.fxml;
    exports mpp.basketproject;
    opens mpp.basketproject.controller to javafx.fxml;
    exports mpp.basketproject.controller;
    opens mpp.basketproject.dto to javafx.fxml;
    exports mpp.basketproject.dto;

}