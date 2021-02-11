module pl.kubaretip {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires jnativehook;
    requires java.desktop;
    requires java.logging;
    opens pl.kubaretip;
    exports pl.kubaretip;
    opens pl.kubaretip.controller to javafx.fxml;
    exports pl.kubaretip.controller to javafx.fxml;
}