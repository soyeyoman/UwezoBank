package Main.Customers;


import Main.Models.Databasehandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomerPageController{
    private int customerId;
    private  String customerEmail;
    private ResultSet customerDetails;
    private  Databasehandler handler = Databasehandler.getInstance();

    @FXML TextField age;
    @FXML TextField email;
    @FXML TextField full_name;
    @FXML TextField city;
    @FXML TextField address;
    @FXML TextField postal;



    public void setCustomer(String email) throws SQLException {
        ResultSet rs = handler.executeQuery("SELECT * FROM customers WHERE email ='"+email+"'");
        if(rs.next()){
            customerId = Integer.parseInt(rs.getString("id"));
            customerEmail = email;
        }
        customerDetails = handler.executeQuery("SELECT * FROM customer_info WHERE customer_id ='"+customerId+"'");
        customerDetails.next();

        age.setText(customerDetails.getString("age"));
        this.email.setText(email);
        full_name.setText(rs.getString("name"));
        city.setText(customerDetails.getString("city"));
        address.setText(customerDetails.getString("address"));
        postal.setText(customerDetails.getString("postal_address"));
    }
}
