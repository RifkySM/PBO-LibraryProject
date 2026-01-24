module com.example.pbolibraryproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.pbolibraryproject to javafx.fxml;
    opens com.example.pbolibraryproject.controllers to javafx.fxml;
    exports com.example.pbolibraryproject;
    exports com.example.pbolibraryproject.controllers;
    exports com.example.pbolibraryproject.service;
    exports com.example.pbolibraryproject.models;
}