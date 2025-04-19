package controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.User;
import view.GameMenu;
import view.LoginMenuController;
import view.ProfileMenu;

public class LoginAndRegisterController {
    public static String login(String username, String password){
        User user;
        if ((user = User.getUserByUsername(username)) != null){
            if (user.isPasswordCorrect(password)){
                ProfileMenu.setCurrentUser(user);
//                MainMenuController.setCurrentUser(user);
                GameMenu.setCurrentUser(user);
                return "login";
            } else return "wrong";
        } else return "not found";
    }

    public static String createAccount(String username, String password, String checkPassword){
        if (User.getUserByUsername(username) == null) {
            if (password.equals(checkPassword)) {
                User.createUser(username, password, new ImageView(new Image(LoginMenuController.class.getResource("/images/a.jpg").toExternalForm())));
                return "create";
            } else return "wrong";
        } else return "exist";
    }

    public static String changeUsername(String username, String password, User user){
        if (User.getUserByUsername(username) == null) {
            user.setUsername(username);
            user.setPassword(password);
            return "change";
        } else return "exist";
    }
}
