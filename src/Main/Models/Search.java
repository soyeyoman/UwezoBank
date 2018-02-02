package Main.Models;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Search {
    private  ObservableList<CustomerData> ansListCus;
    private ObservableList<TransactionData> ansListtrans;
    private boolean add = true;
    public ObservableList<CustomerData> searchCustomer(ObservableList<CustomerData> list, AnchorPane parent){
        ansListCus = FXCollections.observableArrayList();

        list.forEach( t -> {

            parent.getChildren().forEach(text ->{

                if(text instanceof TextField){
                    String key = ((TextField) text).getText();
                    if(!key.trim().equals("")){
                        switch (text.getId()){
                           case "id_search":
                               contains(key,t.getId());
                              break;
                           case "name_search":
                                contains(key,t.getName());
                               break;
                           case "age_search":
                               contains(key,t.getAge());
                               break;
                           case "city_search":
                                contains(key,t.getCity());
                               break;
                           case "postal_search":
                               contains(key,t.getPostalAddress());
                               break;
                           case "ad_search":
                                contains(key,t.getAddress());
                               break;
                           case "email_search":
                                contains(key,t.getEmail());
                               break;

                       }

                    }
                }
            });
            if (add)ansListCus.add(t);
            add = true;
        });
        return ansListCus;
    }

    private void contains(String key,String value){
       if(!value.toLowerCase().contains(key.toLowerCase())){
           add = false;
       }
    }

    public ObservableList<TransactionData> searchTransaction(ObservableList<TransactionData> list, AnchorPane parent) {
        ansListtrans = FXCollections.observableArrayList();

        list.forEach( t -> {

            parent.getChildren().forEach(text ->{

                if(text instanceof TextField){
                    String key = ((TextField) text).getText();
                    if(!key.trim().equals("")){
                        switch (text.getId()){
                            case "code_search":
                                contains(key,t.getCode());
                                break;
                            case "from_search":
                                contains(key,t.getFrom());
                                break;
                            case "to_search":
                                contains(key,t.getTo());
                                break;
                            case "date_search":
                                contains(key,t.getDate());
                                break;
                            case "amount_search":
                                contains(key,t.getAmount());
                                break;
                        }

                    }
                }else if(text instanceof ComboBox && ((ComboBox) text).getValue() != null){

                    contains(((ComboBox) text).getValue().toString(),t.getType());
                }
            });
            if (add)ansListtrans.add(t);
            add = true;
        });
        return ansListtrans;
    }
}
