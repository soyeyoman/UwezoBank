package Main.Employees;

import Main.Models.Accounts;
import Main.Models.CustomerData;
import Main.Models.Databasehandler;
import Main.Models.TransactionData;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import java.sql.ResultSet;



/**
 * Created by steve on 10/5/2017.
 */
public class EmployeePageController {
    @FXML JFXButton saveCustomer;
    @FXML TextField customerName;
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
    @FXML TableColumn<CustomerData,String> customer_id;
    @FXML JFXTabPane employeepane;
    @FXML JFXButton setEdit;

    private String employeeID;
    private Databasehandler databasehandler = Databasehandler.getInstance();
    private ObservableList<CustomerData> customerdata;
    private ObservableList<TransactionData> transactiondata;
    private String customerId ="";
    private boolean save = true;
    private boolean edit = false;

    public void setEmployee(String employee) {
        try {
            ResultSet rs = databasehandler.executeQuery("SELECT id FROM workers WHERE email = '"+employee+"'");
            rs.next();
            employeeID = rs.getString("id");

            setCustomerTable();
            setTransactionsTable();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        saveCustomer.setDisable(true);


    }

    private void setTransactionsTable() {
        try{
            transactiondata = FXCollections.observableArrayList();
            ResultSet rs = databasehandler.executeQuery("SELECT * FROM transactions");
            while (rs.next()){
                transactiondata.add(new TransactionData(rs.getString("code"),rs.getString("recepient_id"),
                        rs.getString("initializer_id")
                        ,rs.getString("date"),rs.getString("amount"),rs.getString("amount")));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void setCustomerTable() {
        try{
            customerdata = FXCollections.observableArrayList();
            ResultSet rs = databasehandler.executeQuery("SELECT customer_info.*,customers.email FROM customer_info,customers WHERE customers.id = customer_info.customer_id");
            while(rs.next()){
                customerdata.add(new CustomerData(rs.getString("name"),rs.getString("email"),
                        rs.getString("age"),rs.getString("address"),rs.getString("postal_address"),rs.getString("city"),rs.getString("customer_id")));
            }

            full_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            email.setCellValueFactory(new PropertyValueFactory<>("email"));
            city.setCellValueFactory(new PropertyValueFactory<>("city"));
            address.setCellValueFactory(new PropertyValueFactory<>("address"));
            postal_address.setCellValueFactory(new PropertyValueFactory<>("postalAddress"));
            age.setCellValueFactory(new PropertyValueFactory<>("age"));
            customer_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            customer_table.setItems(null);
            customer_table.setItems(customerdata);

            customer_table.setRowFactory( tr ->{
                TableRow<CustomerData> row = new TableRow<>();
                row.setOnMouseClicked( e ->{

                    if(e.getClickCount() == 2 && !row.isEmpty()){
                         customerName.setText(row.getItem().getName());
                         customerAge.setText(row.getItem().getAge());
                         customerEmail.setText(row.getItem().getEmail());
                         customerAd.setText(row.getItem().getAddress());
                         customerPAD.setText(row.getItem().getPostalAddress());
                         customerCity.setText(row.getItem().getCity());
                         setEnableDisable(false,false);
                         customerId = row.getItem().getId();
                         setEdit.setVisible(true);
                         employeepane.getSelectionModel().select(0);
                         save = false;

                    }

                });
                return row;
            });


        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

     private void setEnableDisable(Boolean set,boolean age){

         if(age){
             customerAge.setEditable(true);
             customerName.setEditable(true);
         }else{
             customerAge.setEditable(false);
             customerName.setEditable(false);
         }
         customerEmail.setEditable(set);
         customerAd.setEditable(set);
         customerPAD.setEditable(set);
         customerCity.setEditable(set);
     }
    @FXML void saveCustomer(){

         if(save ){
             try{

                 databasehandler.executeAction("INSERT INTO customers(email,password) VALUES('"+customerEmail.getText()+"','password')");
                 ResultSet rs = databasehandler.executeQuery("SELECT id FROM customers WHERE email = '"+customerEmail.getText()+"'");
                 rs.next();
                 String id = rs.getString("id");
                 databasehandler.executeAction("INSERT INTO customer_info(customer_id,age,city,address,postal_address,name) VALUES("+id
                         +","+customerAge.getText()+",'"+customerCity.getText()+"','"+customerAd.getText()+"','"+customerPAD.getText()
                         +"','"+customerName.getText()+"')");
                 Accounts.createAccount(id,customerName.getText(),customerAge.getText());

             }catch (Exception e){
                 System.out.println(e.getMessage());
                 System.out.println("save customer!!");
             }
         }else{
              databasehandler.executeAction("UPDATE customers SET email = '"+customerEmail.getText()+"' WHERE id ="+customerId);
              databasehandler.executeAction("UPDATE customer_info SET city = '"+customerCity.getText()+"', address = '"+customerAd.getText()
                      +"', postal_address ='"+customerPAD.getText()+"' WHERE id = "+customerId);
              customerId = "";
              edit = false;
              save = true;
         }
        customerName.setText("");
        customerPAD.setText("");
        customerAd.setText("");
        customerCity.setText("");
        customerAge.setText("");
        customerEmail.setText("");
        saveCustomer.setDisable(true);
        setEnableDisable(true,true);
        setEdit.setVisible(false);

    }

    public void customerDetails(KeyEvent keyEvent) {
        TextField field = (TextField) keyEvent.getSource();
        addcustomerwarning.setText("");

        if((!customerId.equals("") && edit) || save){
            if(!customerCity.isDisable()){
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

    }


    public void setEdit() {
        if(edit){
            setEnableDisable(false,false);
            edit = false;
            saveCustomer.setDisable(true);
            save = true;
        }else{
            saveCustomer.setDisable(false);
            setEnableDisable(true,false);
            edit = true;
            save = false;
        }

    }
}
