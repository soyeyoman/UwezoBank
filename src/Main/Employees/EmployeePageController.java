package Main.Employees;

import Main.Models.Accounts;
import Main.Models.CustomerData;
import Main.Models.Databasehandler;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.awt.*;
import java.sql.ResultSet;



/**
 * Created by steve on 10/5/2017.
 */
public class EmployeePageController {
    @FXML JFXButton saveCustomer;
    @FXML
    TextField customerName;
    @FXML TextField customerAge;
    @FXML TextField customerEmail;
    @FXML TextField customerAd;
    @FXML TextField customerPAD;
    @FXML TextField customerCity;
    @FXML Label addcustomerwarning;
    @FXML TableColumn<CustomerData,String> full_name;
    @FXML TableColumn<CustomerData,String> email;
    @FXML  TableColumn<CustomerData,String> age;
    @FXML TableColumn<CustomerData,String> postal_address;
    @FXML TableColumn<CustomerData,String> city;
    @FXML TableColumn<CustomerData,String> address;
    @FXML TableView<CustomerData> customer_table;

    private String employeeID;
    private Databasehandler databasehandler = Databasehandler.getInstance();
    private ObservableList<CustomerData> data;
    public void setEmployee(String employee) {
        try {
            ResultSet rs = databasehandler.executeQuery("SELECT id FROM workers WHERE email = '"+employee+"'");
            rs.next();
            employeeID = rs.getString("id");

            setCustomerTable();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        saveCustomer.setDisable(true);


    }

    private void setCustomerTable() {
        try{
            data = FXCollections.observableArrayList();
            ResultSet rs = databasehandler.executeQuery("SELECT customer_info.*,customers.email FROM customer_info,customers WHERE customers.id = customer_info.customer_id");
            while(rs.next()){
                data.add(new CustomerData(rs.getString("name"),rs.getString("email"),
                        rs.getString("age"),rs.getString("address"),rs.getString("postal_address"),rs.getString("city")));
            }

            full_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            city.setCellValueFactory(new PropertyValueFactory<>("city"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            postal_address.setCellValueFactory(new PropertyValueFactory<>("postalAddress"));
            age.setCellValueFactory(new PropertyValueFactory<>("age"));
            customer_table.setItems(null);
            customer_table.setItems(data);




        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @FXML void saveCustomer(){
       try{

           databasehandler.executeAction("INSERT INTO customers(email,password) VALUES('"+customerEmail.getText()+"','password')");
           ResultSet rs = databasehandler.executeQuery("SELECT id FROM customers WHERE email = '"+customerEmail.getText()+"'");
           rs.next();
           String id = rs.getString("id");
           databasehandler.executeAction("INSERT INTO customer_info(customer_id,age,city,address,postal_address,name) VALUES("+id
                   +","+customerAge.getText()+",'"+customerCity.getText()+"','"+customerAd.getText()+"','"+customerPAD.getText()
                   +"','"+customerName.getText()+"')");
           Accounts.createAccount(id,customerName.getText(),customerAge.getText());
           customerName.setText("");
           customerPAD.setText("");
           customerAd.setText("");
           customerCity.setText("");
           customerAge.setText("");
           customerEmail.setText("");
           saveCustomer.setDisable(true);
       }catch (Exception e){
           System.out.println(e.getMessage());
           System.out.println("save customer!!");
       }
    }

    public void customerDetails(KeyEvent keyEvent) {
        TextField field = (TextField) keyEvent.getSource();
        addcustomerwarning.setText("");
        if (field.equals(customerName)) {
            if (!keyEvent.getCode().isLetterKey()) {
                addcustomerwarning.setText("ENTER A VALID NAME !!");
                saveCustomer.setDisable(true);
            } else {
                if (!customerName.getText().equals("") && !customerEmail.getText().equals("") &&
                        !customerAge.getText().equals("") && !customerAd.getText().equals("") && !customerPAD.getText().equals("")
                        && !customerCity.getText().equals("")) {
                    saveCustomer.setDisable(false);
                }else{
                    addcustomerwarning.setText("FILL IN ALL DETAILS !! ");
                }
            }
        }else if(field.equals(customerAge)){
            if(!keyEvent.getCode().isDigitKey()){
                addcustomerwarning.setText("ENTER VALID AGE !!");
                saveCustomer.setDisable(true);
            }else if(field.getText().length() != 2){
                addcustomerwarning.setText("ENTER VALID AGE !!");
            }else{
                if (!customerName.getText().equals("") && !customerEmail.getText().equals("") &&
                        !customerAge.getText().equals("") && !customerAd.getText().equals("") && !customerPAD.getText().equals("")
                        && !customerCity.getText().equals("")) {
                    saveCustomer.setDisable(false);
                }else{
                    addcustomerwarning.setText("FILL IN ALL DETAILS !! ");
                }
            }
        }else if(field.equals(customerCity)){
            if (!keyEvent.getCode().isLetterKey()) {
                addcustomerwarning.setText("ENTER A VALID CITY !!");
                saveCustomer.setDisable(true);
            } else {
                if (!customerName.getText().equals("") && !customerEmail.getText().equals("") &&
                        !customerAge.getText().equals("") && !customerAd.getText().equals("") && !customerPAD.getText().equals("")
                        && !customerCity.getText().equals("")) {
                    saveCustomer.setDisable(false);
                }else{
                    addcustomerwarning.setText("FILL IN ALL DETAILS !! ");
                }
            }
        }else{
            if (!customerName.getText().equals("") && !customerEmail.getText().equals("") &&
                    !customerAge.getText().equals("") && !customerAd.getText().equals("") && !customerPAD.getText().equals("")
                    && !customerCity.getText().equals("")) {
                saveCustomer.setDisable(false);
            }else{
                addcustomerwarning.setText("FILL IN ALL DETAILS !! ");
                saveCustomer.setDisable(true);
            }
        }
    }


}
