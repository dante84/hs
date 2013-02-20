
package mx.edu.ceneval.controladores;

// @author Daniel.Meza

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
       private ArrayList<String> values = new ArrayList<String>();
       
       public UploadedFile getArchivo() { return archivo; }

       public void setArchivo(UploadedFile up) { this.archivo = up; }
       
       public void subir(ActionEvent ae){                         
              
              if( archivo != null ){
                  
                  String nombreArchivo = archivo.getFileName();              
                  int clave = Integer.parseInt( nombreArchivo.substring(6,10) ); 
                  
                  System.out.println("Nombre archivo " + nombreArchivo + " Clave = " + clave );
                  
                  if( nombreArchivo.endsWith(".dat") ){                                            
                      
                      try{
                           
                          ArrayList<String> lineas = new ArrayList<String>();
                          String linea = "";
                          InputStream is = archivo.getInputstream();
                          
                          Map<String,String> valores = new HashMap<String,String>();  
                          
                          BufferedReader br = new BufferedReader(new InputStreamReader(is));
                          
                          while( (linea = br.readLine()) != null ){                                 
                                 lineas.add(linea);                                                                                                   
                          }                                                                                                       
                                                                                                                             
                          cc = new Conexion();
                          conexion = cc.getC();
                          Statement s = conexion.createStatement();
                          ResultSet rs = s.executeQuery("select * from longitud_campos left outer join claves_examen on " +
                                                        "longitud_campos.clave_instrumento = claves_examen.clave_instrumento " +
                                                        "where claves_examen.clave = '" + clave + "'");                                                    
                                                                                                                                      
                          int longitud = 0;                                                                                                                                                                                       
                                                    
                          if( !rs.isBeforeFirst() ){ context.addMessage(null, new FacesMessage("La clave de examen no existe. Verifica")); }
                          else{
                              
                               ResultSetMetaData rsmd = rs.getMetaData();                                                    
                               rs.next();
                                  
                               int noColumnas = rsmd.getColumnCount();
                               int noLineas = lineas.size() - 2;
                               String dato = "";
                               int posicionInicio = rs.getInt("posicion_inicio");
                               int h = 1;
                               int indice = 0;
                               
                               System.out.println(noColumnas + " " + noLineas + " " + posicionInicio );
                               
                               int k = 0;
                               while( k <= noLineas ){
                                   
                                      String line = lineas.get(k); 
                                      int tamañoLinea = line.length() - 1;
                                      System.out.println( k + "  -  " + line );                                                                                                            
                                    
                                      int i = 0;
                                      int l = 0;
                                      while( i <= tamañoLinea ){
                                                                                                                         
                                             char c = line.charAt(i);                                                                                                                                                                        
                                             boolean paso = true;
                                             while( paso ){
                                             
                                                    String nc = rsmd.getColumnName(h);
                                                
                                                    //System.out.println(nc);
                                                
                                                    if( nc.equals("tipo_exa") || nc.equals("version_diccionario") || nc.equals("id") ||
                                                        nc.equals("nombre") ){ 
                                                        h++;
                                                        continue; 
                                                    }else{ longitud = rs.getInt(h); } 
                                                        
                                                    System.out.println(l + " " + posicionInicio);                                                                                                
                                                    
                                                    if( l > posicionInicio ){
                                                    
                                                        System.out.println( l + "  -  c  =  " + c );
                                                        dato += c;                                                                                          
                                                        indice++;
                                                    
                                                        if( indice == longitud ){
                                                        
                                                            indice = 0;
                                                            l += longitud;
                                                            h++;
                                                            System.out.println("Dato = " + dato);
                                                            dato = "";                                                                                                                                                                                                                                                
                                                            
                                                        }
                                                    
                                                    } 
                                                
                                                    h++;
                                                    l++;
                                                
                                           }
                                            
                                           i++;
                                             
                                      }
                                        
                                     
                                      k++;
                                      h = 1;         
                                    
                               }
                                                                                        
                               rs.close();
                               s.close();
                               conexion.close();
                               
                          }
                          
                     }catch(Exception e){}
                                            
                 }else{ context.addMessage(null, new FacesMessage("El archivo es invalido")); }
                                                      
              }
                                          
       }
       
       public void regresaValor(int longitud,String linea,int indice,String clave){
            
              String valor = "";
              char c = '\0';                            
              
              int i = 0;              
              boolean seguir = true;
              int ñ = 0;
              
              while( seguir ){                                       
                  
                     c = (char)linea.charAt(ñ);                                                                                                                                                                                                              
                                                                                                                                                                                                                                                                                                                
                     if( ñ >= 60 ){                                                                                                                             
                         valor += c;
                         i++;                                                    
                         if( i == longitud ){                                                                                                                                      
                             ñ += i - 1;
                             System.out.println(ñ + " " + seguir + " " + valor + " " + longitud + " " + i);
                             i = 0;
                             values.add(valor);                           
                             valor = "";                                                               
                         }
                                                                     
                     }
                                          
                     ñ++;                     
                     
                     if( ñ > linea.length() - 1 ){ seguir = false; }                                          
                                                                                                                                                                                                                                           
              } 
                      
       }
           
}
