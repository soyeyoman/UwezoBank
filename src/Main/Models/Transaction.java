package Main.Models;


import java.sql.ResultSet;

public class Transaction {
   static Databasehandler databasehandler = Databasehandler.getInstance();
    public static void createTransaction(String fromAccount,String toAccount,String type,double Amount){
        StringBuilder sb = new StringBuilder();
        sb.append("T");
        if(type.equals("W"))sb.append("W");
        if(type.equals("S"))sb.append("S");
        if(type.equals("D"))sb.append("D");

        long time = System.nanoTime();
        String tim = String.format("%d",time).substring(0,7);
        sb.append(tim);
        if(!toAccount.equals("0"))sb.append("X").append(toAccount.charAt(2));

        saveTransaction(fromAccount,toAccount,Amount,sb.toString(),type);
    }

    private static void saveTransaction(String fromAccount,String toAccount,double amount,String code,String type) {
       try {
           ResultSet fromDetails = databasehandler.executeQuery("SELECT customer_id FROM accounts WHERE number =" + fromAccount);
           fromDetails.next();
           String fromId = fromDetails.getString("customer_id");
           String toId = "0";
           if (!toAccount.equals("0")) {
               ResultSet toDetails = databasehandler.executeQuery("SELECT customer_id FROM accounts WHERE number = " + toAccount);
               toDetails.next();
               toId = toDetails.getString("customer_id");
           }

           databasehandler.executeAction("INSERT INTO transactions(code,type,amount,initializer_id,recepient_id) VALUES('"+code+"','"+
                   type+"',"+amount+","+fromId+","+toId+")");

       }catch (Exception e){
           System.out.println(e.getMessage());
       }
    }
}
