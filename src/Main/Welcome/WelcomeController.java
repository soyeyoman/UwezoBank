package Main.Welcome;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WelcomeController extends StackPane{

@FXML ImageView logo;
@FXML StackPane pane;

Logger logger = Logger.getLogger(getClass().getName());
Stage window = new Stage();


public WelcomeController(){
    FXMLLoader loader = new FXMLLoader(getClass().getResource("welcome.fxml"));
    loader.setController(this);
    try{
       this.getChildren().clear();
       this.getChildren().add(loader.load());
    } catch (IOException e) {
        logger.log(Level.SEVERE,e.getMessage());
    }
    window.initStyle(StageStyle.UNDECORATED);
    window.setScene(new Scene(this,600,400));
}

public void show() {
    window.show();

    //rotates the logo 180 degrees
    RotateTransition rot = new RotateTransition(Duration.millis(1000));
    rot.setNode(logo);
    rot.setFromAngle(180);
    rot.setToAngle(0);

    //scales logo from x3 to x1
    ScaleTransition scale = new ScaleTransition(Duration.millis(1000));
    scale.setNode(logo);
    scale.setFromX(3);
    scale.setFromY(3);
    scale.setToX(1);
    scale.setToY(1);

    //

    rot.playFromStart();
    scale.playFromStart();



}

public void hide(Stage  primaryStage){
    FadeTransition fd = new FadeTransition(Duration.millis(1000));
    fd.setNode(this);
    fd.setFromValue(1);
    fd.setToValue(0.4);
    fd.play();
    fd.setOnFinished(e->{
        primaryStage.show();
        FadeTransition fi = new FadeTransition(Duration.millis(1000));
        fi.setNode(primaryStage.getScene().getRoot());
        fi.setFromValue(0.1);
        fi.setToValue(1);
        fi.play();
        window.hide();
    });


}

}