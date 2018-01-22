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

    public CustomerData(String name, String email, String age, String address, String postalAddress,String city) {
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.age = new SimpleStringProperty(age);
        this.address = new SimpleStringProperty(address);
        this.city= new SimpleStringProperty(city);
        this.postalAddress = new SimpleStringProperty(postalAddress);
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
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

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPostalAddress() {
        return postalAddress.get();
    }

    public StringProperty postalAddressProperty() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress.set(postalAddress);
    }
}
