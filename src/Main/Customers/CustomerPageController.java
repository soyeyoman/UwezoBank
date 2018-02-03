package Main.Customers;


import Main.Models.Databasehandler;
import Main.Models.Password;
import Main.Models.Transaction;
import Main.Prompts.ConfirmBox;
import Main.Prompts.Prompt;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;


public class CustomerPageController{
    private int customerId;
    private ResultSet customerDetails;
    private String customerEmail;
    private  Databasehandler handler = Databasehandler.getInstance();
    private ResultSet accountDetails;

    @FXML JFXTabPane customerPage;
    @FXML JFXTabPane accounts;
    @FXML TextField age;
    @FXML TextField email;
    @FXML TextField full_name;
    @FXML TextField city;
    @FXML TextField address;
    @FXML TextField postal;
    @FXML TextField acc_name;
    @FXML TextField acc_no;
    @FXML TextField balance;
    @FXML Label widthraw_balance;
    @FXML Label after_widthdraw;
    @FXML TextField amount_widthdraw;
    @FXML JFXButton widthdrawbtn;
    @FXML TextField deposit;
    @FXML JFXButton depositbtn;
    @FXML JFXButton transferbtn;
    @FXML Label after_transfer;
    @FXML Label transfer_name;
    @FXML TextField transfer_account;
    @FXML TextField transfer_amount;
    @FXML JFXButton changePinBtn;
    @FXML PasswordField currentPin;
    @FXML PasswordField newPin;
    @FXML PasswordField confirmPin;
    @FXML Label infoLabel;

    public void setCustomer(String email) throws SQLException {
        ResultSet rs = handler.executeQuery("SELECT * FROM customers WHERE email ='"+email+"'");
        if(rs.next()){
            customerId = Integer.parseInt(rs.getString("id"));
            customerEmail = rs.getString("email");
        }
        customerDetails = handler.executeQuery("SELECT * FROM customer_info WHERE customer_id ='"+customerId+"'");
        customerDetails.next();
        accountDetails = handler.executeQuery("SELECT * FROM accounts WHERE customer_id='"+customerId+"'");
        accountDetails.next();
        age.setText(customerDetails.getString("age"));
        this.email.setText(email);
        full_name.setText(customerDetails.getString("name"));
        city.setText(customerDetails.getString("city"));
        address.setText(customerDetails.getString("address"));
        postal.setText(customerDetails.getString("postal_address"));
        acc_name.setText(customerDetails.getString("name"));
        acc_no.setText(accountDetails.getString("number"));
        balance.setText(accountDetails.getString("balance"));
        widthraw_balance.setText(accountDetails.getString("balance"));

        widthdrawbtn.setDisable(true);
        depositbtn.setDisable(true);
        transferbtn.setDisable(true);
        changePinBtn.setDisable(true);
    }

    public void edit() {
        email.setEditable(true);
        city.setEditable(true);
        postal.setEditable(true);
        address.setEditable(true);

    }

    public void savePersonal() throws SQLException {
        if(!email.getText().equals("") && !email.getText().equals(customerEmail)){
            handler.executeAction("UPDATE customers SET email ='"+email.getText()+"'");
        }

        if(!city.getText().equals("") && !customerDetails.getString("city").equals(city.getText())){
            handler.executeAction("UPDATE customer_info SET city ='"+city.getText()+"'");
        }

        if(!postal.getText().equals("") && !customerDetails.getString("postal_address").equals(postal.getText())){
            handler.executeAction("UPDATE customer_info SET postal_address ='"+postal.getText()+"'");
        }

        if(!address.getText().equals("") && !customerDetails.getString("address").equals(address.getText())){
            handler.executeAction("UPDATE customer_info SET address ='"+address.getText()+"'");
        }

    }

    public void widthrawAmountEntered() {
        String amount = amount_widthdraw.getText();

        if(!amount.trim().equals("")){
            char val = amount.charAt(amount.length()-1);
            double value = 0;
            if(Character.isDigit(val)){
                value = Double.parseDouble(amount);
            }else if(val == '.'){
                amount = amount + 0;
                value = Double.parseDouble(amount);
            }

            double balance = Double.parseDouble(widthraw_balance.getText());
            if(value < balance && value > 0) {
                after_widthdraw.setText(String.format("%f",balance-value));
                widthdrawbtn.setDisable(false);
            }else{
                if(value > 0){
                    after_widthdraw.setText("NOT ENOUGH CASH !!");
                }else{
                    after_widthdraw.setText("Enter Amount !!");
                }
                widthdrawbtn.setDisable(true);
            }
        }


    }

