module com.example.ecommerce_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens app to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.fxml;
    opens service to javafx.fxml;
    opens util to javafx.fxml;

    exports app;
    exports controller;
    exports model;
    exports service;
    exports util;
}