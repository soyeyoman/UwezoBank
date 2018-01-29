package Main.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.TextField;

public class Search {
    private  ObservableList<CustomerData> ansList;

    public ObservableList<CustomerData> searchCustomer(ObservableList<CustomerData> list, AnchorPane parent){
        ansList = FXCollections.observableArrayList();

        parent.getChildren().forEach( child ->{
            if(child instanceof TextField){
                String key = ((TextField) child).getText();
                switch(child.getId()){
                    case "id_search":
                        list.forEach( t->{
                           add(key,t.getId(),t);
                        });
                    case "name_search":
                        list.forEach( t ->{
                            add(key,t.getName(),t);
                        });
                    case "age_search":
                        list.forEach( t ->{
                            add(key,t.getAge(),t);
                        });
                     case "email_search":
                         list.forEach(t ->{
                              add(key,t.getEmail(),t);
                         });
                    case "":

                }
            }
        });
        return ansList;
    }

    private void add(String key,String value,CustomerData t){
     if(!ansList.contains(t)){
         if(value.startsWith(key)){
             ansList.add(0,t);
         }else if(value.contains(key)){
             ansList.add(t);
         }
      }
    }
}
