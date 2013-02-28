
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
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import mx.edu.ceneval.extras.Conexion;
import org.primefaces.model.UploadedFile;
 
public class CDat {
    
       private UploadedFile archivo;          
       private FacesContext context = FacesContext.getCurrentInstance();
       private Connection conexion;
       private Conexion cc;
       private ArrayList<SelectItem> diccionarios;
       private String diccionarioSeleccionado = "Test";     
       private UISelectOne selectOne;
       
       public CDat(){
           
              diccionarios = new ArrayList<SelectItem>();              
              
              try{                                   
                  
                  cc = new Conexion();
                  conexion = cc.getC();
                  Statement s = conexion.createStatement();
                  ResultSet rs = s.executeQuery("select version_diccionario from longitud_campos");                                                                            
                  
                  if( rs.isBeforeFirst() ){
                      while( rs.next() ){
                             
                             String dic = rs.getString(1);
                             if( dic != null ){
                                 diccionarios.add(new SelectItem("Try",dic,"Catch"));
                                 context.addMessage(null, new FacesMessage("Diccionario agregado : " + dic));
                             }
                             
                      }
                  }                                    
                  
              }catch(Exception e){ e.printStackTrace(); }                                                                                                                              
              
       }
       
       public UploadedFile getArchivo() { return archivo; }

       public void setArchivo(UploadedFile up) { this.archivo = up; }              
       
       public void subir(ActionEvent ae){                         
              
              for( SelectItem item : diccionarios ){
                   System.out.println("Item = " + item.getValue());
              }
              
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
                                                        "where claves_examen.clave = '" + clave + "' and version_diccionario = '" +
                                                         diccionarioSeleccionado + "'");                                                    
                                                                                                                                                                                                                   
                          if( !rs.isBeforeFirst() ){ context.addMessage(null, new FacesMessage("La clave de examen no existe. Verifica")); }
                          else{                                                             
                               
                               ResultSetMetaData rsmd = rs.getMetaData();                                                    
                               rs.next();
                               
                               int noColumnas = rsmd.getColumnCount();
                               ArrayList<Integer> longitudes = new ArrayList<Integer>();
                                                                                             
                               for( int h = 1; h <= noColumnas; h++ ){
                                   
                                    String nc = rsmd.getColumnName(h);                                                
                                    System.out.println(nc);
                                                
                                    if( nc.equals("tipo_exa") || nc.equals("version_diccionario") || nc.equals("id") ||
                                        nc.equals("nombre") ){ 
                                        continue; 
                                    }else{ longitudes.add(rs.getInt(h)); } 
                                    
                               }
                                               
                               int tLongitudes = longitudes.size() - 1;
                               int longitud = longitudes.get(0);  
                               int noLineas = lineas.size() - 2;
                               String dato = "";
                               int posicionInicio = rs.getInt("posicion_inicio");
                               int h = 1;
                               int indice = 0;
                               
                               System.out.println( noColumnas + " " + noLineas + " " + posicionInicio );
                               
                               int k = 0;
                               while( k <= noLineas ){
                                   
                                      String line = lineas.get(k); 
                                      int tamañoLinea = line.length() - 1;
                                      System.out.println(line);
                                     
                                      int i = 0;
                                      int l = 0;
                                      while( i <= tamañoLinea ){                                                                                          
                                             
                                             if( i > posicionInicio ){
                                             
                                                 char c = line.charAt(i);                                                                                                                                                                                                                         
                                                 dato += c;                                                                                          
                                                 indice++;
                                                    
                                                 if( indice == longitud ){                                                                                                                                                                  
                                                     
                                                     indice = 0;
                                                     l += longitud;                                                     
                                                     longitud = longitudes.get(h);
                                                     h++;                                                               
                                                     dato = "";    
                                                     
                                                     System.out.println("tLongitudes = " + tLongitudes + " k = " + k + " i = " + i + " longitud = " + longitud + 
                                                                        "  Dato = " + dato  + " l = " + l + " TLinea = " + tamañoLinea );
                                                     
                                                     if( l >= (tamañoLinea - posicionInicio) ){ break; }
                                                           
                                                 }
                                                    
                                             } 
                                                            
                                             i++;
                                             
                                      }
                                                                             
                                      k++;                                     
                                    
                               }                                                              
                                                              
                          }
                          
                          rs.close();
                          s.close();
                          conexion.close();
                                                    
                     }catch(Exception e){}
                                            
                 }else{ context.addMessage(null, new FacesMessage("El archivo es invalido")); }                                
                 
                 selectOne.setRendered(true); 
                  
              }
                            
              
       }     
       
       public void ProcesarDat(){
            
              
             
       }
      
       public String getDiccionarioSeleccionado() { return diccionarioSeleccionado; }

       public void setDiccionarioSeleccionado(String diccionarioSeleccionado) {
              this.diccionarioSeleccionado = diccionarioSeleccionado;
       }
   
   
   
       public UISelectOne getSelectOne() {
              return selectOne;
       }

       public void setSelectOne(UISelectOne selectOne) {
              this.selectOne = selectOne;
       }
    
       public ArrayList<SelectItem> getDiccionarios() {
              return diccionarios;
       }

       public void setDiccionarios(ArrayList<SelectItem> diccionarios) {
              this.diccionarios = diccionarios;
       }
           
}
