package view;

import controller.LoginAndRegisterController;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RegisterMenuController {
    public static Stage stage;
    public TextField username;
    public PasswordField password;
    public PasswordField checkPassword;
    public void createAccount(MouseEvent mouseEvent) throws Exception {
        String usernameText = username.getText();
        String passwordText = password.getText();
        String checkPasswordText = checkPassword.getText();
        String result = LoginAndRegisterController.createAccount(usernameText, passwordText, checkPasswordText);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erorre");
        if (result.equals("exist")){
            alert.setHeaderText("Username Erorre!");
            alert.setContentText("This username has already been used");
            alert.show();
        } else if (result.equals("wrong")){
            alert.setHeaderText("Password Erorre!");
            alert.setContentText("Password is not the same as password check");
            alert.show();
        } else {
            new LoginMenu().start(stage);
        }
    }
}
