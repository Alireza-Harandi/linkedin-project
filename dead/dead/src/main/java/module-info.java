module com.example.dead {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.json;
    requires java.mail;


    opens com.example.dead to javafx.fxml;
    exports com.example.dead;
}