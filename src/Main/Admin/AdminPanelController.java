package Main.Admin;

import Main.Models.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.sql.ResultSet;
import java.util.ConcurrentModificationException;

/**
 * Created by steve on 10/5/2017.
 */
public class AdminPanelController {
 private String admin;
 private boolean save = true;
 private Databasehandler databasehandler;
 private String employeeId ="";
 private boolean edit=false;
 private ObservableList<EmployeeData> employeeData;
 private ObservableList<EmployeeData> searchResTrans;
 private Search search = new Search();

 @FXML AnchorPane bottomEmployee;
 @FXML TextField email;
 @FXML TextField name;
 @FXML TextField city;
 @FXML TextField address;
 @FXML TextField p_address;
 @FXML TextField age;
 @FXML JFXButton setEdit;
 @FXML JFXButton saveEmployee;
 @FXML TableView<EmployeeData> employee_table;
 @FXML Label addwarning;
 @FXML JFXTabPane adminpane;
 @FXML TableColumn<String,EmployeeData> table_id;
 @FXML TableColumn<String,EmployeeData> table_name;
 @FXML TableColumn<String,EmployeeData> table_age;
 @FXML TableColumn<String,EmployeeData> table_email;
 @FXML TableColumn<String,EmployeeData> table_city;
 @FXML TableColumn<String,EmployeeData> table_address;
 @FXML TableColumn<String,EmployeeData> table_postal_address;

    public void setAdmin(String admin) {
        this.admin = admin;
        databasehandler = Databasehandler.getInstance();
        employeeData = FXCollections.observableArrayList();
        searchResTrans = FXCollections.observableArrayList();
        setEmployeeTable();
    }


