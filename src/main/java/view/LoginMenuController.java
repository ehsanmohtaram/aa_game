package view;

import controller.LoginAndRegisterController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;

public class LoginMenuController {
    public static Stage stage;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    public void login(MouseEvent mouseEvent) throws Exception {
        String usernameText = username.getText();
        String passwordText = password.getText();
        String result = LoginAndRegisterController.login(usernameText, passwordText);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erorre");
        if (result.equals("not found")){
            alert.setHeaderText("Login erorre!");
            alert.setContentText("Your username is not founded!");
            alert.show();
        } else if (result.equals("wrong")){
            alert.setHeaderText("Password erorre!");
            alert.setContentText("Password is wrong!");
            alert.show();
        } else if (result.equals("login")) {
            MainMenuController.stage = stage;
            new MainMenu().start(stage);
        }
    }

    public void createAccount(MouseEvent mouseEvent) throws Exception {
        RegisterMenuController.stage = stage;
        new RegisterMenu().start(stage);
    }

    public void playAsGuest(MouseEvent mouseEvent) throws Exception {
        MainMenuController.stage = stage;
        new GameMenu().start(stage);
    }
}
