package view;

import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ThrowTextAnimation extends Transition {
    private Pane pane;
    private Text text;
    private Circle centerCircle;
    private Circle intersectsCircle = new Circle(300, 180, 164.1);



    public ThrowTextAnimation(Pane pane, Text text, Circle centerCircle) {
        this.pane = pane;
        this.text = text;
        this.centerCircle = centerCircle;
        this.setCycleDuration(Duration.seconds(1));
        this.setCycleCount(-1);
    }
    @Override
    protected void interpolate(double v) {
        double y = text.getY() - 2;
        if (text.getY() < 0) {
            pane.getChildren().remove(text);
            this.stop();
        }
        if (text.getBoundsInLocal().intersects(intersectsCircle.getLayoutBounds()))

            this.stop();
        text.setY(y);
    }
}

