package mx.edu.ceneval.extras;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

// @author Daniel.Meza
 
public class HibernateHelper {
    
       Configuration configuration;
       ServiceRegistry sr;
       SessionFactory sf;
       private Session sesion;
    
       public HibernateHelper(){
              
              configuration = new Configuration();
              configuration.configure();
              sr = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
              sf = new Configuration().configure().buildSessionFactory(sr);
              sesion = sf.openSession();
             
       }
    
       public Session getSesion() {
              return sesion;
       }

}
