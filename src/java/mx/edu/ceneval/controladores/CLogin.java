
package mx.edu.ceneval.controladores;

// @author Daniel.Meza

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import mx.edu.ceneval.extras.HibernateHelper;
import org.hibernate.Session;
 
public class CLogin {
    
       private HibernateHelper hh;
       private String nombre;
       private FacesContext context = FacesContext.getCurrentInstance();
       
       public String compararDatosLogin(){ 
               
              try{
                  
                  hh = new HibernateHelper();
                  Session s = hh.getSesion();
                  s.beginTransaction();
                  
                  System.out.println("from Usuarios where nombre = '" + nombre + "'");
                  List nombres = s.createQuery("from Usuarios where nombre = '" + nombre + "'").list();                  
                  
                  s.getTransaction().commit();
                                    
                  if( nombres.size() - 1 != -1 ){ 
                      context.addMessage("", new FacesMessage("Bienvenido " + nombre));
                      context.getExternalContext().getFlash().setKeepMessages(true);
                      return "login";  
                  }
                  else{ 
                       context.addMessage("", new FacesMessage("El nombre de usuario no existe o esta mal escrito"));                        
                  }
                                    
                  s.close();
                                    
              }catch(Exception e ){ e.printStackTrace(); }             
              
              return "";
             
       }
       
       public String desloguearse(){    
              context.getExternalContext().invalidateSession();
              return "/index.xhtml?faces-redirect=true";              
       }
    
       public String getNombre() {
              return nombre;
       }

       public void setNombre(String nombre) {
              this.nombre = nombre;
       }
    
}
