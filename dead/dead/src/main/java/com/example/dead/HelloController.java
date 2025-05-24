package com.example.dead;

import com.example.dead.controller.SignupLogin;
import com.example.dead.model.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.dead.HelloApplication.bounds;

public class HelloController implements Initializable {

    List<Object> objects;

    int forgetCount = 0;

    boolean signUp = true;

    Effect effect;

    @FXML
    private Text announceText;

    @FXML
    private Circle secondBall;

    @FXML
    private Circle firstBall;

    @FXML
    private Circle thirdBall;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private ImageView designIcon;

    @FXML
    private TextField eMailField;

    @FXML
    private Text forgetPasswordText;

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpLoginButton;

    @FXML
    private Text signUpLoginText;

    @FXML
    private Text tierUpText;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Text goToSignInUp;


    @FXML
    void goToSignInUpClicked(MouseEvent event) {
        if (signUp) {
            goToSignIn();
            signUp = false;
        } else {
            goToSignUp();
            signUp = true;
        }
    }

    public void goToSignIn() {
        new Thread(() -> {
            eMailField.setOpacity(0);
            nameTextField.setOpacity(0);
            birthDatePicker.setOpacity(0);
            forgetPasswordText.setVisible(true);
            forgetPasswordText.setOpacity(100);
            forgetPasswordText.setDisable(false);
        }).start();
        new Thread(() -> {
            Platform.runLater(() -> {
                goToSignInUp.setText("Don't you have an account? SignUp!");
                signUpLoginText.setText("SignIn");
                signUpLoginButton.setText("SignIn");
            });
        }).start();
    }

    public void goToSignUp() {
        new Thread(() -> {
            eMailField.setOpacity(1);
            nameTextField.setOpacity(1);
            birthDatePicker.setOpacity(1);
            forgetPasswordText.setVisible(false);
            forgetPasswordText.setDisable(true);
            forgetCount = 0;
        }).start();
        new Thread(() -> {
            Platform.runLater(() -> {
                passwordField.setPromptText("Password");
                goToSignInUp.setText("Did you forget your password?");
                signUpLoginText.setText("SignUp");
                signUpLoginButton.setText("SignUp");
            });
        }).start();
    }

    @FXML
    void goToSignInUpEntered(MouseEvent event) {
        goToSignInUp.setUnderline(true);
    }

    @FXML
    void goToSignInUpExited(MouseEvent event) {
        goToSignInUp.setUnderline(false);
    }

    @FXML
    void designIconEntered(MouseEvent event) {
        designIcon.setEffect(new Bloom(.9));
    }

    @FXML
    void designIconExited(MouseEvent event) {
        designIcon.setEffect(null);
    }

    @FXML
    void eMailFieldEntered(MouseEvent event) {
        effect = eMailField.getEffect();
        eMailField.setEffect(new Bloom(.99));
    }

    @FXML
    void eMailFieldExited(MouseEvent event) {
        eMailField.setEffect(effect);
    }

