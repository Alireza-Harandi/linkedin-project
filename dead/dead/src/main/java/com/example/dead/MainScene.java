package com.example.dead;

import com.example.dead.controller.GraphController;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.dead.HelloApplication.bounds;
import static com.example.dead.ProfileScene.chooseImage;

public class MainScene implements Initializable {

    List<Object> objects;

    private Thread runner;

    public static User user;

    private final List<Pane> panes = new ArrayList<>();

    public Pane[] arranges;

    public AnchorPane arrangeAnchorPane;

    @FXML
    private GridPane chatGridPane;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private GridPane fieldGridPane;

    @FXML
    private Text fieldNameText;

    @FXML
    private ScrollPane fieldScrollPane;

    @FXML
    private GridPane generalGridPane;

    @FXML
    private ScrollPane generalScrollPane;

    @FXML
    private GridPane influencersGridPane;

    @FXML
    private TextField searchBox;

    @FXML
    private TextField uniArrange;

    @FXML
    private TextField specsArrange;

    @FXML
    private TextField concsArrange;

    @FXML
    private TextField fieldArrange;

    @FXML
    private TextField workArrange;

    @FXML
    private GridPane universityGridPane;

    @FXML
    private Text universityNameText;

    @FXML
    private ScrollPane universityScrollPane;

    @FXML
    private ImageView userImage;

    @FXML
    private Text usernameText;

    @FXML
    private GridPane wokspaceGridPane;

    @FXML
    private ScrollPane workspaceScrollPane;

    @FXML
    private GridPane searchGridPane;

    @FXML
    void userImageClicked(MouseEvent event) throws IOException {
        ProfileScene.user = user;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
        HelloApplication.stage.setScene(scene);
    }

    @FXML
    void searchBoxClicked(MouseEvent event) {
        searchGridPane.setVisible(true);
        searchGridPane.setDisable(false);
        searchBox.toFront();
    }

    @FXML
    void imageClicked(MouseEvent event) {
        searchGridPane.setVisible(false);
        searchGridPane.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            searchGridPane.getChildren().clear();
            searchGridPane.setVgap(15);
            searchGridPane.setPadding(new Insets(15));
            searchGridPane.setAlignment(Pos.CENTER);
            List<User> users = UserController.getInstance().search(searchBox.getText());
            int row = 0;
            for (User user : users) {
                HBox userRow = new HBox(35);
                ImageView imageView = new ImageView();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-scene.fxml"));
                imageView.setOnMouseClicked(event -> {
                    ProfileScene.user = user;
                    Scene scene;
                    try {
                        scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    HelloApplication.stage.setScene(scene);
                });
                imageView.setImage(chooseImage(user.getId()).getImage());
                imageView.setFitHeight(60);
                imageView.setFitWidth(60);
                userRow.setAlignment(Pos.CENTER_LEFT);
                userRow.getStyleClass().add("row-style");
                Text userName = new Text(user.getName());
                userName.setStyle("-fx-font-weight: bold; -fx-font-size: 22px;");
                userRow.getChildren().add(imageView);
                userRow.getChildren().add(userName);
                userRow.setPadding(new Insets(5));
                userRow.setBackground(new Background(new BackgroundFill(
                        Color.web("rgba(115, 104, 199, 0.48)"), new CornerRadii(15), Insets.EMPTY)));
                userRow.setBorder(new Border(new BorderStroke(
                        Color.web("rgba(80, 72, 150, 0.8)"), BorderStrokeStyle.SOLID,
                        new CornerRadii(15), new BorderWidths(2))));
                searchGridPane.add(userRow, 0, row++);
            }
        });
        usernameText.setText(user.getUserName());
        universityNameText.setText(user.getUniversity());
        fieldNameText.setText(user.getField());
        Platform.runLater(() -> {
            userImage.setImage(chooseImage(user.getId()).getImage());
        });

        objects = UserController.getInstance().getMainScene(user);
        List<User> influencers = ((List<User>) objects.get(1));
        List<User> generalUsers = ((List<User>) objects.get(2));
        List<User> universityUsers = ((List<User>) objects.get(3));
        List<User> workspaceUsers = ((List<User>) objects.get(4));
        List<User> fieldUsers = ((List<User>) objects.get(5));
        Platform.runLater(() -> {
            setInfluencers(influencers, influencersGridPane);
            setUsers(generalUsers, generalGridPane, generalScrollPane);
            setUsers(universityUsers, universityGridPane, universityScrollPane);
            setUsers(workspaceUsers, wokspaceGridPane, workspaceScrollPane);
            setUsers(fieldUsers, fieldGridPane, fieldScrollPane);
        });

        searchGridPane.setVisible(false);
        searchGridPane.setDisable(true);
//        searchGridPane.toBack();
    }

    public void setInfluencers(List<User> users, GridPane influencersGridPane) {
        influencersGridPane.getChildren().clear();
        influencersGridPane.setVgap(15);
        influencersGridPane.setPadding(new Insets(15));
        influencersGridPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < Math.min(users.size(), 4); i++) {
            HBox row = new HBox(50);
            row.setAlignment(Pos.CENTER);
            row.getStyleClass().add("row-style");
            ImageView imageView = chooseImage(users.get(i).getId());
            imageView.setFitWidth(110);
            imageView.setFitHeight(100);
            final User actual = UserController.getInstance().getProfile(users.get(i).getUserName());
            imageView.setOnMouseClicked(mouseEvent -> {
                ProfileScene.user = actual;
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-scene.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HelloApplication.stage.setScene(scene);
            });
            Text text1 = new Text(users.get(i).getUserName());
            text1.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
            row.getChildren().addAll(imageView, text1);
            row.setPadding(new Insets(5));
            row.setBackground(new Background(new BackgroundFill(Color.web("rgba(115, 104, 199, 0.48)"), new CornerRadii(15), Insets.EMPTY)));
            row.setBorder(new Border(new BorderStroke(Color.web("rgba(80, 72, 150, 0.8)"), BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));

            influencersGridPane.add(row, 0, i);
        }
    }

    public void setUsers(List<User> users, GridPane gridPane, ScrollPane scrollPane) {
        HBox hBox = new HBox(25);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(5));
        int index = 0;
        for (User value : users) {
            VBox row = new VBox(8);
            row.getStyleClass().add("row-style");
            ImageView imageView = chooseImage(value.getId());
            final User actual = UserController.getInstance().getProfile(users.get(index).getUserName());
            imageView.setOnMouseClicked(mouseEvent -> {
                ProfileScene.user = actual;
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("profile-scene.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                HelloApplication.stage.setScene(scene);
            });
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            Text text1 = new Text(value.getUserName());
            text1.setStyle("-fx-font-weight: bold");
            row.getChildren().addAll(imageView, text1);
            row.setPadding(new Insets(10));
            row.setBackground(new Background(new BackgroundFill(Color.web("rgba(115, 104, 199, 0.48)"), new CornerRadii(15), Insets.EMPTY)));
            row.setBorder(new Border(new BorderStroke(Color.web("rgba(80, 72, 150, 0.8)"), BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));
            hBox.getChildren().add(row);
            index++;
        }
        gridPane.getChildren().clear();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(hBox);
    }

    public void searchButtonClicked(MouseEvent mouseEvent) throws IOException {
        String code = uniArrange.getText() + " " + fieldArrange.getText() + " " + workArrange.getText() + " " + specsArrange.getText() + " " + concsArrange.getText();
        List<User> users = GraphController.getInstance().customizeSuggestion(user, code);
        setUsers(users, generalGridPane, generalScrollPane);

    }
}

