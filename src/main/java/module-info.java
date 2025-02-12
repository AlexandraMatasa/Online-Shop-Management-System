module com.example.test2javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.test2javafx to javafx.fxml;
    exports com.example.test2javafx;
}