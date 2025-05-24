package com.example.dead;

import com.example.dead.controller.DataBaseController;
import com.example.dead.controller.FileManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static Stage stage;
    public static javafx.stage.Screen screen = javafx.stage.Screen.getPrimary();
    public static javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();

    @Override
    public void start(Stage stage) throws IOException {
        HelloApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
        stage.setTitle("TierUp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        DataBaseController.getInstance().preProcess("C:\\java.code\\Social-Media\\dead\\dead\\src\\main\\resources\\com\\example\\dead\\files");
        launch();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down the server...");
            FileManager.getInstance().writeUsersToFile(DataBaseController.getInstance().getUsers());
            System.out.println("Database information has been saved to the file.");
            System.exit(0);
        }));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}