    @FXML
    void forgetPasswordTextClicked(MouseEvent event) {
        if (forgetCount == 0) {
            passwordField.setPromptText("Email");
            forgetCount++;
            new Thread(() -> {
                for (int i = 1; i <= 50; i++) {
                    int finalI = i;
                    Platform.runLater(() -> {
                        forgetPasswordText.setOpacity(1 - ((double) (finalI * 2) / 10));
                    });
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                forgetPasswordText.setDisable(true);
            }).start();
        }
    }

    @FXML
    void signUpLoginButtonClicked(MouseEvent event) throws IOException {
        if (signUp) {
            if (isValidEmail(eMailField.getText())) {
                objects = SignupLogin.getInstance().checkSignup(usernameTextField.getText(), eMailField.getText());
                if (Objects.equals(objects.getFirst(), "checkSignup usernameExists")) {
                    announceText.setText("username exists");
                    announceText.setVisible(true);
                } else {
                    VerificationCode.username = usernameTextField.getText();
                    VerificationCode.email = eMailField.getText();
                    ContinueSignup.username = usernameTextField.getText();
                    ContinueSignup.password = passwordField.getText();
                    ContinueSignup.email = eMailField.getText();
                    ContinueSignup.birthdate = birthDatePicker.getValue();
                    ContinueSignup.name = nameTextField.getText();
                    VerificationCode.isLogin = false;
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("verification-code.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
                    HelloApplication.stage.setScene(scene);
                }
            } else {
                announceText.setText("your email is not valid");
            }
        } else if (forgetCount == 0) {
            objects = SignupLogin.getInstance().login(usernameTextField.getText(), passwordField.getText());
            if (Objects.equals(objects.getFirst(), "login success")) {
                MainScene.user = (User) objects.get(1);
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-scene.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
                HelloApplication.stage.setScene(scene);
            } else if (Objects.equals(objects.getFirst(), "login invalidPassword")) {
                announceText.setText("invalid password");
                announceText.setVisible(true);
            } else {
                announceText.setText("invalid username");
                announceText.setVisible(true);
            }
        } else {
            objects = SignupLogin.getInstance().checkParameter(usernameTextField.getText(), passwordField.getText());
            if (Objects.equals(objects.getFirst(), "checkParameter usernameIsNotFound")) {
                announceText.setText("username is not found");
                announceText.setVisible(true);
            } else if (Objects.equals(objects.getFirst(), "checkParameter theEmailDoesNotMatch")) {
                announceText.setText("email doesn't match");
                announceText.setVisible(true);
            } else {
                VerificationCode.isLogin = true;
                VerificationCode.username = usernameTextField.getText();
                VerificationCode.email = passwordField.getText();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("verification-code.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), bounds.getWidth(), bounds.getHeight());
                HelloApplication.stage.setScene(scene);
            }
        }
    }

    @FXML
    void forgetPasswordTextEntered(MouseEvent event) {
        forgetPasswordText.setUnderline(true);
    }

    @FXML
    void forgetPasswordTextExited(MouseEvent event) {
        forgetPasswordText.setUnderline(false);
    }

    @FXML
    void nameTextFieldEntered(MouseEvent event) {
        effect = nameTextField.getEffect();
        nameTextField.setEffect(new Bloom(.99));
    }

    @FXML
    void nameTextFieldExited(MouseEvent event) {
        nameTextField.setEffect(effect);
    }

    @FXML
    void passwordFieldEntered(MouseEvent event) {
        effect = passwordField.getEffect();
        passwordField.setEffect(new Bloom(.99));
    }

    @FXML
    void passwordFieldExited(MouseEvent event) {
        passwordField.setEffect(effect);
    }

    @FXML
    void signUpLoginButtonEntered(MouseEvent event) {
        effect = signUpLoginButton.getEffect();
        signUpLoginButton.setEffect(new DropShadow());
    }

    @FXML
    void signUpLoginButtonExited(MouseEvent event) {
        signUpLoginButton.setEffect(effect);
    }

    @FXML
    void signUpLoginTextEntered(MouseEvent event) {
        effect = signUpLoginText.getEffect();
        signUpLoginText.setEffect(new Bloom());
    }

    @FXML
    void signUpLoginTextExited(MouseEvent event) {
        signUpLoginText.setEffect(effect);
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

    @FXML
    void usernameTextFieldEntered(MouseEvent event) {
        effect = usernameTextField.getEffect();
        usernameTextField.setEffect(new Bloom(.99));
    }

    @FXML
    void usernameTextFieldExited(MouseEvent event) {
        usernameTextField.setEffect(effect);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            String pass = checkPasswordStrength(newValue);
            if (pass.equals("Strong")) {
                firstBall.setFill(Color.GREEN);
                secondBall.setFill(Color.GREEN);
                thirdBall.setFill(Color.GREEN);
            } else if (pass.equals("Weak")) {
                firstBall.setFill(Color.RED);
                secondBall.setFill(Color.WHITE);
                thirdBall.setFill(Color.WHITE);
            } else {
                firstBall.setFill(Color.ORANGE);
                secondBall.setFill(Color.ORANGE);
                thirdBall.setFill(Color.WHITE);
            }
        });
        announceText.setVisible(false);
        forgetPasswordText.setVisible(false);
        forgetPasswordText.setDisable(true);
    }

    public static String checkPasswordStrength(String password) {
        if (password.length() < 6) {
            return "Weak";
        }
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        int strengthPoints = (hasLower ? 1 : 0) + (hasUpper ? 1 : 0) + (hasDigit ? 1 : 0) + (hasSpecial ? 1 : 0);
        if (strengthPoints >= 3 && password.length() >= 8) {
            return "Strong";
        } else if (strengthPoints >= 2) {
            return "Medium";
        } else {
            return "Weak";
        }
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

}
