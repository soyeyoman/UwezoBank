package Main.Customers;


import Main.Models.Databasehandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerPageController implements Initializable {
    public String user ;
    Databasehandler handler = Databasehandler.getInstance();

    @FXML TextField age;
    @FXML TextField email;
    @FXML TextField full_name;
    @FXML TextField city;
    @FXML TextField address;
    @FXML TextField postal;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
