module com.example.bibliotheque {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jakarta.xml.bind;
    requires java.desktop;
    //requires org.apache.poi.poi;
    //requires org.apache.poi.ooxml;
    //requires org.apache.commons.collections4;
    //requires org.apache.xmlbeans;
    //requires org.apache.poi.ooxml.schemas;
    //requires mysql.connector.j;
    //requires java.sql;

    opens com.example.bibliotheque to javafx.fxml;
    exports com.example.bibliotheque;
    exports com.example.bibliotheque.Controller;
    opens com.example.bibliotheque.Controller to javafx.fxml;
    opens com.example.bibliotheque.Model to javafx.base, jakarta.xml.bind;

}
