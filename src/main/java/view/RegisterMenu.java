package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class RegisterMenu extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = FXMLLoader.load(RegisterMenu.class.getResource("/fxml/registerMenu.fxml"));
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }


}
