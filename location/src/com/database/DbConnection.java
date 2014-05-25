
package com.database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.common.Common;
import com.common.LangLat;

public class DbConnection {

public DbConnection() {
}

 public void doJDBC(String email, String lat, String longi, String day, String month, String year) {
          final String JDBC_DRIVER = Common.JDBC_DRIVER; 
          final String DB_URL = Common.DB_URL;

      //  Database credentials
          final String USER = Common.USER;
          final String PASS = Common.PASS;
          Connection conn = null;
          Statement stmt = null;
          try {
         //STEP 2: Register JDBC driver
         Class.forName("com.mysql.jdbc.Driver");

         //STEP 3: Open a connection
         System.out.println("Connecting to a selected database...");
         conn = DriverManager.getConnection(DB_URL, USER, PASS);
         System.out.println("Connected database successfully...");
        
         //STEP 4: Execute a query
         System.out.println("Creating statement...");
         stmt = conn.createStatement();

         String sql = "INSERT into location_values values ('"+email+"','"+lat+"','"+longi+"','"+day+"','"+month+"',"+"'"+year+"')";
         int rs = stmt.executeUpdate(sql);
         //STEP 5: Extract data from result set
      
      }catch(SQLException se){
         //Handle errors for JDBC
         se.printStackTrace();
      }catch(Exception e){
         //Handle errors for Class.forName
         e.printStackTrace();
      }finally{
         //finally block used to close resources
         try{
            if(stmt!=null)
               conn.close();
         }catch(SQLException se){
         }// do nothing
         try{
            if(conn!=null)
               conn.close();
         }catch(SQLException se){
            se.printStackTrace();
         }//end finally try
      }//end try
      System.out.println("Goodbye!");
      }

public List<LangLat> getData(String emailid, String day, String
		 month, String year) {
	// TODO Auto-generated method stub
	final String JDBC_DRIVER = Common.JDBC_DRIVER; 
    final String DB_URL = Common.DB_URL;

//  Database credentials
    final String USER = Common.USER;
    final String PASS = Common.PASS;
    Connection conn = null;
    Statement stmt = null;
    List<LangLat> list = new ArrayList<LangLat>();
    try {
   //STEP 2: Register JDBC driver
   Class.forName("com.mysql.jdbc.Driver");

   //STEP 3: Open a connection
   System.out.println("Connecting to a selected database...");
   conn = DriverManager.getConnection(DB_URL, USER, PASS);
   System.out.println("Connected database successfully...");
  
   //STEP 4: Execute a query
   System.out.println("Creating statement...");
   stmt = conn.createStatement();
   System.out.println("Getting data email" + emailid + " "+day +" "+month+" "+year);
   String sql = "SELECT EMAIL, latitude, longitude FROM location_values where EMAIL = '"+emailid+"'"+
		   		"and day ='"+day+"'"+"and month='"+month+"'"+"and year='"+year+"'";
   ResultSet rs = stmt.executeQuery(sql);
   System.out.println("Getting data email -1");
   while(rs.next()){
       //Retrieve by column name
       String email  = rs.getString("EMAIL");
       String lat = rs.getString("latitude");
       String longi = rs.getString("longitude");
       System.out.println("Getting data email -2");
       LangLat obj = new LangLat();
       obj.lat = lat;
       obj.longi = longi;
       list.add(obj);
       //
    }
    rs.close();
   //STEP 5: Extract data from result set

}catch(SQLException se){
   //Handle errors for JDBC
   se.printStackTrace();
}catch(Exception e){
   //Handle errors for Class.forName
   e.printStackTrace();
}finally{
   //finally block used to close resources
   try{
      if(stmt!=null)
         conn.close();
   }catch(SQLException se){
   }// do nothing
   try{
      if(conn!=null)
         conn.close();
   }catch(SQLException se){
      se.printStackTrace();
   }//end finally try
}//end try
    System.out.println("Goodbye! -----for getting datat");
    return list;
}

}