    private void setEmployeeTable(){
          new Thread(()->{
        try{
            employeeData = FXCollections.observableArrayList();
            ResultSet rs = databasehandler.executeQuery("SELECT workers_info.*,workers.email,workers.name FROM workers_info,workers WHERE workers.id = workers_info.worker_id");
            while(rs.next()){
                employeeData.add(new EmployeeData(rs.getString("name"),rs.getString("age"),rs.getString("city"),rs.getString("address")
                        ,rs.getString("postal_address"),rs.getString("id"),rs.getString("email")));

            }

            table_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            table_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            table_city.setCellValueFactory(new PropertyValueFactory<>("city"));
            table_address.setCellValueFactory(new PropertyValueFactory<>("address"));
            table_postal_address.setCellValueFactory(new PropertyValueFactory<>("p_address"));
            table_age.setCellValueFactory(new PropertyValueFactory<>("age"));
            table_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            employee_table.setItems(null);
            employee_table.setItems(employeeData);

            employee_table.setRowFactory( tr ->{
                TableRow<EmployeeData> row = new TableRow<>();
                row.setOnMouseClicked( e ->{

                    if(e.getClickCount() == 2 && !row.isEmpty()){
                        name.setText(row.getItem().getName());
                        age.setText(row.getItem().getAge());
                        email.setText(row.getItem().getEmail());
                        address.setText(row.getItem().getAddress());
                        p_address.setText(row.getItem().getP_address());
                        city.setText(row.getItem().getCity());
                        setEnableDisable(false,false);
                        employeeId = row.getItem().getId();
                        setEdit.setVisible(true);
                        adminpane.getSelectionModel().select(0);
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
    @FXML void employeeDetails(KeyEvent event) {
        TextField field = (TextField) event.getSource();
        addwarning.setText("");

        if((!employeeId.equals("") && edit) || save){
            if(!city.isDisable()){
                if (field.equals(name)) {
                    if (!event.getCode().isLetterKey()) {
                        addwarning.setText("ENTER A VALID NAME !!");
                        saveEmployee.setDisable(true);

                    } else {
                        if (!name.getText().equals("") && !email.getText().equals("") &&
                                !age.getText().equals("") && !address.getText().equals("") && !p_address.getText().equals("")
                                && !city.getText().equals("")) {
                            saveEmployee.setDisable(false);
                        }else{
                            addwarning.setText("FILL IN ALL DETAILS !! ");
                        }
                    }
                }else if(field.equals(age)){
                    if(!event.getCode().isDigitKey()){
                        addwarning.setText("ENTER VALID AGE !!");
                        saveEmployee.setDisable(true);
                    }else if(field.getText().length() != 2){
                        addwarning.setText("ENTER VALID AGE !!");
                    }else{
                        if (!name.getText().equals("") && !email.getText().equals("") &&
                                !age.getText().equals("") && !address.getText().equals("") && !p_address.getText().equals("")
                                && !city.getText().equals("")) {
                            saveEmployee.setDisable(false);
                        }else{
                            addwarning.setText("FILL IN ALL DETAILS !! ");
                        }
                    }
                }else if(field.equals(city)){
                    if (!event.getCode().isLetterKey()) {
                        addwarning.setText("ENTER A VALID CITY !!");
                        saveEmployee.setDisable(true);
                    } else {
                        if (!name.getText().equals("") && !email.getText().equals("") &&
                                !age.getText().equals("") && !address.getText().equals("") && !p_address.getText().equals("")
                                && !city.getText().equals("")) {
                            saveEmployee.setDisable(false);
                        }else{
                            addwarning.setText("FILL IN ALL DETAILS !! ");
                        }
                    }
                }else{
                    if (!name.getText().equals("") && !email.getText().equals("") &&
                            !age.getText().equals("") && !address.getText().equals("") && !p_address.getText().equals("")
                            && !city.getText().equals("")) {
                        saveEmployee.setDisable(false);
                    }else{
                        addwarning.setText("FILL IN ALL DETAILS !! ");
                        saveEmployee.setDisable(true);
                    }
                }
            }

        }
    }

    public void saveEmployee() {
        try {
            if (save) {

                databasehandler.executeAction("INSERT INTO workers(email,password,name) VALUES('" + email.getText() + "','" +
                        Password.hash("password") + "','" + name.getText() + "')");
                ResultSet rs = databasehandler.executeQuery("SELECT id FROM workers WHERE email = '" + email.getText() + "'");
                rs.next();
                String id = rs.getString("id");
                databasehandler.executeAction("INSERT INTO workers_info(worker_id,age,city,address,postal_address) VALUES('" + id
                        + "','" + age.getText() + "','" + city.getText() + "','" + address.getText() + "','" + p_address.getText()
                        + "')");
                Accounts.createAccount(id, name.getText(), age.getText());

                rs = databasehandler.executeQuery("SELECT id FROM workers WHERE email ='" + email.getText() + "'");
                rs.next();
                employeeData.add(new EmployeeData(name.getText(), age.getText(), city.getText(), address.getText(), p_address.getText(), rs.getString("id"), email.getText()));


            } else {
                databasehandler.executeAction("UPDATE workers SET email = '" + email.getText() + "' WHERE id =" + employeeId);
                databasehandler.executeAction("UPDATE workers_info SET city = '" + city.getText() + "', address = '" + address.getText()
                        + "', postal_address ='" + p_address.getText() + "' WHERE id = " + employeeId);
                employeeData.forEach(t -> {
                    if (t.getId().equals(employeeId)) employeeData.remove(t);
                });

                employeeData.add(new EmployeeData(name.getText(), age.getText(), city.getText(), address.getText(), p_address.getText(), employeeId, email.getText()));

                employeeId = "";

                edit = false;
                save = true;
            }
        }catch (ConcurrentModificationException e) {
            System.out.println(e.getMessage());
            employeeData.add(new EmployeeData(name.getText(), age.getText(), city.getText(), address.getText(), p_address.getText(), employeeId, email.getText()));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally {
            employee_table.setItems(null);
            employee_table.setItems(employeeData);
            name.setText("");
            p_address.setText("");
            address.setText("");
            city.setText("");
            age.setText("");
            email.setText("");
            saveEmployee.setDisable(true);
            setEnableDisable(true,true);
            setEdit.setVisible(false);
        }


    }

    private void setEnableDisable(Boolean set,boolean age){

        if(age){
            this.age.setEditable(true);
            name.setEditable(true);
        }else{
            this.age.setEditable(false);
            name.setEditable(false);
        }
        email.setEditable(set);
        address.setEditable(set);
        p_address.setEditable(set);
        city.setEditable(set);
    }

    @FXML void setEdit() {
        if(edit){
            setEnableDisable(false,false);
            edit = false;
            saveEmployee.setDisable(true);
            save = true;
        }else{
            saveEmployee.setDisable(false);
            setEnableDisable(true,false);
            edit = true;
            save = false;
        }
    }

   @FXML void searchEmp() {
        searchResTrans =  search.searchEmployee(employeeData,bottomEmployee);
        employee_table.setItems(null);
        employee_table.setItems(searchResTrans);
    }

    @FXML void clearSearch() {
        searchResTrans.removeAll();
        employee_table.setItems(null);
        employee_table.setItems(employeeData);
        searchResTrans.addAll(employeeData);
        bottomEmployee.getChildren().forEach( child ->{
            if(child instanceof  TextField){
                ((TextField) child).setText("");
            }
        });
    }
}
