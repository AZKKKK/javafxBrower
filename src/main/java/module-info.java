module project.javafxbrowser {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens project.javafxbrowser to javafx.fxml;
    exports project.javafxbrowser;
}