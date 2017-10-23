package Main;

import Main.Welcome.WelcomeController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Main extends Application {

    WelcomeController wel = new WelcomeController();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Login/loginView.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 800, 500));

        wel.show();
        PauseTransition ps = new PauseTransition(Duration.seconds(2));
        ps.setOnFinished(e ->{
            wel.hide(primaryStage);
        });
        ps.playFromStart();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
