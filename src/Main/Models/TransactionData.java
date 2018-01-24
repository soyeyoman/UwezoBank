package Main.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransactionData {
    private StringProperty code;
    private StringProperty to;
    private StringProperty from;
    private StringProperty date;
    private StringProperty amount;
    private StringProperty type;


    public TransactionData(String code, String to, String from, String date, String amount,String type) {
        this.code = new SimpleStringProperty(code);
        this.to = new SimpleStringProperty(to);
        this.from = new SimpleStringProperty(from);
        this.date = new SimpleStringProperty(date);
        this.type = new SimpleStringProperty(type);
        this.amount = new SimpleStringProperty(amount);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getCode() {
        return code.get();
    }

    public StringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getTo() {
        return to.get();
    }

    public StringProperty toProperty() {
        return to;
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public String getFrom() {
        return from.get();
    }

    public StringProperty fromProperty() {
        return from;
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }
}
