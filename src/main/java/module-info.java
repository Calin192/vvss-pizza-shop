module pizzashop {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;

    opens pizzashop.model;
    exports pizzashop.model;
    opens pizzashop to javafx.fxml;
    exports pizzashop;
    opens pizzashop.controller to javafx.fxml;
    exports pizzashop.controller;



    exports pizzashop.repository;
    exports pizzashop.service;
    opens pizzashop.service;
    opens pizzashop.repository;
    exports pizzashop.validator;
    opens pizzashop.validator;

}


