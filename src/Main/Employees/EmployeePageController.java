package Main.Employees;

import Main.Models.*;
import Main.Prompts.ConfirmBox;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Vector;


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
    @FXML TableColumn<TransactionData,String> trans_code;
    @FXML TableColumn<TransactionData,String> trans_from;
    @FXML TableColumn<TransactionData,String> trans_to;
    @FXML TableColumn<TransactionData,String> trans_date;
    @FXML TableColumn<TransactionData,String> trans_amount;
    @FXML TableColumn<TransactionData,String> trans_type;
    @FXML TableView<TransactionData> trans;
    @FXML JFXTabPane employeepane;
    @FXML JFXButton setEdit;
    @FXML AnchorPane bottomCustomer;
    @FXML ComboBox<TypeOption> type_choose;
    @FXML AnchorPane bottomTransaction;
    @FXML TextField oldpass;
    @FXML TextField newpass;
    @FXML TextField confirmpass;
    @FXML Label  change_info;
    @FXML JFXButton changeBtn;

    private String employeeID;
    private Databasehandler databasehandler = Databasehandler.getInstance();
    private ObservableList<CustomerData> customerdata;
    private ObservableList<TransactionData> transactiondata;
    private Search search = new Search();
    ObservableList<CustomerData> searchResultCus;
    ObservableList<TransactionData> searchResultTrans;
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
        type_choose.setItems(FXCollections.observableArrayList(TypeOption.values()));
    }


    private void setTransactionsTable() {
      new Thread(()->{
          try{
              transactiondata = FXCollections.observableArrayList();
              ResultSet rs = databasehandler.executeQuery("SELECT * FROM transactions");
              while (rs.next()){
                  ResultSet from = databasehandler.executeQuery("SELECT number FROM accounts WHERE customer_id="+rs.getString("initializer_id"));
                  from.next();

                  String toNum;
                  if(!rs.getString("recepient_id").equals("0")){
                      ResultSet to = databasehandler.executeQuery("SELECT number FROM accounts WHERE customer_id="+rs.getString("recepient_id"));
                      to.next();
                      toNum = to.getString("number");
                  }else{
                      toNum = "null";
                  }


                  transactiondata.add(new TransactionData(rs.getString("code"),toNum,
                          from.getString("number")
                          ,rs.getString("time"),rs.getString("amount"),rs.getString("type").trim()));

                  trans_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                  trans_code.setCellValueFactory(new PropertyValueFactory<>("code"));
                  trans_from.setCellValueFactory(new PropertyValueFactory<>("from"));
                  trans_to.setCellValueFactory(new PropertyValueFactory<>("to"));
                  trans_date.setCellValueFactory(new PropertyValueFactory<>("date"));
                  trans_type.setCellValueFactory(new PropertyValueFactory<>("type"));
                  trans.setItems(null);
                  trans.setItems(transactiondata);

              }

              trans.setRowFactory( rf ->{
                  TableRow<TransactionData> row = new TableRow<>();
                  row.setOnMouseClicked( e ->{
                      if(e.getClickCount() == 2){
                          try {
                              TransactionData td = row.getItem();
                              FXMLLoader loader = new FXMLLoader(getClass().getResource("transactionview.fxml"));
                              Parent root = loader.load();
                              TransactionViewController controller = loader.getController();
                              controller.setTransaction(td,employeeID);
                              Stage stage = new Stage();
                              stage.initModality(Modality.APPLICATION_MODAL);
                              stage.setScene(new Scene(root));
                              stage.setResizable(false);
                              stage.showAndWait();
                          } catch (IOException e1) {
                              e1.printStackTrace();
                          }

                      }
                  });
                  return row;
              });

           Thread.sleep(60000);
          }catch (Exception e){
              System.out.println(e.getMessage());
          }

      }).start();

       type_choose.valueProperty().addListener( e->{
           searchTrans();
       });
    }

    private void setCustomerTable() {
       new Thread(()->{
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


           try {
               Thread.sleep(6000);
           } catch (InterruptedException e) {
               System.out.println(  e.getMessage());
           }
       }).start();
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

                 databasehandler.executeAction("INSERT INTO customers(email,password,pin) VALUES('"+customerEmail.getText()+"','"+
                         Password.hash("password")+"','"+Password.hash("1234")+"')");
                 ResultSet rs = databasehandler.executeQuery("SELECT id FROM customers WHERE email = '"+customerEmail.getText()+"'");
                 rs.next();
                 String id = rs.getString("id");
                 databasehandler.executeAction("INSERT INTO customer_info(customer_id,age,city,address,postal_address,name) VALUES("+id
                         +","+customerAge.getText()+",'"+customerCity.getText()+"','"+customerAd.getText()+"','"+customerPAD.getText()
                         +"','"+customerName.getText()+"')");
                 Accounts.createAccount(id,customerName.getText(),customerAge.getText());

                 rs = databasehandler.executeQuery("SELECT id FROM customers WHERE email ='"+customerEmail.getText()+"'");
                 rs.next();
                 customerdata.add(new CustomerData(customerName.getText(),customerEmail.getText(),customerAge.getText(),customerAd.getText(),
                         customerPAD.getText(),customerCity.getText(),rs.getString("id")));

             }catch (Exception e){
                 System.out.println(e.getMessage());
             }
         }else{
              databasehandler.executeAction("UPDATE customers SET email = '"+customerEmail.getText()+"' WHERE id ="+customerId);
              databasehandler.executeAction("UPDATE customer_info SET city = '"+customerCity.getText()+"', address = '"+customerAd.getText()
                      +"', postal_address ='"+customerPAD.getText()+"' WHERE id = "+customerId);
              customerdata.forEach( t ->{
                  if(t.getId().equals(customerId))customerdata.remove(t);
              });

             customerdata.add(new CustomerData(customerName.getText(),customerEmail.getText(),customerAge.getText(),customerAd.getText(),
                     customerPAD.getText(),customerCity.getText(),customerId));
             customerId = "";

              edit = false;
              save = true;
         }
        customer_table.setItems(null);
        customer_table.setItems(customerdata);
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

    @FXML void customerDetails(KeyEvent keyEvent) {
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


    @FXML void setEdit() {
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


    @FXML void searchCus() {
        searchResultCus =  search.searchCustomer(customerdata,bottomCustomer);
        customer_table.setItems(null);
        customer_table.setItems(searchResultCus);

    }


    @FXML void searchTrans() {
        searchResultTrans =  search.searchTransaction(transactiondata,bottomTransaction);
        trans.setItems(null);
        trans.setItems(searchResultTrans);

    }

    @FXML void clearcustomerSearch() {
        searchResultCus.removeAll();
        customer_table.setItems(null);
        customer_table.setItems(customerdata);
        searchResultCus.addAll(customerdata);
        bottomCustomer.getChildren().forEach( child ->{
            if(child instanceof  TextField){
                ((TextField) child).setText("");
            }
        });
    }

    @FXML void cleartransacionSearch(){
        searchResultTrans.removeAll();
        trans.setItems(null);
        trans.setItems(transactiondata);
        searchResultTrans.addAll(transactiondata);
        bottomTransaction.getChildren().forEach( child ->{
            if(child instanceof  TextField){
                ((TextField) child).setText("");
            }
        });
    }

    public void changePassKeyEntered(KeyEvent event) {
        if(event.getSource().equals(newpass) || event.getSource().equals(confirmpass)){
            if(!newpass.getText().equals(confirmpass.getText())){
                change_info.setText("New password does not match confirmation !!");
                changeBtn.setDisable(true);
            }else{
                change_info.setText("");
                if(!oldpass.getText().trim().equals("") && !oldpass.getText().trim().equals("") && !oldpass.getText().trim().equals("")){
                    changeBtn.setDisable(false);
                }else{
                    change_info.setText("Fill in all fields !!");
                    changeBtn.setDisable(false);
                }
            }
        }else{
            if(!oldpass.getText().trim().equals("") && !oldpass.getText().trim().equals("") && !oldpass.getText().trim().equals("")){
              changeBtn.setDisable(false);
            }else{
                change_info.setText("Fill in all fields !!");
                changeBtn.setDisable(true);
            }
        }

    }

    public void changePassword() {
        double x = oldpass.getScene().getWindow().getX();
        double y = oldpass.getScene().getWindow().getY();
        Vector<Double> pos = new Vector<>();
        pos.add(x);
        pos.add(y);
        try{
            ResultSet rs = databasehandler.executeQuery("SELECT password FROM workers WHERE id = "+employeeID);
            rs.next();
            String olpass = Password.hash(oldpass.getText().trim());
            if(olpass.equals(rs.getString("password"))){
                boolean accept =  new ConfirmBox().display("Change pin!!"," CHANGE PIN !!",pos);
                if(accept){
                    databasehandler.executeAction("UPDATE workers SET password ='"+Password.hash(newpass.getText())+"'");
                    oldpass.setText("");
                    newpass.setText("");
                    confirmpass.setText("");
                    changeBtn.setDisable(true);
                }

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
