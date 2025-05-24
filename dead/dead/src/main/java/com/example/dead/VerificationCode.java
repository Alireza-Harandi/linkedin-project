package com.example.dead;

import com.example.dead.controller.SignupLogin;
import com.example.dead.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class VerificationCode implements Initializable {

    public static boolean isLogin = false;

    List<Object> objects;

    public static String username;

    public static String email;

    Effect effect;

    @FXML
    private AnchorPane parentAnchorPane;

    @FXML
    private TextField firstDigit;

    @FXML
    private TextField secondDigit;

    @FXML
    private TextField thirdDigit;

    @FXML
    private TextField fourthDigit;

    @FXML
    private TextField fifthDigit;

    @FXML
    private TextField sixthDigit;

    @FXML
    private ImageView designIcon;

    @FXML
    private Text receivedCodeText;

    @FXML
    private Text resendCode;

    @FXML
    private Text announceText;

    @FXML
    private Button confirmButton;

    @FXML
    private Text tierUpText;

    @FXML
    void designIconEntered(MouseEvent event) {
        designIcon.setEffect(new Bloom(.9));
    }

    @FXML
    void receivedCodeTextEntered(MouseEvent event) {
        effect = receivedCodeText.getEffect();
        receivedCodeText.setEffect(new Bloom(0.01));
    }

    @FXML
    void designIconExited(MouseEvent event) {
        designIcon.setEffect(effect);
    }

    @FXML
    void receivedCodeTextExited(MouseEvent event) {
        receivedCodeText.setEffect(effect);
    }

    @FXML
    void resendCodeClicked(MouseEvent event) {
        if (isLogin) {
            System.out.println(SignupLogin.getInstance().checkParameter(username, email).toString());
        } else {
            System.out.println(SignupLogin.getInstance().checkSignup(username, email).toString());
        }
    }

    @FXML
    void returnClicked(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.bounds.getWidth(), HelloApplication.bounds.getHeight());
        HelloApplication.stage.setScene(scene);
    }

    @FXML
    void confirmButtonClicked(MouseEvent event) throws IOException {
        String code = firstDigit.getText() + secondDigit.getText() + thirdDigit.getText() + fourthDigit.getText() + fifthDigit.getText() + sixthDigit.getText();
        objects = SignupLogin.getInstance().verifyCode(username, email, code);
        if (objects.getFirst().equals("verifyCode valid login")) {
            MainScene.user = (User) objects.get(1);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-scene.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.bounds.getWidth(), HelloApplication.bounds.getHeight());
            HelloApplication.stage.setScene(scene);
        } else if (objects.getFirst().equals("verifyCode valid signup")) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("continue-signup.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.bounds.getWidth(), HelloApplication.bounds.getHeight());
            HelloApplication.stage.setScene(scene);
        } else {
            announceText.setVisible(true);
        }
    }

    @FXML
    void resendCodeEntered(MouseEvent event) {
        resendCode.setUnderline(true);
    }

    @FXML
    void resendCodeExited(MouseEvent event) {
        resendCode.setUnderline(false);
    }

    @FXML
    void confirmButtonEntered(MouseEvent event) {
        effect = confirmButton.getEffect();
        confirmButton.setEffect(new DropShadow());
    }

    @FXML
    void confirmButtonExited(MouseEvent event) {
        confirmButton.setEffect(effect);
    }

    @FXML
    void tierUpTextEntered(MouseEvent event) {
        effect = tierUpText.getEffect();
        tierUpText.setEffect(new Bloom(.01));
    }

    @FXML
    void tierUpTextExited(MouseEvent event) {
        tierUpText.setEffect(effect);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        announceText.setVisible(false);
        Platform.runLater(() -> firstDigit.requestFocus());
        TextField[] fields = {firstDigit, secondDigit, thirdDigit, fourthDigit, fifthDigit, sixthDigit};
        for (int i = 0; i < fields.length - 1; i++) {
            int nextIndex = i + 1;
            int finalI = i;
            fields[i].addEventHandler(KeyEvent.KEY_TYPED, event -> {
                String input = event.getCharacter();
                if (!input.isEmpty() && input.matches("\\d")) {
                    fields[finalI].setText(input);
                    fields[nextIndex].requestFocus();
                }
                event.consume();
            });
        }
        fields[fields.length - 1].addEventHandler(KeyEvent.KEY_TYPED, event -> {
            String input = event.getCharacter();
            if (!input.isEmpty() && input.matches("\\d")) {
                fields[fields.length - 1].setText(input);
                Platform.runLater(() -> parentAnchorPane.requestFocus());
            }
            event.consume();
        });
    }
}
