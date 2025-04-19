package view;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class IceModeAnimation extends Transition {
    private Circle iceCircle;

    public IceModeAnimation(Circle iceCircle) {
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.seconds(1));
        this.iceCircle = iceCircle;

    }

    @Override
    protected void interpolate(double v) {
        if (v < 0.33) {
            iceCircle.setScaleY(1.1);
            iceCircle.setScaleX(1.1);
        }
        else if (v < 0.66) {
            iceCircle.setScaleY(1.2);
            iceCircle.setScaleX(1.2);
        }
        else {
            iceCircle.setScaleY(1.3);
            iceCircle.setScaleX(1.3);
        }

    }
}
