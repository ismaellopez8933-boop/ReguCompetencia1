module com.example.autoservicio1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires kernel;
    requires layout;
    requires javafx.graphics;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.example.autoservicio1 to javafx.fxml;
    exports com.example.autoservicio1;
}