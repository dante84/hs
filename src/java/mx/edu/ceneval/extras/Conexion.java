
package mx.edu.ceneval.extras;

// @author Daniel.Meza

import java.sql.Connection;
import java.sql.DriverManager;
 
public class Conexion {
     
       private Connection c;
       
       public Conexion(){   
              
              try{
                  Class.forName("com.mysql.jdbc.Driver");
                  c = DriverManager.getConnection("jdbc:mysql://localhost/replicasiipo","root","slipknot");
              }
              catch(Exception e){ e.printStackTrace(); }
            
       }
    
       public Connection getC() {
              return c;
       }              
    
}
