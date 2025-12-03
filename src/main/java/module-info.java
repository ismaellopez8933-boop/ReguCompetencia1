module com.example.autoservicio1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.github.librepdf.openpdf;
    requires kernel;
    requires layout;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires com.opencsv;


    opens com.example.autoservicio1 to javafx.fxml;
    exports com.example.autoservicio1;
}