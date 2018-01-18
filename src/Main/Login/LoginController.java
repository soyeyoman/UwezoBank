package Main.Login;

import Main.Models.LoginModel;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML JFXDrawer drawer;
    @FXML Label userid;
    @FXML JFXHamburger hamburger;
    @FXML JFXTextField pin;
    @FXML JFXTextField email_user;
    @FXML JFXPasswordField password;
    @FXML Label error;
    private String user = "customer";
    LoginModel model = new LoginModel();
    HamburgerBackArrowBasicTransition back;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {


            VBox sidepane = FXMLLoader.load(getClass().getResource("sidebar.fxml"));
            drawer.setSidePane(sidepane);
             back = new HamburgerBackArrowBasicTransition(hamburger);
            back.setRate(-1);

            hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, this::hideDraw);

            for(Node node :sidepane.getChildren()){

                    if(node.getAccessibleText() != null){
                        switch (node.getAccessibleText()){
                            case "customer":
                                node.addEventHandler(MouseEvent.MOUSE_CLICKED,e -> {
                                    userid.setText("Customer Login");
                                    email_user.promptTextProperty().set("Email");
                                    pin.visibleProperty().setValue(true);
                                    user = "customer";
                                });

                                break;
                            case "employee":
                                node.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
                                    userid.setText("Employee Login");
                                    email_user.promptTextProperty().set("Email");
                                    pin.visibleProperty().setValue(false);
                                    user = "employee";
                                });

                                break;
                            case "admin":
                                node.addEventHandler(MouseEvent.MOUSE_CLICKED,e->{
                                    userid.setText("Admin Login");
                                    email_user.promptTextProperty().set("User Name");
                                    pin.visibleProperty().setValue(false);
                                    user = "admin";
                                });

                                break;
                            default:
                                break;
                        }
                    }

                }

        } catch (IOException e){
            e.printStackTrace();
        }


    }

    @FXML void login(){
        error.setText("");
        System.out.println(user + " "+password.getText()+" "+email_user.getText()+ " "+pin.getText());
        boolean log =  model.login(user,password.getText(),email_user.getText(),(Stage)email_user.getScene().getWindow(),pin.getText());
        if(!log){
            error.setText("Wrong Credentials !!");
        }
    }

    @FXML void hideDraw(Event e){


        if(drawer.isShown()){
            drawer.close();
            back.setRate(back.getRate() * -1);
            back.play();
        }else if(e.getSource().equals(hamburger)){
            drawer.open();
            back.setRate(back.getRate() * -1);
            back.play();
        }
    }
}

