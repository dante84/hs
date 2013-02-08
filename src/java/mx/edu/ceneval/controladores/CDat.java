
package mx.edu.ceneval.controladores;

// @author Daniel.Meza

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import mx.edu.ceneval.extras.Conexion;
import org.primefaces.model.UploadedFile;
 
public class CDat {
    
       private UploadedFile archivo;          
       private FacesContext context = FacesContext.getCurrentInstance();
       private Connection conexion;
       private Conexion cc;
       
       public UploadedFile getArchivo() { return archivo; }

       public void setArchivo(UploadedFile up) { this.archivo = up; }
       
       public void subir(ActionEvent ae){                         
              
              if( archivo != null ){
                  
                  String nombreArchivo = archivo.getFileName();              
                  int clave = Integer.parseInt( nombreArchivo.substring(6,10) ); 
                  
                  System.out.println("Nombre archivo " + nombreArchivo + " Clave = " + clave );
                  
                  if( nombreArchivo.endsWith(".dat") ){
                      
                      try{
                                                                              
                          File f1 = (File)archivo;
                          
                          System.out.println("File " + f1.getName());
                          
                          FileInputStream fis = new FileInputStream(f1);                                                     
                          
                          char c;                                                                    
                          int i = 0;
                          String valor = "";                                                    
                          
                          cc = new Conexion();
                          conexion = cc.getC();
                          Statement s = conexion.createStatement();
                          ResultSet rs = s.executeQuery("select * from longitud_campos left outer join claves_examen on " +
                                                        "longitud_campos.clave_instrumento = claves_examen.clave_instrumento " +
                                                        "where claves_examen.clave = '" + clave + "'");                                                    
                               
                          
                          
                          if( !rs.isBeforeFirst() ){ context.addMessage(null, new FacesMessage("La clave de examen no existe. Verifica")); }
                          else{
                                ResultSetMetaData rsmd = rs.getMetaData();                                                    
                                while( true ){
                                                                     
                                       c = (char)fis.read();                                                                                                               
                                           
                                       System.out.println("Caracter = " + c);
                                       
                                       if( c == -1 ){ break; }
                                                                                                            
                                       /*
                                       if( i >= 61 ){                                                                               
                                     
                                           int longitud = 0;                                                                                                                                                                                       
                                           rs.next();
                                                   
                                           for( int m = 2; m <= rsmd.getColumnCount(); m++){
                                                String nc = rsmd.getColumnName(m);                                                                                             
                                             
                                                if( !nc.equals("id") && !nc.equals("tipo_exa") && !nc.equals("nombre") &&
                                                    !nc.equals("clave") && !nc.equals("clave_instrumento") ){
                                                    longitud = rs.getInt(m);                                          
                                                }else{ continue; }                                                                                                
                                             
                                                if( c != '\n' ){
                                                    for( int n = 1; n <= longitud; n++ ){
                                                         valor += c;
                                                                                                                                                           
                                                         System.out.println("longitud = " + longitud + " c = " + c + " valor = " + valor + " n = " + n);
                                                    
                                                         if( n == longitud ){                                          
                                                             System.out.println("valor: " + valor);                                                              
                                                             valor = "";
                                                         }
                                                         
                                                    }
                                                    
                                                }    
                                                                                      
                                        
                                           }
                                             
                                           
                                           
                                       }*/
                                                                                                       
                                       i++;
                                                                  
                                }                                                       
                                
                          }
                          
                          rs.close();
                          s.close();
                          conexion.close();
                          
                      }catch(Exception e){}
                                            
                  }else{                      
                        context.addMessage(null, new FacesMessage("El archivo es invalido"));                        
                  }
                                                      
              }
                                          
       }
    
}
