
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
                                  
                               int ñ = 0;
                               for( int p = 0; p <= lineas.size() - 2; p++ ){   
                                   
                                    String line = lineas.get(p);                                      
                                    System.out.println((lineas.size() - 2) + "  " + p + "  " + line);
                                    String valor = "";
                                    char c = '\0';                                          
                                    int index = 0;              
                                    boolean seguir = true;                                                                                                
                                         
                                    while( seguir ){                                       
                  
                                           c = (char)line.charAt(ñ);                                                                                                                                                                                                                                                                                                                                                               
                                           System.out.println("ñ = " + ñ + "   c = " + c);
                                           
                                           for( int m = 1; m <= rsmd.getColumnCount(); m++ ){
                                                String nc = rsmd.getColumnName(m);                                                                                                                                          
                                                System.out.println("columna = " + nc);
                                                if( nc.equals("id") || nc.equals("tipo_exa") || nc.equals("nombre") || nc.equals("clave") || 
                                                    nc.equals("clave_instrumento") ){ 
                                                    continue;                                                 
                                                }else{ longitud = rs.getInt(m); }                                                                                                                                                                                                                           
                                                
                                                if( ñ >= 60 ){      
                                                    
                                                    valor += c;
                                                    index++;                                                    
                                                   
                                                    if( index == longitud ){                                                                                                                                      
                                                        
                                                        ñ += index - 1;
                                                        System.out.println(ñ + " " + seguir + " " + valor + " " + longitud + " " + index);
                                                        index = 0;
                                                        values.add(valor);                           
                                                        valor = "";                                                               
                                                        break;
                                                        
                                                  }
                                                                     
                                              }                                                                                           
                                                                                                                                                                                                                                           
                                           }                                                                                                                       
                                           
                                           ñ++;                                               
                                           if( ñ == line.length() - 1 ){ 
                                               rs.beforeFirst();
                                               seguir = false; 
                                           }
                                         
                                    }                                                       
                                
                               }
                          
//                               for( String v : values ){
//                                    System.out.println("Valor = " + v);
//                               }
                               
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
