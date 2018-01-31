package Main.Models;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Search {
    private  ObservableList<CustomerData> ansList;
    private boolean add = true;
    public ObservableList<CustomerData> searchCustomer(ObservableList<CustomerData> list, AnchorPane parent){
        ansList = FXCollections.observableArrayList();

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
            if (add)ansList.add(t);
            add = true;
        });
        return ansList;
    }

    private void contains(String key,String value){
       if(!value.toLowerCase().contains(key.toLowerCase())){
           add = false;
       }
    }
}
