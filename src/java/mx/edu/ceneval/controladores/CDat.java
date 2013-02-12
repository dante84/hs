
package mx.edu.ceneval.controladores;

// @author Daniel.Meza

import java.io.InputStream;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import mx.edu.ceneval.extras.Conexion;
import org.apache.commons.io.IOUtils;
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
                      
                      System.out.println("Antes del try");
                      
                      try{
                                                                              
                          InputStream is = archivo.getInputstream();
                          StringWriter sw = new StringWriter();
                          IOUtils.copy(is, sw);
                          String datos = sw.toString();
                          String valor = "";                                                                                                                                    
                          
                          System.out.println("Antes del resultset");
                          
                          cc = new Conexion();
                          conexion = cc.getC();
                          Statement s = conexion.createStatement();
                          ResultSet rs = s.executeQuery("select * from longitud_campos left outer join claves_examen on " +
                                                        "longitud_campos.clave_instrumento = claves_examen.clave_instrumento " +
                                                        "where claves_examen.clave = '" + clave + "'");                                                    
                                                                                  
                          System.out.println("ResultSet " + !rs.isBeforeFirst());
                          
                          int longitud = 0;                                                                                                                                                                                       
                          
                          if( !rs.isBeforeFirst() ){ context.addMessage(null, new FacesMessage("La clave de examen no existe. Verifica")); }
                          else{
                              
                               ResultSetMetaData rsmd = rs.getMetaData();                                                    
                               int lDatos = datos.length() - 1; 
           
                               rs.next();
                                                   
                               for( int m = 1; m <= rsmd.getColumnCount(); m++){
                                    String nc = rsmd.getColumnName(m);                                                                                                                                          
                                    if( nc.equals("id") || nc.equals("tipo_exa") || nc.equals("nombre") || nc.equals("clave") || 
                                        nc.equals("clave_instrumento") ){ 
                                        continue;                                                 
                                    }else{ longitud = rs.getInt(m); }                                                                                                
                                             
                                    //System.out.println("Caracter = " + c + " valor = " + valor + " longitud = " + longitud + " nc = " + nc);                                                                                                                           
                                    valor = regresaValor(longitud, lDatos, datos);
                                                                                                                                                                                                                                                                                                                                                                                                                            
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
       
       public String regresaValor(int longitud,int lDatos,String datos){
            
              String valor = "";
              char c = '\0';              
              
              for( int n = 1; n <= longitud; n++ ){
                                                                                                            
                   if( n == longitud ){                                                 
                       break;
                   }
                                                      
                   for( int ñ = 0; ñ <= lDatos; ñ++ ){
                                                                                                   
                        c = (char)datos.charAt(ñ);                                                                                                                                                                                                              
                                                                                                                                                                                                                                                                                                                
                        if( ñ >= 61 ){                                                                               
                            System.out.println("ñ = " + ñ + " c = " + c + " valor = " + valor );                                                                                                                           
                            valor += c;
                                                                                                                                                           
                            //System.out.println("longitud = " + longitud + " c = " + c + " valor = " + valor + " n = " + n);                                                                                                          
                                                       
                        }
                                                    
                   }    
                                                                                                                              
              } 
           
              return valor;
           
       }
       
    
}
