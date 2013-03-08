
package mx.edu.ceneval.controladores;

// @author Daniel.Meza

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import mx.edu.ceneval.extras.Conexion;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
 
public class CDat implements Serializable{
    
       private UploadedFile archivo;          
       private FacesContext context = FacesContext.getCurrentInstance();
       private Connection conexion;
       private Conexion cc;
       private ArrayList<SelectItem> diccionarios;      
       private UISelectOne selectOne;
       private CommandButton cbProcesarArchivo;
       private String nombreArchivo;             
       
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
                                 diccionarios.add(new SelectItem(dic,dic,"Catch"));                                 
                             }
                             
                      }
                  }                                    
                  
              }catch(Exception e){ e.printStackTrace(); }                                                                                                                              
              
       }
       
       public void generArchivo(FileUploadEvent fue){
           
              UploadedFile file = archivo;
              nombreArchivo =  file.getFileName().trim();
              
              context.addMessage(null, new FacesMessage("El archivo : " + nombreArchivo));
              
              try{
                  
                  InputStream is = file.getInputstream();
                  OutputStream salida = new FileOutputStream(new File("C:\\temp\\" + nombreArchivo));
                  
                  int c = 0;
                  byte[] bytes = new byte[1024];
                  while((c = is.read(bytes)) != -1 ){
                         salida.write(bytes,0,c);
                  }
                  
                  is.close();
                  salida.flush();
                  salida.close();
                  
              }catch(Exception e){ e.printStackTrace(); }
              
       }
       
       public UploadedFile getArchivo() { return archivo; }

       public void setArchivo(UploadedFile up) { this.archivo = up; }                           
       
       public void subir(ActionEvent ae){                                                                               
                           
              UploadedFile file = archivo;
              String na = file.getFileName().trim();
              
              Map mapaSesion = context.getExternalContext().getSessionMap();
              mapaSesion.put("na",na);
              
              context.addMessage(null, new FacesMessage("El archivo : " + na));
              
              try{
                  
                  InputStream is = file.getInputstream();
                  OutputStream salida = new FileOutputStream(new File("C:\\temp\\" + na));
                  
                  int c = 0;
                  byte[] bytes = new byte[1024];
                  while((c = is.read(bytes)) != -1 ){
                         salida.write(bytes,0,c);
                  }
                  
                  is.close();
                  salida.flush();
                  salida.close();
                  
              }catch(Exception e){ e.printStackTrace(); }
              
              selectOne.setRendered(true);
              cbProcesarArchivo.setRendered(true);
              
       }                                        
    
       public ArrayList<SelectItem> getDiccionarios() {
              return diccionarios;
       }

       public void setDiccionarios(ArrayList<SelectItem> diccionarios) {
              this.diccionarios = diccionarios;
       }

       public CommandButton getCbProcesarArchivo() {
              return cbProcesarArchivo;
       }
   
       public void setCbProcesarArchivo(CommandButton cbProcesarArchivo) {
              this.cbProcesarArchivo = cbProcesarArchivo;
       }
   
       public UISelectOne getSelectOne() {
              return selectOne;
       }
   
       public void setSelectOne(UISelectOne selectOne) {
              this.selectOne = selectOne;
       }
    
       public String getNombreArchivo() {
              return nombreArchivo;
       }

       public void setNombreArchivo(String nombreArchivo) {
              this.nombreArchivo = nombreArchivo;
       }
           
}
