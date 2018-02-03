package Main.Employees;

import Main.Models.Databasehandler;
import Main.Models.TransactionData;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

            public class TransactionViewController {

                @FXML
                TextField acc_no;
                @FXML
                TextField acc_name;
                @FXML
                TextField amount;
                @FXML
                TextField code;
                @FXML
                TextField date;
                @FXML
                Label trans_type;
                @FXML
                TextField to_no;
                @FXML
                TextField to_name;
                @FXML
                JFXButton reversebtn;
                private String employeeId;

                public void setTransaction(TransactionData trans,String employeeId) {
                    try {
                        this.employeeId = employeeId;

                        Databasehandler databasehandler = Databasehandler.getInstance();
                        ResultSet rs = databasehandler.executeQuery("SELECT name FROM customer_info,accounts WHERE customer_info.customer_id  = accounts.customer_id AND accounts.number =" + trans.getFrom());
                        rs.next();
                        acc_name.setText(rs.getString("name"));
                        acc_no.setText(trans.getFrom());
                        amount.setText(trans.getAmount());
                        code.setText(trans.getCode());
                        date.setText(trans.getDate());

                        switch (trans.getType()) {
                            case "W":
                                trans_type.setText("Widthrawal");
                                to_name.setText("null");
                                to_no.setText("null");
                                break;
                            case "D":
                                trans_type.setText("Deposit");
                                to_name.setText("null");
                                to_no.setText("null");
                                break;
                            case "S":
                                trans_type.setText("Transfer");
                                databasehandler = Databasehandler.getInstance();
                                rs = databasehandler.executeQuery("SELECT name FROM customer_info,accounts WHERE " +
                                        " customer_info.customer_id  = accounts.customer_id AND accounts.number =" + trans.getTo());
                                rs.next();
                                to_name.setText(rs.getString("name"));
                                to_no.setText(trans.getTo());
                                reversebtn.setVisible(true);
                                break;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }

                @FXML
                void reverse() {
                    try {
                        Databasehandler databasehandler = Databasehandler.getInstance();
                        databasehandler.executeAction("DELETE  FROM transactions WHERE code='" + code.getText() + "'");
                        databasehandler.executeAction("INSERT INTO `reversed` (`code`, `employee_id`, `amount`, `transaction_time`, `from_account`,`to_account`) VALUES ( '" + code.getText() + "', '" + employeeId
                                + "', '" + amount.getText() + "', '" + date.getText() + "', '" + acc_no.getText() + "','" + to_no.getText() + "')");
                        databasehandler.executeAction("UPDATE accounts SET balance = balance + " + amount.getText() + " WHERE number=" + acc_no.getText());

                        ResultSet balanceRes = databasehandler.executeQuery("SELECT balance FROM accounts WHERE number=" + to_no.getText());
                        balanceRes.next();
                        String balance = balanceRes.getString("balance");

                        if(!balance.equals("0")){
                            databasehandler.executeAction("UPDATE accounts SET balance = balance - " + amount.getText() + " WHERE number=" + to_no.getText());
                        }else{
                            databasehandler.executeAction("UPDATE accounts SET credit=" + amount.getText() + " WHERE number=" + to_no.getText());
                        }

                        ((Stage) code.getScene().getWindow()).close();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }

            }
