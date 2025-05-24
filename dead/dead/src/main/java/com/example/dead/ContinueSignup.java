package com.example.dead;

import com.example.dead.controller.SignupLogin;
import com.example.dead.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.dead.HelloApplication.bounds;

public class ContinueSignup implements Initializable {

    List<Object> objects;

    boolean running = true;

    Thread runner = null;

    static String name;

    static String username;

    static String password;

    static String email;

    static LocalDate birthdate;

    @FXML
    private TextField fieldTextField;

    @FXML
    private AnchorPane fifth;

    @FXML
    private AnchorPane first;

    @FXML
    private AnchorPane fourth;

    @FXML
    private AnchorPane second;

    @FXML
    private AnchorPane third;

    @FXML
    private TextField universityTextField;

    @FXML
    private TextField workPlaceTextField;

    String style;

    @FXML
    void continueButtonClicked(MouseEvent event) throws IOException {
        List<String> strings = new ArrayList<>();
        strings.add("hey");
        strings.add("-1");
        strings.add(name);
        strings.add(username);
        strings.add(password);
        strings.add(email);
        strings.add(birthdate.toString());
        strings.add(universityTextField.getText());
        strings.add(fieldTextField.getText());
        strings.add(workPlaceTextField.getText());
        strings.add(checkSpecialities());
        objects = SignupLogin.getInstance().signup(strings);
        MainScene.user = (User) objects.get(1);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
        HelloApplication.stage.setScene(scene);
    }

    private String checkSpecialities() {
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 0;
        if (!Objects.equals(first.getStyle(), style)) {
            counter++;
            stringBuilder.append("Assistant Manager");
        }
        if (!Objects.equals(second.getStyle(), style)) {
            if (counter == 0) {
                stringBuilder.append("Staff Scientist");
                counter++;
            } else {
                stringBuilder.append(",");
            }
        }
        if (!Objects.equals(third.getStyle(), style)) {
            if (counter == 0) {
                stringBuilder.append("Research Assistant");
                counter++;
            } else {
                stringBuilder.append(" , Research Assistant");
            }
        }
        if (!Objects.equals(fourth.getStyle(), style)) {
            if (counter == 0) {
                stringBuilder.append("Chief Design Engineer");
                counter++;
            } else {
                stringBuilder.append(" , Chief Design Engineer");
            }
        }
        if (!Objects.equals(fifth.getStyle(), style)) {
            if (counter == 0) {
                stringBuilder.append("Statistician");
                counter++;
            } else {
                stringBuilder.append(" , Statistician");
            }
        }
        return stringBuilder.toString();
    }

    @FXML
    void firstClicked(MouseEvent event) {
        if (Objects.equals(first.getStyle(), style)) {
            first.setStyle("-fx-background-color: rgba(248, 233, 255, 0.48)");
        } else {
            first.setStyle(style);
        }
    }

    @FXML
    void secondClicked(MouseEvent event) {
        if (Objects.equals(second.getStyle(), style)) {
            second.setStyle("-fx-background-color: rgba(248, 233, 255, 0.48)");
        } else {
            second.setStyle(style);
        }
    }

    @FXML
    void thirdClicked(MouseEvent event) {
        if (Objects.equals(third.getStyle(), style)) {
            third.setStyle("-fx-background-color: rgba(248, 233, 255, 0.48)");
        } else {
            third.setStyle(style);
        }
    }

    @FXML
    void fourthClicked(MouseEvent event) {
        if (Objects.equals(fourth.getStyle(), style)) {
            fourth.setStyle("-fx-background-color: rgba(248, 233, 255, 0.48)");
        } else {
            fourth.setStyle(style);
        }
    }

    @FXML
    void fifthClicked(MouseEvent event) {
        if (Objects.equals(fifth.getStyle(), style)) {
            fifth.setStyle("-fx-background-color: rgba(248, 233, 255, 0.48)");
        } else {
            fifth.setStyle(style);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        style = first.getStyle();
    }
}
