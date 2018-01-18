package Main.Prompts;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Vector;

//Handles all confirmations
public class ConfirmBox {

	  static boolean ans ;
	public  boolean display(String title, String meso, Vector<Double> pos){
	    Stage window = new Stage();
	    
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(200);
		
		Button ybutton = new Button("Yes");
		Button nbutton = new Button("No");

		ybutton.setOnAction(e -> {
			ans = true;
			window.close();
			});

		nbutton.setOnAction(e -> {
			ans = false;
			window.close();
			});
		nbutton.getStyleClass().add("sidebtn");
		ybutton.getStyleClass().add("sidebtn");
		HBox hbox = new HBox();
		hbox.getChildren().addAll(ybutton,nbutton);
		hbox.setSpacing(3);
		hbox.setAlignment(Pos.CENTER);
		Label labe = new Label();
		labe.setText(meso);
	    VBox box = new VBox();
	    box.setSpacing(5);
	    nbutton.prefWidth(10);
	    ybutton.prefWidth(10);
	    box.setPadding(new Insets(3,3,3,3));
	    box.setPrefHeight(150);
	    box.setPrefWidth(200);
	    box.getChildren().addAll(labe,hbox);
	    box.setAlignment(Pos.CENTER);
	    box.setStyle("-fx-border-width:2px;-fx-border-color: dodgerblue;");
	   // box.getStyleClass().add("sidebtn");
	    box.getStylesheets().add(getClass().getResource("../Main.css").toString());
		Scene scene = new Scene(box);
	    window.setScene(scene);
	    window.initStyle(StageStyle.UNDECORATED);
	    window.setX(pos.get(0)+300);
	    window.setY(pos.get(1)+150);
	    window.showAndWait();
	    return ans;
	}
	
	
}
