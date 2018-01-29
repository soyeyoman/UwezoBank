package Main.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerData {
    private StringProperty name;
    private StringProperty email;
    private StringProperty age;
    private StringProperty address;
    private StringProperty postalAddress;
    private StringProperty city;
    private StringProperty id;

    public CustomerData(String name, String email, String age, String address, String postalAddress,String city,String id) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.email = new SimpleStringProperty(email);
        this.age = new SimpleStringProperty(age);
        this.address = new SimpleStringProperty(address);
        this.city= new SimpleStringProperty(city);
        this.postalAddress = new SimpleStringProperty(postalAddress);
    }


    public String getId() {
        return id.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getName() {
        return name.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getAge() {
        return age.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getPostalAddress() {
        return postalAddress.get();
    }


}
