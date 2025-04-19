package view;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.IOException;

public class ThrowBallAnimation extends Transition {
    private Pane gamePane;
    private ProgressBar progressBar;
    private int direction = 1;
    private static int throwDegreeForNow = 0;
    private Circle ball;
    private Circle intersectsCircle = new Circle(300, 350, 162);

    public static void setThrowDegreeForNow(int throwDegreeForNow) {
        ThrowBallAnimation.throwDegreeForNow = throwDegreeForNow;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public ThrowBallAnimation(Pane gamePane, Circle ball ,  ProgressBar progressBar) {
        this.gamePane = gamePane;
        this.ball = ball;
        this.progressBar = progressBar;
        this.setCycleDuration(Duration.INDEFINITE);
        this.setCycleCount(-1);
        Media media = new Media(ThrowBallAnimation.class.getResource("/media/throw.mp3").toExternalForm());
        new MediaPlayer(media).play();
    }

    @Override
    protected void interpolate(double v) {
        if (ball.getBoundsInLocal().intersects(intersectsCircle.getBoundsInLocal())) {
            this.stop();
            progressBar.setProgress(progressBar.getProgress() + 0.2);
            rotate();
            try {
                collision();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (ball.getCenterX() < 0 || ball.getCenterX() > 600 || ball.getCenterY() > 700 || ball.getCenterY() < 0) {
            this.stop();
            try {
                GameMenu.end(gamePane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        ball.setCenterY(ball.getCenterY() - direction * 7);
        ball.setCenterX(ball.getCenterX() + throwDegreeForNow / 7);
    }

    private void collision() throws IOException {
        for (Node node : gamePane.getChildren()) {
                if (node instanceof Circle) {
                    if (!ball.equals((Circle) node)) {
                        if (ball.getBoundsInParent().intersects(node.getBoundsInParent())) {
                            this.stop();
                            GameMenu.end(gamePane);
                            break;
                        }
                    }
                }
            }
    }

    private void rotate() {
        Line line = new Line();
        gamePane.getChildren().add(line);
        RotateAnyThingAnimation rotateBall = new RotateAnyThingAnimation(gamePane, ball);
        RotateAnyThingAnimation rotateLine = new RotateAnyThingAnimation(gamePane, line);
        GameMenu.getAllRotateAnyThingAnimations().add(rotateBall);
        GameMenu.getAllRotateAnyThingAnimations().add(rotateLine);
        double x = ball.getCenterX() - 300.0;
        double y = ball.getCenterY() - 350.0;
        double angle = Math.toDegrees(Math.acos(x / 162));
        if (y > 0)
            angle = 90 - angle + 270.;
        rotateBall.setAngle(angle);rotateLine.setAngle(angle);
        rotateBall.play();
        rotateLine.play();
    }


}
