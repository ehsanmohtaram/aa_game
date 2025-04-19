package view;

import controller.LoginAndRegisterController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class ProfileMenu extends Application {
    public static Stage stage;
    private static User currentUser;
    public TextField username;
    public PasswordField password;
    public LoginMenu loginMenu = new LoginMenu();

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = FXMLLoader.load(MainMenuController.class.getResource("/fxml/profileMenu.fxml"));
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public static void setCurrentUser(User currentUser) {
        ProfileMenu.currentUser = currentUser;
    }

    public void ChangeUsernameOrPassword(MouseEvent mouseEvent) throws IOException {
        BorderPane borderPane = FXMLLoader.load(MainMenuController.class.getResource("/fxml/changeUsernameOrPassword.fxml"));
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public void changeFunction(MouseEvent mouseEvent) throws Exception {
        String usernameText = username.getText();
        String passwordText = password.getText();
        String result = LoginAndRegisterController.changeUsername(usernameText, passwordText, currentUser);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erorre");
        if (result.equals("exist")){
            alert.setHeaderText("Username Erorre!");
            alert.setContentText("This username has already been used");
            alert.show();
        } else if(result.equals("change")) {
            start(stage);
        }
    }


    public void logout(MouseEvent mouseEvent) throws Exception {
        loginMenu.start(stage);
    }

    public void deleteAccount(MouseEvent mouseEvent) throws Exception {
        User.getUsers().remove(currentUser);
        loginMenu.start(stage);
    }

    public void selectAvatar(MouseEvent mouseEvent) throws IOException {
//        BorderPane borderPane = FXMLLoader.load(MainMenuController.class.getResource("/fxml/avatar.fxml"));
        Pane avatarPane = FXMLLoader.load(GameMenu.class.getResource("/fxml/game.fxml"));
        ImageView avatar = currentUser.getAvatar();
        ImageView a = new ImageView(new Image(ProfileMenu.class.getResource("/images/a.jpg").toExternalForm()));
        ImageView b = new ImageView(new Image(ProfileMenu.class.getResource("/images/b.jpg").toExternalForm()));
        ImageView c = new ImageView(new Image(ProfileMenu.class.getResource("/images/c.jpg").toExternalForm()));
        ImageView d = new ImageView(new Image(ProfileMenu.class.getResource("/images/d.jpg").toExternalForm()));
        ImageView returnTo = new ImageView(new Image(ProfileMenu.class.getResource("/images/return.png").toExternalForm()));
        Text ownImage = new Text(210, 550, "set Avatar by onw image");

        setImageSize(avatar,210, 100, 200, 200);
        setImageSize(a, 470, 400,100, 100);
        setImageSize(b, 320, 400,100, 100);
        setImageSize(c, 170, 400,100, 100);
        setImageSize(d, 20, 400,100, 100);
        setImageSize(returnTo, 0,0,100, 100);
        ownImage.setFont(new Font(20));ownImage.setFill(Color.GREEN);

        avatarPane.getChildren().add(avatar);
        avatarPane.getChildren().add(a);
        avatarPane.getChildren().add(b);
        avatarPane.getChildren().add(c);
        avatarPane.getChildren().add(d);
        avatarPane.getChildren().add(returnTo);
        avatarPane.getChildren().add(ownImage);

        setMouseClickAvatar(a, avatar);
        setMouseClickAvatar(b, avatar);
        setMouseClickAvatar(c, avatar);
        setMouseClickAvatar(d, avatar);
        setOnMouseClickedReturn(returnTo);
        setOnMouseClickedOwnImage(ownImage, avatarPane, avatar);


        Scene scene = new Scene(avatarPane);
        stage.setScene(scene);
        stage.show();
    }

    public void setImageSize(ImageView imageView, int x, int y, int height, int width) {
        imageView.setFitHeight(width);
        imageView.setFitWidth(height);
        imageView.setX(x);
        imageView.setY(y);
    }

    private void setOnMouseClickedOwnImage(Text ownImage, Pane avatarPane, ImageView avatar) {
        ownImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TextField url = new TextField("Your image path");
                url.setLayoutX(230);url.setLayoutY(570);
                avatarPane.getChildren().add(url);
                Button submit = new Button("submit");
                submit.setLayoutX(275);submit.setLayoutY(600);
                avatarPane.getChildren().add(submit);
                submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        ImageView ownImage = new ImageView(new Image(url.getText()));
                        setAvatar(ownImage, avatar);
                    }
                });
            }
        });
    }

    private void setOnMouseClickedReturn(ImageView imageView) {
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new ProfileMenu().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public void setMouseClickAvatar(ImageView imageView, ImageView avatar) {
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setAvatar(imageView, avatar);
            }
        });
    }

    public void setAvatar(ImageView imageView, ImageView avatar){
        avatar.setImage(imageView.getImage());
        currentUser.setAvatar(imageView);
    }

}

