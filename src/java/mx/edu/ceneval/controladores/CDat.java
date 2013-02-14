
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
import java.util.Set;
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
                           
                          ArrayList<String> lineas = new ArrayList<String>();
                          String linea = "";
                          InputStream is = archivo.getInputstream();
                          
                          Map<String,String> valores = new HashMap<String,String>();  
                          
                          BufferedReader br = new BufferedReader(new InputStreamReader(is));
                          
                          while( (linea = br.readLine()) != null ){                                 
                                 lineas.add(linea);                                                                                                   
                          }
                          
                          int i = 1;
                          for(String line : lineas ){
                              System.out.println( i + " - " + line);
                              i++;
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
                               
                               int indice = 0;
                               for( int p = 0; p <= lineas.size() - 2; p++ ){                                                                             
                                    String line = lineas.get(p);
                                    for( int m = 1; m <= rsmd.getColumnCount(); m++){
                                         String nc = rsmd.getColumnName(m);                                                                                                                                          
                                         if( nc.equals("id") || nc.equals("tipo_exa") || nc.equals("nombre") || nc.equals("clave") || 
                                             nc.equals("clave_instrumento") ){ 
                                             continue;                                                 
                                         }else{ longitud = rs.getInt(m); }                                                                                                
                                                                                   
                                         valores = regresaValor(longitud,line,indice,nc);                                         
                                         
                                         Set keys = valores.keySet();
                                         
                                         for(Object value : keys ){                                             
                                             System.out.println("Columna = " + nc + " valor = " + valores.get((String)value) + " longitud = " + longitud );
                                         }
                                         
                                    }                                                       
                                
                               }
                          
                               rs.close();
                               s.close();
                               conexion.close();
                               
                          }
                          
                     }catch(Exception e){}
                                            
                 }else{ context.addMessage(null, new FacesMessage("El archivo es invalido")); }
                                                      
              }
                                          
       }
       
       public Map<String,String> regresaValor(int longitud,String linea,int indice,String clave){
            
              Map<String,String> valores = new HashMap<String,String>();              
              String valor = "";
              char c = '\0';              
              
              int i = 0;
              for( int ñ = indice; ñ <= linea.length() - 1; ñ++ ){
                                                                                                   
                   c = (char)linea.charAt(ñ);                                                                                                                                                                                                              
                                                                                                                                                                                                                                                                                                                
                   if( ñ >= 60 ){                                                                                                                             
                       valor += c;
                       i++;                                                    
                       if( i == longitud ){                                                                            
                           i = 0;
                           valores.put(clave, valor);
                           valores.put("indice",String.valueOf(indice));
                           valor = "";                           
                       }
                       
                   }
                   
                   indice++;
                                                                                                                                                                                                                                           
              } 
           
              return valores;
           
       }
           
}
