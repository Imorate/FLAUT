module FLAUT {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires static lombok;

    opens ir.imorate to javafx.fxml;
    exports ir.imorate;
}