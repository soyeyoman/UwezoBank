package Main.Models;

import Main.Admin.AdminPanelController;
import Main.Customers.CustomerPageController;
import Main.Employees.EmployeePageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by steve on 10/18/2017.
 */
public class LoginModel {
    Databasehandler handler = Databasehandler.getInstance();
    Stage stage;

    public boolean login(String user,String password,String email_user,Stage stage,String pin){
        this.stage = stage;
        boolean logged = false;
        switch (user){
            case "admin":
                logged = adminLogin(email_user,password);
                break;
            case "employee":
                logged = workerLogin(email_user,password);
                break;
            case "customer":
                logged =  customerLogin(email_user,password,pin);
                break;
            default:
                break;
        }
        return logged;
    }

    private boolean workerLogin(String email, String password) {
        ResultSet rs = handler.executeQuery("SELECT * FROM workers WHERE email ='"+email+"' AND password ='"+Password.hash(password)+"'");
        try {
            if(rs.next()){
                getView("worker",email);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean adminLogin(String user, String password)  {
          ResultSet rs = handler.executeQuery("SELECT * FROM admins WHERE user_name='"+user+"' AND password = '"+Password.hash(password)+"'");
          try {
              if(rs.next()){
                   getView("admin",user);
                  return true;
              }
          } catch (SQLException e) {
              System.out.println(e.getMessage());
          } catch (IOException e) {
              e.printStackTrace();
          }
        return false;
    }

    private boolean customerLogin(String email, String password,String pin) {
        ResultSet rs = handler.executeQuery("SELECT * FROM customers WHERE email ='"+email+"' AND password = '"+Password.hash(password)+"'");
        try {
            if(rs.next()){
                String userid = rs.getString("id");
                rs = handler.executeQuery("SELECT * FROM accounts WHERE customer_id ='"+userid+"' AND pin ='"+Password.hash(pin)+"'");
                if(rs.next()){
                    getView("customer",email);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
     }

    private void getView(String user,String email_user) throws IOException, SQLException {
        String viewpath = "";
        FXMLLoader loader;
        Parent root = null;
        switch (user){
            case "admin":
                viewpath = "../Admin/adminpanel.fxml";
                loader = new  FXMLLoader();
                loader.setLocation(getClass().getResource(viewpath));
                root = loader.load();
                AdminPanelController Acon = loader.getController();
                Acon.setAdmin(email_user);

                break;
            case "worker":
                viewpath = "../Employees/employeepage.fxml";
                loader = new  FXMLLoader();
                loader.setLocation(getClass().getResource(viewpath));
                root = loader.load();
                EmployeePageController Econ = loader.getController();
                Econ.setEmployee(email_user);

                break;
            case "customer":
                viewpath = "../Customers/customerpage.fxml";
                loader = new  FXMLLoader();
                loader.setLocation(getClass().getResource(viewpath));
                root = loader.load();
                CustomerPageController con = loader.getController();
                 con.setCustomer(email_user);

                break;

        }
        stage.setScene(new Scene(root,800,500));
    }


}
