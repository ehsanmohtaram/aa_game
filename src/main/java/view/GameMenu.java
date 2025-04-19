package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameMenu extends Application {
    private static User currentUser;
    private static int map = 72;
    private static MediaPlayer mediaPlayer;
    private static boolean mute = false;
    private boolean twoPlayerMode = true;
    private int finaleNumber = 11;
    private static int temporaryNumber = 11;
    private boolean visible = true;
    private int sign = 1;
    private int radius = 10;
    private static int throwDegree = 15;
    private int throwDegreeForNow;
    private static int timeForIceMode = 5;
    private static int time;
    private static int score = 0;
    private Pane gamePane;
    private static Stage stage;
    private static ArrayList<RotateAnyThingAnimation> allRotateAnyThingAnimations = new ArrayList<>();
    private static ArrayList<Timeline> allTimeline = new ArrayList<>();

    public static ArrayList<RotateAnyThingAnimation> getAllRotateAnyThingAnimations() {
        return allRotateAnyThingAnimations;
    }

    public static void setThrowDegree(int throwDegree) {
        GameMenu.throwDegree = throwDegree;
    }

    public static void setTimeForIceMode(int timeForIceMode) {
        GameMenu.timeForIceMode = timeForIceMode;
    }

    public static void setCurrentUser(User currentUser) {
        GameMenu.currentUser = currentUser;
    }

    public static void setMute(boolean mute) {
        GameMenu.mute = mute;
    }

    public static boolean isMute() {
        return mute;
    }

    public void setFinaleNumber(int finaleNumber) {
        this.finaleNumber = finaleNumber;
    }

    public static void setTemporaryNumber(int temporaryNumber) {
        GameMenu.temporaryNumber = temporaryNumber;
    }

    public static void setMap(int map) {
        GameMenu.map = map;
    }

    @Override
    public void start(Stage stage) throws Exception {
        media();
        this.stage = stage;
        time = 0;score = 0;
        temporaryNumber = finaleNumber;
        throwDegreeForNow = 0;
        ThrowBallAnimation.setThrowDegreeForNow(0);
        Pane gamePane = FXMLLoader.load(GameMenu.class.getResource("/fxml/game.fxml"));
        this.gamePane = gamePane;
        Circle centerCircle = createCenterCircle();
        beforeStart();
        createSettingImage(gamePane, createSettingPane(gamePane));

        createBall(centerCircle, createProgressBar());
        Scene scene = new Scene(gamePane);
        stage.setScene(scene);
        gamePane.getChildren().get(0).requestFocus();
        stage.show();
    }




    private Circle createBall(Circle centerCircle, ProgressBar progressBar) {
        Circle ball = returnBall();
        runTime();
        Circle ball2 = returnBall();
        ball2.setCenterY(30);
        ball2.setFill(Color.RED);
        if (!twoPlayerMode)
            ball2.setVisible(false);
        Text scorePrinted = createText("Your score: " + score, 0, 50, 20, Color.BLACK, gamePane);
        Text throwDegreePrinted = createText("Throw degree: 0", 0, 80, 20,Color.BLACK, gamePane);
        Text remainBall = createText(Integer.toString(temporaryNumber),275, 370, 50, Color.WHITE, gamePane);
        gamePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyName = keyEvent.getCode().getName();
                if (keyName.equals("Space")) {
                    scoreText(scorePrinted);
                    Circle throwBall = returnBall();
                    throwBall.setCenterX(ball.getCenterX());throwBall.setCenterX(ball.getCenterX());
                    new ThrowBallAnimation(gamePane, throwBall, progressBar).play();
                    temporaryNumber--;
                    remainBall.setText(Integer.toString(temporaryNumber));
                    doAllFaze(remainBall, throwDegreePrinted);
                } else if (keyName.equals("Enter") && twoPlayerMode){
                    System.out.println("ld");
                    scoreText(scorePrinted);
                    Circle throwBall = returnBall();
                    throwBall.setCenterY(ball2.getCenterY());throwBall.setCenterX(ball2.getCenterX());
                    throwBall.setFill(Color.RED);
                    ThrowBallAnimation throwBallAnimation = new ThrowBallAnimation(gamePane, throwBall, progressBar);
                    throwBallAnimation.setDirection(-1);
                    throwBallAnimation.play();
                    temporaryNumber--;
                    remainBall.setText(Integer.toString(temporaryNumber));
                    doAllFaze(remainBall, throwDegreePrinted);
                }
                if (temporaryNumber == 0) {
                    gamePane.getChildren().remove(ball);
                    try {
                        end(gamePane);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                else if (keyName.equals("Tab") && (progressBar.getProgress() >= 1)){
                    progressBar.setProgress(0);doIceMode();
                } else if (keyName.equals("Left") && allTimeline.size() == 4){
                    if (ball.getCenterX() > 10) {
                        ball.setCenterX(ball.getCenterX() - 5);
                        ball2.setCenterX(ball2.getCenterX() - 5);
                    }
                } else if (keyName.equals("Right") && allTimeline.size() == 4){
                    if (ball.getCenterX() < 590) {
                        ball.setCenterX(ball.getCenterX() + 5);
                        ball2.setCenterX(ball2.getCenterX() + 5);
                    }
                }
            }
        });
        return ball;
    }

    private void createSettingImage(Pane gamePane, Pane settingPane) throws IOException {
        ImageView setting = new ImageView(new Image(GameMenu.class.getResource("/images/setting.jpg").toExternalForm()));
        setting.setX(500);setting.setY(0);
        setting.setFitWidth(100);setting.setFitHeight(100);
        gamePane.getChildren().add(setting);
        setting.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                settingPane.setVisible(true);
            }
        });
    }

    private void media() {
        Media media = new Media(GameMenu.class.getResource("/media/got1.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        this.mediaPlayer = mediaPlayer;
        mediaPlayer.play();
        mediaPlayer.setMute(mute);
    }

    public Pane createSettingPane(Pane gamePane) throws IOException {
        Pane settingPane = FXMLLoader.load(GameMenu.class.getResource("/fxml/game.fxml"));
        gamePane.getChildren().add(settingPane);
        Button restart = createButton("Restart", 480, 220,2,2,settingPane);
        Button exit = createButton("Exit", 488, 290, 2 , 2, settingPane);
        Button mute = createButton("Mute", 485, 360, 2 , 2, settingPane);
        MenuButton media = new MenuButton("Media");
        settingPane.getChildren().add(media);
        restartClicked(restart);
        exitClicked(exit);
        muteClicked(mute, settingPane);
        mediaClicked(media, settingPane);
        settingPane.setVisible(false);
        return settingPane;
    }

    private void mediaClicked(MenuButton media, Pane settingPane) {
        media.setLayoutX(482);media.setLayoutY(430);media.setScaleX(2);media.setScaleY(2);
        MenuItem media1 = new MenuItem("1");
        MenuItem media2 = new MenuItem("2");
        MenuItem media3 = new MenuItem("3");
        media.getItems().add(media1);media.getItems().add(media2);media.getItems().add(media3);
        media1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Media mediaForPlay = new Media(GameMenu.class.getResource("/media/got1.mp3").toExternalForm());
                mediaPlayer.stop();
                mediaPlayer = new MediaPlayer(mediaForPlay);
                mediaPlayer.play();
                settingPane.setVisible(false);
                gamePane.getChildren().get(0).requestFocus();
            }
        });
        media2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Media mediaForPlay = new Media(GameMenu.class.getResource("/media/got2.mp3").toExternalForm());
                mediaPlayer.stop();
                mediaPlayer = new MediaPlayer(mediaForPlay);
                mediaPlayer.play();
                settingPane.setVisible(false);
                gamePane.getChildren().get(0).requestFocus();
            }
        });
        media3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Media mediaForPlay = new Media(GameMenu.class.getResource("/media/got2.mp3").toExternalForm());
                mediaPlayer.stop();
                mediaPlayer = new MediaPlayer(mediaForPlay);
                mediaPlayer.play();
                settingPane.setVisible(false);
                gamePane.getChildren().get(0).requestFocus();
            }
        });
    }

    private void muteClicked(Button mute, Pane settingPane) {
        mute.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mediaPlayer.isMute()){
                    mediaPlayer.setMute(false);
                } else
                    mediaPlayer.setMute(true);
                settingPane.setVisible(false);
                gamePane.getChildren().get(0).requestFocus();
            }
        });
    }

    private void exitClicked(Button exit) {
        exit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    for (Timeline timeline : allTimeline) timeline.stop();
                    allTimeline.clear();
                    mediaPlayer.stop();
                    new MainMenu().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void restartClicked(Button restart) {
        restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    for (Timeline timeline : allTimeline) timeline.stop();
                    allTimeline.clear();
                    mediaPlayer.stop();
                    new GameMenu().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public Circle returnBall() {
        Circle ball = new Circle(300, 670 , radius);
        gamePane.getChildren().add(ball);
        return ball;
    }

    private void runTime() {
        Timeline passedTime = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> time++));
        allTimeline.add(passedTime);
        passedTime.setCycleCount(-1);
        passedTime.play();
    }

    private ProgressBar createProgressBar() {
        HBox hBox = new HBox();
        hBox.setSpacing(20);
        createText("Ice Mode:",0, 0, 20,Color.BLACK, hBox);
        ProgressBar progressBar = new ProgressBar(0.2);
        progressBar.setMinSize(120, 25);
        hBox.getChildren().add(progressBar);
        gamePane.getChildren().add(hBox);
        return progressBar;
    }

    private Circle createCenterCircle() {
        Circle centerCircle = new Circle(300, 350, 55);
        centerCircle.setFill(Color.BLACK);
        gamePane.getChildren().add(centerCircle);
        return centerCircle;
    }

    public void beforeStart() {
        double angle = 270;
        for (int i = 0; i < 5; i++){
            Circle defaultBall = new Circle( 10);
            Line line = new Line();
            gamePane.getChildren().add(line);
            gamePane.getChildren().add(defaultBall);
            RotateAnyThingAnimation ballRotate = new RotateAnyThingAnimation(gamePane, defaultBall);
            RotateAnyThingAnimation lineRotate = new RotateAnyThingAnimation(gamePane, line);
            ballRotate.setAngle(angle);
            lineRotate.setAngle(angle);
            allRotateAnyThingAnimations.add(ballRotate);
            allRotateAnyThingAnimations.add(lineRotate);
            ballRotate.play();
            lineRotate.play();
            angle -= map;
        }
    }

    public void doIceMode(){
        int pastDuration = RotateAnyThingAnimation.getDuration();
        Circle iceCircle = new Circle(300, 350,55);
        iceCircle.setFill(new ImagePattern(new Image(GameMenu.class.getResource("/images/ice.jpg").toExternalForm())));
        gamePane.getChildren().add(iceCircle);
        new IceModeAnimation(iceCircle).play();
        RotateAnyThingAnimation.setDuration(2);
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                RotateAnyThingAnimation.setDuration(pastDuration);
                iceCircle.setVisible(false);
                timer.cancel();
            }
        }, timeForIceMode * 1000);
    }

    private void doAllFaze(Text remainBall, Text throwDegreePrinted) {
            if (temporaryNumber - 1 == 3 * finaleNumber / 4) {
                remainBall.setFill(Color.BLUE);
                Timeline reversTimeline = new Timeline(new KeyFrame(Duration.seconds(5), actionEvent -> faz2()));
                allTimeline.add(reversTimeline);
                reversTimeline.setCycleCount(-1);
                reversTimeline.play();
            } else if (temporaryNumber - 1 == finaleNumber / 2) {
                remainBall.setFill(Color.RED);
                Timeline visibleTimeline = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent -> faz3()));
                allTimeline.add(visibleTimeline);
                visibleTimeline.setCycleCount(-1);
                visibleTimeline.play();
            }else if (temporaryNumber - 1 ==  finaleNumber / 4) {
                remainBall.setFill(Color.GREEN);
                throwDegreeForNow = throwDegree;
                faz4(throwDegreePrinted);
            }
    }

    public void faz2(){
        sign = -1 * sign;
        radius = radius - sign * 3;
        RotateAnyThingAnimation.setSign(sign);
        for (Node node : gamePane.getChildren()) {
            if (node instanceof Circle) {
                if ((((Circle) node).getRadius() != 55)) {
                    ((Circle) node).setRadius(((Circle) node).getRadius() - sign * 3);
                }
            }
        }

    }

    public void faz3(){
        visible = !visible;
        for (Node node : gamePane.getChildren()) {
            if (node instanceof Circle) {
                if ((((Circle) node).getCenterY() != 670) && (((Circle) node).getRadius() != 55) && (((Circle) node).getCenterY() != 30)) {
                    node.setVisible(!visible);
                }
            } else if (node instanceof Line)
                node.setVisible(!visible);
        }
    }


    public void faz4(Text throwDegreePrinted){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), actionEvent -> throwText(throwDegreePrinted)));
        allTimeline.add(timeline);
        timeline.setCycleCount(-1);
        timeline.play();
    }

    public void throwText(Text throwDegreePrinted){
        throwDegreeForNow = (throwDegreeForNow + 36) % throwDegree * 2 - throwDegree;
        ThrowBallAnimation.setThrowDegreeForNow(throwDegreeForNow);
        throwDegreePrinted.setText("Throw degree: " + throwDegreeForNow);
    }

    public void scoreText(Text scorePrinted){
        score += score + 10;
        scorePrinted.setText("Your score: " + score);
    }

    public static Text createText(String textContain, int x, int y, int font, Color color, Pane pane){
        Text text = new Text(x, y, textContain);
        text.setFont(new Font(font));
        text.setFill(color);
        pane.getChildren().add(text);
        return text;
    }


    public static void end(Pane gamePane) throws IOException {
        if ((currentUser != null) && ((currentUser.getBestScore() < score) || (currentUser.getBestScore() == score &&
                currentUser.getTimeInBestScore() > time))){
            currentUser.setBestScore(score);
            currentUser.setTimeInBestScore(time);
        }
        for (Timeline timeline : allTimeline) timeline.stop();
        allTimeline.clear();
        mediaPlayer.stop();
        Text loserOrWiner = createText("You are a loser 😂😂",180, 50,30, Color.WHITE,gamePane);
        if (temporaryNumber == 0) {
            gamePane.setBackground(new Background(new BackgroundFill(Color.GREEN,null,null)));
            loserOrWiner.setText("you win");loserOrWiner.setX(250);
        } else
            gamePane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        createText("Passed Time: " + time + "s", 120, 100,20,Color.WHITE, gamePane);
        createText("Your score: " + score, 320, 100, 20, Color.WHITE, gamePane);
        User.scoreBord();
        int y = 250;
        for (User user : User.getUsers()) {
            Text text  = createText(user.getUsername() + "      Best score: " + user.getBestScore() + "     Time:  " + user.getTimeInBestScore(),
                    120, y, 20, Color.WHITE, gamePane);
            if (y == 250)
                text.setFill(Color.CORNFLOWERBLUE);
            if (y == 290)
                text.setFill(Color.BLUE);
            if (y == 330)
                text.setFill(Color.GOLD);
            y += 40;
        }

        Button returnToMainMenu = new Button("Main menu");
        returnToMainMenu.setLayoutX(250);returnToMainMenu.setLayoutY(160);
        returnToMainMenu.setScaleX(2);returnToMainMenu.setScaleY(2);
        gamePane.getChildren().add( returnToMainMenu);
        gamePane.getChildren().get(gamePane.getChildren().size() - 1).requestFocus();
        returnToMainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
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

    public static Button createButton(String textContain, int x, int y, int scaleX, int scaleY, Pane pane) {
        Button button = new Button(textContain);
        button.setLayoutX(x);button.setLayoutY(y);
        button.setScaleX(scaleX);button.setScaleY(scaleY);
        pane.getChildren().add(button);
        return button;
    }



    public void ffff(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName().equals("Space"))
            System.out.println("ffffffffffffffffffffffffff");
    }
}