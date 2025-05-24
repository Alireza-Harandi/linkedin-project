package com.example.dead;

import com.example.dead.controller.UserController;
import com.example.dead.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.dead.HelloApplication.bounds;

public class ProfileScene implements Initializable {

    boolean connectionValue;

    List<Object> objects;

    public static User user;

    static ImageView newImageView;

    @FXML
    private TextField birthDateField;

    @FXML
    private GridPane connectionsGridPane;

    @FXML
    private ScrollPane connectionsScrollPane;

    @FXML
    private TextField eMailField;

    @FXML
    private TextField connectKeyButton;

    @FXML
    private TextField fieldTextField;

    @FXML
    private TextField deleteAccountKey;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField nameField;

    @FXML
    private Text numberOfConnections;

    @FXML
    private TextField passwordField;

    @FXML
    private Text pureConnectionPercent;

    @FXML
    private GridPane specialitiesGridPane;

    @FXML
    private TextField universityField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField workPlaceField;

    @FXML
    void deleteAccountClicked(MouseEvent event) throws IOException {
        UserController.getInstance().deleteAccount(user);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
        HelloApplication.stage.setScene(scene);
    }

    @FXML
    void connectKeyButtonClicked(MouseEvent event) throws IOException {
        if (connectionValue) {
            UserController.getInstance().disConnect(user.getUserName(), MainScene.user.getUserName());
            connectionValue = false;
            connectKeyButton.setText("Connect");
        } else {
            UserController.getInstance().connect(user.getUserName(), MainScene.user.getUserName());
            connectionValue = true;
            connectKeyButton.setText("Disconnect");
        }
    }

    @FXML
    void returnClicked(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
        HelloApplication.stage.setScene(scene);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameField.setText(user.getName());
        usernameField.setText(user.getUserName());
        birthDateField.setText(user.getDateOfBirth().toString());
        universityField.setText(user.getUniversity());
        fieldTextField.setText(user.getEmail());
        workPlaceField.setText(user.getWorkPlace());
        passwordField.setDisable(true);
        passwordField.setVisible(false);
        eMailField.setVisible(false);
        eMailField.setDisable(true);
        eMailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        imageView.setImage(chooseImage(user.getId()).getImage());
        if (!Objects.equals(user.getUserName(), MainScene.user.getUserName())) {
            deleteAccountKey.setVisible(false);
            deleteAccountKey.setDisable(true);
            if (user.getConnectionId().contains(MainScene.user.getId())) {
                connectKeyButton.setText("Disconnect");
                connectionValue = true;
            } else {
                connectKeyButton.setText("Connect");
                connectionValue = false;
            }
        } else {
            connectKeyButton.setVisible(false);
            connectKeyButton.setDisable(true);
        }


        pureConnectionPercent.setText(String.valueOf(UserController.getInstance().purification(user)));
        numberOfConnections.setText(String.valueOf(user.getConnectionId().size()));

        objects = UserController.getInstance().getAllConnections(user);
        List<User> users = (List<User>) objects.get(1);
        for (int i = 0; i < users.size(); i++) {
            int finalI = i;
            Platform.runLater(() -> {
                VBox row = new VBox(10);
                row.setAlignment(Pos.CENTER);
                row.getStyleClass().add("row-style");
                ImageView imageView = chooseImage(users.get(finalI).getId());
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-scene.fxml"));
                imageView.setOnMouseClicked(e -> {
                    ProfileScene.user = users.get(finalI);
                    Scene scene;
                    try {
                        scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
                    } catch (IOException ee) {
                        throw new RuntimeException(ee);
                    }
                    HelloApplication.stage.setScene(scene);
                });
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                Text text1 = new Text(users.get(finalI).getUserName());
                text1.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
                row.getChildren().addAll(imageView, text1);
                row.setPadding(new Insets(10));
                row.setBackground(new Background(new BackgroundFill(Color.web("rgba(115, 104, 199, 0.48)"), new CornerRadii(15), Insets.EMPTY)));
                row.setBorder(new Border(new BorderStroke(Color.web("rgba(80, 72, 150, 0.8)"), BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));
                connectionsGridPane.add(row, 0, finalI);
            });
        }
        connectionsGridPane.setVgap(20);
        connectionsScrollPane.setFitToWidth(true);
        connectionsScrollPane.setContent(connectionsGridPane);
        setSpecialities();

    }

    public void setSpecialities() {
        specialitiesGridPane.setVgap(15);
        specialitiesGridPane.setPadding(new Insets(15));
        specialitiesGridPane.setAlignment(Pos.CENTER);

        Object[] specs = user.getSpecialties().toArray();
        for (int i = 0; i < Math.min(4, specs.length); i++) {
            HBox row = new HBox(50);
            row.setAlignment(Pos.CENTER);
            row.getStyleClass().add("row-style");
            Text text1 = new Text((String) specs[i]);
            text1.setStyle("-fx-font-weight: bold; -fx-font-size: 22px;");
            row.getChildren().addAll(text1);
            row.setPadding(new Insets(5));
            row.setBackground(new Background(new BackgroundFill(Color.web("rgba(115, 104, 199, 0.48)"), new CornerRadii(15), Insets.EMPTY)));
            row.setBorder(new Border(new BorderStroke(Color.web("rgba(80, 72, 150, 0.8)"), BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));
            specialitiesGridPane.add(row, 0, i);
        }
    }

    public static ImageView chooseImage(long id) {
        id = id % 4;
        ImageView imageView = new ImageView();
        switch ((int) id) {
            case 0 -> {
                imageView.setImage(new Image(Objects.requireNonNull(HelloApplication.class.getResource("/com/example/dead/profiles/Avatar1.png")).toExternalForm()));
            }
            case 1 -> {
                imageView.setImage(new Image(Objects.requireNonNull(HelloApplication.class.getResource("/com/example/dead/profiles/Avatar2.png")).toExternalForm()));
            }
            case 2 -> {
                imageView.setImage(new Image(Objects.requireNonNull(HelloApplication.class.getResource("/com/example/dead/profiles/Avatar3.png")).toExternalForm()));
            }
            case 3 -> {
                imageView.setImage(new Image(Objects.requireNonNull(HelloApplication.class.getResource("/com/example/dead/profiles/Avatar4.png")).toExternalForm()));
            }
        }
        return imageView;
    }
}
