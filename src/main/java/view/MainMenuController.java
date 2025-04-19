package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class MainMenuController {
    public static Stage stage;
//    private static User currentUser;

//    public static void setCurrentUser(User currentUser) {
//        MainMenuController.currentUser = currentUser;
//    }
//
//    public static User getCurrentUser() {
//        return currentUser;
//    }

    public void profileMenu(MouseEvent mouseEvent) throws Exception {
        ProfileMenu.stage = stage;
        new ProfileMenu().start(stage);
    }

    public void exit(MouseEvent mouseEvent) throws Exception {
        new LoginMenu().start(stage);
    }

    public void newGame(MouseEvent mouseEvent) throws Exception{
        new GameMenu().start(stage);
    }

    public void scoreBord(MouseEvent mouseEvent) throws IOException {
        User.scoreBord();
        int y = 100;
        Pane scoreBordPane = FXMLLoader.load(MainMenuController.class.getResource("/fxml/game.fxml"));
        for (User user : User.getUsers()) {
            createText(100, y, scoreBordPane, user);
            y += 50;
        }
        ImageView returnTo = new ImageView(new Image(ProfileMenu.class.getResource("/images/return.png").toExternalForm()));
        returnTo.setY(0);returnTo.setX(0);
        returnTo.setFitHeight(100);returnTo.setFitWidth(100);
        scoreBordPane.getChildren().add(returnTo);
        returnTo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Scene scene = new Scene(scoreBordPane);
        stage.setScene(scene);
    }

    public void setting(MouseEvent mouseEvent) throws IOException {
        Pane settingPane = FXMLLoader.load(MainMenuController.class.getResource("/fxml/game.fxml"));
        createDifficultyLevelButton(settingPane);
        setNumberBall(settingPane);
        setOneMap(settingPane);
        muteButton(settingPane);

        returnTo(settingPane);
        Scene scene = new Scene(settingPane);
        stage.setScene(scene);
    }

    private void muteButton(Pane settingPane) {
        Button button = new Button("Mute");
        button.setLayoutX(260);button.setLayoutY(200);button.setScaleX(1.3);button.setScaleY(1.3);
        settingPane.getChildren().add(button);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (GameMenu.isMute())
                    GameMenu.setMute(false);
                else
                    GameMenu.setMute(true);
            }
        });
    }


    private void returnTo(Pane settingPane) {
        ImageView returnTo = new ImageView(new Image(ProfileMenu.class.getResource("/images/return.png").toExternalForm()));
        returnTo.setY(0);returnTo.setX(0);
        returnTo.setFitHeight(100);returnTo.setFitWidth(100);
        settingPane.getChildren().add(returnTo);
        returnTo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void createDifficultyLevelButton(Pane settingPane) {
        MenuButton difficultyLevel = new MenuButton("Difficulty level");
        settingPane.getChildren().add(difficultyLevel);
        difficultyLevel.setLayoutX(260);difficultyLevel.setLayoutY(100);difficultyLevel.setScaleY(1.3);difficultyLevel.setScaleX(1.3);
        createMenuItemForDifficulty(difficultyLevel);
    }

    public void createMenuItemForDifficulty(MenuButton menuButton) {
        MenuItem easy = new MenuItem("Easy");
        MenuItem middle = new MenuItem("Middle");
        MenuItem hard = new MenuItem("Hard");
        easy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                RotateAnyThingAnimation.setDuration(5);
                GameMenu.setThrowDegree(12);
                GameMenu.setTimeForIceMode(7);
            }
        });
        middle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                RotateAnyThingAnimation.setDuration(10);
                GameMenu.setThrowDegree(15);
                GameMenu.setTimeForIceMode(5);
            }
        });
        hard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                RotateAnyThingAnimation.setDuration(15);
                GameMenu.setThrowDegree(17);
                GameMenu.setTimeForIceMode(3);
            }
        });
        menuButton.getItems().add(easy);
        menuButton.getItems().add(middle);
        menuButton.getItems().add(hard);
    }

    private void setNumberBall(Pane settingPane) {
    }

    private void createMenuItemForMap(MenuButton map) {
        MenuItem easy = new MenuItem("1");
        MenuItem middle = new MenuItem("2");
        MenuItem hard = new MenuItem("3");
        easy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameMenu.setMap(80);
            }
        });
        middle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameMenu.setMap(55);
            }
        });
        hard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameMenu.setMap(72);
            }
        });
        map.getItems().add(easy);
        map.getItems().add(middle);
        map.getItems().add(hard);
    }

    private void setOneMap(Pane settingPane) {
        MenuButton map = new MenuButton("Map");
        settingPane.getChildren().add(map);
        map.setLayoutX(260);map.setLayoutY(150);map.setScaleY(1.3);map.setScaleX(1.3);
        createMenuItemForMap(map);
    }

    public Text createText(int x, int y, Pane pane, User user) {
        Text text = new Text(x, y, user.getUsername() + "       Best score:   " + user.getBestScore() +
                "           Time:     " + user.getTimeInBestScore());
        text.setFont(new Font(20));
        if (y == 100)
            text.setFill(Color.GREEN);
        else if (y == 150)
            text.setFill(Color.BLUE);
        else if (y == 200)
            text.setFill(Color.RED);
        pane.getChildren().add(text);
        return text;
    }


}
