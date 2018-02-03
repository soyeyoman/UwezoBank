package Main.Models;

import java.sql.ResultSet;
import java.util.Random;


public class Accounts {

    public static void createAccount(String customerId,String name,String age){
        Databasehandler databasehandler = Databasehandler.getInstance();
         StringBuilder sb = new StringBuilder();
         if(customerId.length() > 1){
             sb.append(customerId.substring(0,1));
         }else{
             sb.append(customerId);
             sb.append("0");
         }

         sb.append(age);
         sb.append(new StringBuilder().append(age).reverse().toString());

         if(name.length() >= 10){
             sb.append(name.length());
         }else{
             sb.append(name.length());
             sb.append("0");
         }

         Random gen = new Random();
         int num1 = gen.nextInt(9);


         sb.append(num1);


         try{
             ResultSet rs = databasehandler.executeQuery("SELECT * FROM accounts WHERE number = "+sb.toString());
             if(!rs.next()){
                 System.out.println(sb.toString());
                 databasehandler.executeAction("INSERT INTO accounts(number,pin,balance,credit,customer_id) VALUES('"+sb.toString()
                         + "',1234,0,0,'"+customerId+"')");
             }

         }catch (Exception e){
             System.out.println(e.getMessage());
         }

    }
}
