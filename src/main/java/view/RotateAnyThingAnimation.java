package view;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class RotateAnyThingAnimation extends Transition {
    private Pane pane;
    private Node node;
    private static int sign = 1;
    private static Integer duration = 10;
    private Double angle = 270.0;

    public static void setDuration(Integer durationSet) {
        duration = durationSet;
    }


    public static void setSign(int signSet) {
        sign = signSet;
    }

    public static Integer getDuration() {
        return duration;
    }

    public RotateAnyThingAnimation(Pane pane, Node node) {
        this.pane = pane;
        this.node = node;
        this.setCycleDuration(Duration.seconds(0.0025));
        this.setCycleCount(-1);
    }

    @Override
    protected void interpolate(double v) {
        if (node instanceof Circle) {
            ((Circle) node).setCenterX(300 + 162 * Math.cos(Math.toRadians(angle)));
            ((Circle) node).setCenterY(350 - 162 * Math.sin(Math.toRadians(angle)));
        }
        if (node instanceof Line) {
            ((Line) node).setStartX(300 + 55 * Math.cos(Math.toRadians(angle)));
            ((Line) node).setEndX(300 + 162 * Math.cos(Math.toRadians(angle)));
            ((Line) node).setStartY(350  - 55 * Math.sin(Math.toRadians(angle)));
            ((Line) node).setEndY(350 - 162 * Math.sin(Math.toRadians(angle)));
        }
        angle= angle + sign * duration * (0.01);
    }

    public void setAngle(Double angleForSet) {
        angle = angleForSet;
    }
}
