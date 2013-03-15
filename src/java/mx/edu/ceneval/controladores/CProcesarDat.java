
package mx.edu.ceneval.controladores;

// @author Daniel.Meza

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import mx.edu.ceneval.extras.Conexion;
 
@ManagedBean
@RequestScoped
public class CProcesarDat implements Serializable{
    
       private String diccionarioSeleccionado;     
       private FacesContext context = FacesContext.getCurrentInstance();
       private Connection conexion;
       private Conexion cc;
            
       public void procesarDat(ActionEvent ae){                          
                                                 
              String nombreArchivo = (String)context.getExternalContext().getSessionMap().get("na");
              context.addMessage(null, new FacesMessage("En procesar dat y el archivo es = " + nombreArchivo));                            
                                              
              ArrayList<ArrayList<String>> datos = new ArrayList<ArrayList<String>>();                  
                  
              File archivo = new File("C:\\temp\\" + nombreArchivo );
              int clave = Integer.parseInt( nombreArchivo.substring(6,10) ); 
                  
              System.out.println("Nombre archivo " + nombreArchivo + " Clave = " + clave );
                  
              if( nombreArchivo.endsWith(".dat") ){                                            
                      
                  try{
                           
                      ArrayList<String> lineas = new ArrayList<String>();
                      String linea = "";
                      FileInputStream is = new FileInputStream(archivo);
                         
                      Map<String,String> valores = new HashMap<String,String>();  
                          
                      BufferedReader br = new BufferedReader(new InputStreamReader(is));
                          
                      while( ( linea = br.readLine() ) != null ){                                 
                               lineas.add(linea);                                                                                                   
                      }                                                                                                       
                                                                                                                             
                      cc = new Conexion();
                      conexion = cc.getC();
                      Statement s = conexion.createStatement();
                      
                      String select = "";
                      
                      if( nombreArchivo.startsWith("R") || nombreArchivo.startsWith("r") ){
//                          select += "select posicion_inicio,apli,fecha_apli,opc_apli,ano_ver,tipo_reg,tipo_resp,cve_bpm,apli,fecha_apli,cve_inst,identifica," +
//                                    "folio,matricula,ape_pat,ape_mat,longitud_campos.nombre,dia_nac,mes_nac,ano_nac,sexo,li_mad,li_pad,edo_proc,nom_proc," +
//                                    "ciu_proc,cve_proc,reg_proc,mod_lic,prom_lic,bec_sdac,bec_sne,bec_shd,hrs_trab,est_alca,des_idea,des_desa,des_aten,des_esme," +
//                                    "des_meta,des_dist,des_term,des_duro,hab_eime,cua_pesc,conteni,redaccio,organiza,ortogra,presenta,hab_arg,hab_rep,hab_his," +
//                                    "hab_car,fre_aide,fre_ordn,fre_pala,fre_revi,pre_exa1,pre_exa2,pre_exa3,viv_mad,viv_pad,viv_par,viv_otro,tie_hij," +
//                                    "esco_mad,esco_pad,esco_par,cuan_lib,ser_tele,ser_lav,ser_ref,ser_hor,ser_inte,ser_cabl,ser_tabl,bie_dvd,bien_pc,bie_tv,bie_auto," +
//                                    "ser_bano,vac_rm,edo_rep,vac_ext from longitud_campos left outer join claves_examen on longitud_campos.clave_instrumento = " +
//                                    "claves_examen.clave_instrumento where claves_examen.clave = '" + clave + "' and " + 
//                                    "longitud_campos.version_diccionario = '" + diccionarioSeleccionado + "'";                                                                                                                                                       
                            select += "call evaluarDat(clave,)";
                      }
                      
                      if( nombreArchivo.startsWith("S") || nombreArchivo.startsWith("s") ){
//                          select += "select posicion_inicio,apli,fecha_apli,cve_inst,identifica,per_mcon,folio,perfil,version,cve_con,hora_inis,min_inis,respuesta," +
//                                    "hora_fins,min_fins,res_extra from longitud_campos left outer join claves_examen on longitud_campos.clave_instrumento = " +
//                                    "claves_examen.clave_instrumento where claves_examen.clave = '" + clave + "' and " + 
//                                    "longitud_campos.version_diccionario = '" + diccionarioSeleccionado + "'";                                                                                                                                                       
                      }
                      
                      System.out.println(select);
                      
                      ResultSet rs = s.executeQuery(select);                                                    
                      
                      if( !rs.isBeforeFirst() ){ context.addMessage(null, new FacesMessage("La clave de examen no existe. Verifica")); }
                      else{                                                             
                               
                           ResultSetMetaData rsmd = rs.getMetaData();                                                    
                           rs.next();
                               
                           int noColumnas = rsmd.getColumnCount();
                           ArrayList<Integer> longitudes = new ArrayList<Integer>();
                                                                                             
                           for( int h = 2; h <= noColumnas; h++ ){
                                   
                                String nc = rsmd.getColumnName(h);                                                
                                int length = rs.getInt(h);
                                System.out.println(length + "      " + nc);                                                
                                longitudes.add( length ); 
                                    
                           }
                                               
                           int tLongitudes = longitudes.size();                           
                           int noLineas = lineas.size() - 2;
                           String dato = "";
                           int posicionInicio = rs.getInt("posicion_inicio");                           
                           int indice = 1;
                               
                           System.out.println("Columnas = " + noColumnas + " lineas = " + noLineas + " posicion inicio = " + posicionInicio );
                               
                           int h = 0;
                           int k = 0;
                           while( k <= noLineas ){
                                   
                                  String line = lineas.get(k); 
                                  int tamañoLinea = line.length();
                                  System.out.println("ll = " + tamañoLinea + "   " + line);
                                                                       
                                  int longitud = longitudes.get(h);
                                  int i = -1;                                  
                                  boolean flag = true;
                                  
                                  while( flag ){                                                                                                                                                                                                                                                             
                                      
                                         i++;
                                         
                                         if( i > tamañoLinea ){ 
                                             flag = false;
                                             continue;
                                         }                                                                                                                                                                                              
                                         
                                         if( i >= posicionInicio ){
                                                                                          
                                             char c = line.charAt(i - 1);                                                                                                                                                                                                                                                                      
                                             
                                             if( indice <= longitud ){                                                                                                  
                                                 dato += c;                                                                                                                                                                             
                                                 indice++;                                                 
                                             }
                                             
                                             if( indice > longitud ){
                                                                                                 
                                                 h++;                                                   
                                                 
                                                 System.out.println( h + " " + longitud + " " + dato );                                                 
                                                 
                                                 dato = "";                                                                                                          
                                                 indice = 1;                                                 
                                                                                                                                                   
                                                 if( h >= tLongitudes ){
                                                     h = 0;
                                                     flag = false;
                                                 }
                                                 
                                                 longitud = longitudes.get(h);                                                     
                                                                                                                                                                                                                                                                                                                                                                                                              
                                             }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
                                             
                                         }                                                                                                                                                                                                                                      
                                         
                                  }                                  
                                                                             
                                  k++;                                     
                                    
                          }                                                              
                                                              
                   }
                          
                   rs.close();
                   s.close();
                   conexion.close();
                                                    
              }catch(Exception e){ e.printStackTrace(); }
                                            
           }else{ context.addMessage(null, new FacesMessage("El archivo es invalido")); }                                                                  
              
           context.addMessage(null, new FacesMessage("Archivaldo nombre sesionado = " + nombreArchivo));                                                                                        
             
       }

       public String getDiccionarioSeleccionado() {
              return diccionarioSeleccionado;
       }

       public void setDiccionarioSeleccionado(String diccionarioSeleccionado) {
              this.diccionarioSeleccionado = diccionarioSeleccionado;
       }
    
}
