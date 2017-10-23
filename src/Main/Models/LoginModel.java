package Main.Models;

import Main.Admin.AdminPanelController;
import Main.Customers.CustomerPageController;
import Main.Employees.EmployeePageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
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
        ResultSet rs = handler.executeQuery("SELECT * FROM workers WHERE email ='"+email+"' AND password ='"+password+"'");
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
          ResultSet rs = handler.executeQuery("SELECT * FROM admins WHERE user='"+user+"' AND password = '"+password+"'");
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
        ResultSet rs = handler.executeQuery("SELECT * FROM customers WHERE email ='"+email+"' AND password = '"+password+"'");
        try {
            if(rs.next()){
                String userid = rs.getString("id");
                rs = handler.executeQuery("SELECT * FROM accounts WHERE holder_id ='"+userid+"' AND pin ='"+pin+"'");
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

    private void getView(String user,String userDetail) throws IOException {
        String viewpath = "";
        FXMLLoader loader;
        Parent root = null;
        switch (user){
            case "admin":
                viewpath = "../Admin/adminpanel.fxml";
                loader = new  FXMLLoader(getClass().getResource(viewpath));
                 root = loader.load();
                break;
            case "worker":
                viewpath = "../Employees/employeepage.fxml";
                loader = new  FXMLLoader(getClass().getResource(viewpath));
                 root = loader.load();
                break;
            case "customer":
                viewpath = "../Customers/customerpage.fxml";
                loader = new  FXMLLoader(getClass().getResource(viewpath));
                root = loader.load();
                break;

        }
        stage.setScene(new Scene(root,800,500));
    }


}
