package Main.Models;

import java.sql.*;

public class Databasehandler {
    public static  Connection con = null;
    public static Databasehandler handler = null;
    private static Statement stmt = null;

    public Databasehandler(){
        createConnection();
    }
    public static  Databasehandler getInstance(){
        if (handler == null) handler = new Databasehandler();
        return handler;
    }
    private void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/uwezobank","root","");
        } catch (SQLException ex) {
            System.out.println("Error connecting to database");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public ResultSet executeQuery(String q){
        ResultSet res = null;
        try {
            stmt = con.createStatement();
            res = stmt.executeQuery(q);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }


    public  int executeAction(String q){
        int res  = 0;
        try {
            stmt = con.createStatement();
            res = stmt.executeUpdate(q);
            System.out.println(res);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(res);
        return res;
    }



}
