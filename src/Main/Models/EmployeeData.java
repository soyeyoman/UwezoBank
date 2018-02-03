package Main.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmployeeData {
    private StringProperty name;
    private StringProperty age;
    private StringProperty city;
    private StringProperty address;
    private StringProperty p_address;
    private StringProperty id;
    private StringProperty email;

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public EmployeeData(String name, String age, String city, String address, String p_address, String id, String email) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleStringProperty(age);
        this.city = new SimpleStringProperty(city);
        this.address = new SimpleStringProperty(address);
        this.p_address = new SimpleStringProperty(p_address);
        this.id = new SimpleStringProperty(id);
        this.email = new SimpleStringProperty(email);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAge() {
        return age.get();
    }

    public StringProperty ageProperty() {
        return age;
    }

    public void setAge(String age) {
        this.age.set(age);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getP_address() {
        return p_address.get();
    }

    public StringProperty p_addressProperty() {
        return p_address;
    }

    public void setP_address(String p_address) {
        this.p_address.set(p_address);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }
}