    public void widthdraw(ActionEvent actionEvent) {
       String balance = after_widthdraw.getText();
       String amount = amount_widthdraw.getText();
       Stage win =(Stage) this.balance.getScene().getWindow();
       double x =  win.xProperty().get();
       double y =  win.yProperty().get();
        Vector<Double> pos = new Vector<>();
        pos.add(x);
        pos.add(y);
       boolean accept = new ConfirmBox().display("WidthDraw","Widthdraw "+amount+" ksh ?",pos);


       if(accept){
           try {



               ResultSet res = handler.executeQuery("SELECT balance FROM accounts WHERE customer_id="+customerId);
               res.next();
               if(!res.getString("balance").equals("0")){
                   handler.executeAction("UPDATE accounts SET balance = " + balance + " WHERE customer_id ='" + customerId + "'");
               }
               accountDetails = handler.executeQuery("SELECT * FROM accounts WHERE customer_id='" + customerId + "'");
               accountDetails.next();
               this.balance.setText(accountDetails.getString("balance"));
               widthraw_balance.setText(accountDetails.getString("balance"));
               after_widthdraw.setText("");
               amount_widthdraw.setText("0");
               Transaction.createTransaction(accountDetails.getString("number"),
                       "0","W",Double.parseDouble(amount));
               customerPage.getSelectionModel().select(0);
               accounts.getSelectionModel().select(1);
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
       }
    }

    public void deposit() {
        if(!deposit.getText().trim().equals("")){
            try{
                double value = Double.parseDouble(deposit.getText());
                Stage win =(Stage) this.balance.getScene().getWindow();
                double x =  win.xProperty().get();
                double y =  win.yProperty().get();
                Vector<Double> pos = new Vector<>();
                pos.add(x);
                pos.add(y);
                boolean accept = new ConfirmBox().display("Deposit","Deposit "+value+"!! ",pos);

                if(accept){
                    ResultSet res = handler.executeQuery("SELECT credit FROM accounts WHERE customer_id="+customerId);
                    res.next();
                    Double credit = Double.parseDouble(res.getString("credit"));
                    System.out.println("credit = "+credit);
                    if(credit == 0){
                        handler.executeAction("UPDATE accounts SET balance = balance + "+value+" WHERE customer_id ='"+customerId+"'");
                    }else{

                        if((credit - value) > 0 || (credit-value) == 0){
                            handler.executeAction("UPDATE accounts SET credit = credit - "+value+" WHERE customer_id ='"+customerId+"'");
                            System.out.println("credit  1");
                        }else if((value - credit) > 0){
                            System.out.println("credit 2");
                            double balance = value - credit;
                            handler.executeAction("UPDATE accounts SET credit = 0 WHERE customer_id ='"+customerId+"'");
                            handler.executeAction("UPDATE accounts SET balance = balance + "+balance+" WHERE customer_id ='"+customerId+"'");
                        }

                    }

                    accountDetails = handler.executeQuery("SELECT * FROM accounts WHERE customer_id='"+customerId+"'");
                    accountDetails.next();
                    Transaction.createTransaction(accountDetails.getString("number"),"0","D",Double.parseDouble(deposit.getText()));
                    this.balance.setText(accountDetails.getString("balance"));
                    widthraw_balance.setText(accountDetails.getString("balance"));
                    deposit.setText("0");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
               deposit.setDisable(false);
               deposit.setText(deposit.getText().substring(0,deposit.getText().length()-1));
            }
        }
    }

    public void depositEntered() {
        String depo = deposit.getText();
        if(!depo.trim().equals("")){
            char val = depo.charAt(depo.length()-1);
            if(Character.isDigit(val)){
                depositbtn.setDisable(false);
            }else{
                depositbtn.setDisable(true);
            }
        }

    }

    public void accountNameSearch() {

        String account_no = transfer_account.getText().trim();
        if(account_no.length()>6 && !account_no.equals(acc_no.getText())){
            char val = account_no.charAt(account_no.length()-1);
                if(Character.isDigit(val)){
                    try{
                       ResultSet transferDetails = handler.executeQuery("SELECT customer_info.name "+
                               "FROM accounts,customer_info WHERE accounts.customer_id = customer_info.customer_id "
                               +" AND accounts.number = "+Integer.parseInt(account_no));
                       transferDetails.next();
                        System.out.println(account_no);
                       transfer_name.setText(transferDetails.getString("name"));
                       if(!after_transfer.getText().trim().equals("")){
                            transferbtn.setDisable(false);
                       }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        transferbtn.setDisable(true);
                    }
                }
        }else{
            transfer_name.setText("");
            transferbtn.setDisable(true);
        }
    }

    public void transfer() {
        double value = Double.parseDouble(transfer_amount.getText());
        Vector<Double> pos = getWindowpos();
        boolean accept = new ConfirmBox().display("Transfer","Transfer  "+value+" to !! "+transfer_name.getText(),pos);

        if(accept){
            try{
                double amount =  Double.parseDouble(balance.getText()) - value;
                handler.executeAction("UPDATE accounts SET balance = "+amount+" WHERE customer_id ='"+customerId+"'");
                accountDetails = handler.executeQuery("SELECT * FROM accounts WHERE customer_id='"+customerId+"'");
                accountDetails.next();
                this.balance.setText(accountDetails.getString("balance"));
                widthraw_balance.setText(accountDetails.getString("balance"));
                deposit.setText("0");

                ResultSet rs = handler.executeQuery("SELECT * FROM accounts WHERE number = "+transfer_account.getText());
                rs.next();

                Double credit = Double.parseDouble(rs.getString("credit"));
                if(credit == 0){
                    handler.executeAction("UPDATE accounts SET balance = balance + "+value+" WHERE number = "+transfer_account.getText());
                }else{
                    if((credit - value) == 0 || (credit - amount) > 0 ){
                        handler.executeAction("UPDATE accounts SET credit = credit - "+value+" WHERE number =" +transfer_account.getText());
                    }else if(value-credit > 0){
                        double balance = value - credit;
                        handler.executeAction("UPDATE accounts SET credit = 0 WHERE number ="+transfer_account.getText());
                        handler.executeAction("UPDATE accounts SET balance = balance + "+balance+" WHERE number ="+transfer_account.getText());
                    }
                }


                Transaction.createTransaction(accountDetails.getString("number"),rs.getString("number"),"S",value);

                transfer_account.setText("");
                transfer_amount.setText("");
                transfer_name.setText("");
                after_transfer.setText("");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }

    }

    public void transferAmountEntered() {
        String amount = transfer_amount.getText();

        if(!amount.trim().equals("")){
            char val = amount.charAt(amount.length()-1);
            double value = 0;
            if(Character.isDigit(val)){
                value = Double.parseDouble(amount);
            }else if(val == '.'){
                amount = amount + 0;
                value = Double.parseDouble(amount);
            }

            double balance = Double.parseDouble(widthraw_balance.getText());
            if(value <= balance && value > 0) {
                after_transfer.setText(String.format("%f",balance-value));
                 if(!transfer_name.getText().trim().equals("")){
                     transferbtn.setDisable(false);
                }
            }else{
                if(value > 0){
                    after_transfer.setText("NOT ENOUGH CASH !!");
                }else{after_transfer.setText("Enter Amount !!");
                }
                transferbtn.setDisable(true);
            }
        }else{
            after_transfer.setText("");
            transferbtn.setDisable(true);
        }
    }

    public void changePin() {
        Prompt prompt = new Prompt();
        Vector<Double> pos = getWindowpos();
        if(newPin.getText().equals(confirmPin.getText())){
            try{
                if(Password.hash(currentPin.getText().trim()).equals(accountDetails.getString("pin"))){
                   boolean accept =  new ConfirmBox().display("Change pin!!"," CHANGE PIN !!",pos);
                    if(accept){
                        handler.executeAction("UPDATE accounts SET pin = '"+Password.hash(newPin.getText())+"' WHERE number = "+accountDetails.getString("number"));
                        prompt.display("CHANGED !!","PIN CHANGED !!",false,pos);
                        newPin.setText("");
                        confirmPin.setText("");
                        currentPin.setText("");
                    }
                }else{
                    prompt.display("WRONG PIN!!","CURRENT PIN NOT CORRECT !!",false,pos);
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }else{
            prompt.display("NO MATCH !!","New pin does not much confirmation !!",false,pos);
        }
    }

    private Vector<Double> getWindowpos() {
        Stage win =(Stage) this.balance.getScene().getWindow();
        double x =  win.xProperty().get();
        double y =  win.yProperty().get();
        Vector<Double> pos = new Vector<>();
        pos.add(x);
        pos.add(y);
        return pos;
    }

    public void pinKeyTyped(KeyEvent keyEvent) {
        infoLabel.setText("");
        if(keyEvent.getCode().isDigitKey()){
               PasswordField pass = (PasswordField)keyEvent.getSource();
               if(pass.getText().length() == 4){
                   if(!newPin.getText().trim().equals("") && !currentPin.getText().trim().equals("") && !confirmPin.getText().trim().equals("")) {
                       changePinBtn.setDisable(false);
                   }
               }else{
                   infoLabel.setText("PIN LENGTH SHOULD BE 4 !!");
                   changePinBtn.setDisable(true);
               }

        }else{
            infoLabel.setText("ENTER ONLY DIGITS !!");
            changePinBtn.setDisable(true);
        }
    }
}
