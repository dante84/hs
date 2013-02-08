
package mx.edu.ceneval.controladores;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import mx.edu.ceneval.mapeos.Usuarios;

// @author Daniel.Meza

public class CPrueba {
     
       FacesContext context = FacesContext.getCurrentInstance();
       SessionFactory sf;
       
       public void test(){
           
              try{
                  
                  Configuration configuration = new Configuration();
                  configuration.configure();
                  ServiceRegistry sr = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
                  sf = new Configuration().configure().buildSessionFactory(sr);
                  Session s = sf.openSession();
                  s.beginTransaction();
                  List resultado = s.createQuery("from Usuarios").list();
                  
                  for(Usuarios usuario : (List<Usuarios>)resultado ){
                      context.addMessage(null,new FacesMessage(usuario.getNombre()));                      
                  }
                  
                  s.getTransaction().commit();
                  s.close();
                  
              }catch(Exception e){ e.printStackTrace(); }
                 
       }
      
}
