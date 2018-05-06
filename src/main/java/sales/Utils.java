package sales;

import sales.model.users.Groups;
import sales.model.users.Permissions;
import sales.model.users.Users;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Utils {

    private static Logger logger;
    private static SessionFactory sessionFactory;

    /**
     * Default getter for application wide language
     * @return ResourceBundle of the default language
     */
    public static ResourceBundle getResourceBundle(){
        return ResourceBundle.getBundle("lang", Locale.getDefault(), new UTF8Control());

    }

    /**
     * Default hibernate session factory
     * @return Valid SessionFactory if successful or null if not
     */
    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            try {
                Configuration configuration = new Configuration();

                //Create Properties, can be read from property files too
                Properties props = new Properties();
                props.load(new FileInputStream(new File("res/sales.properties")));
                props.put("hibernate.connection.driver_class", props.getProperty("database.driver_class"));
                props.put("hibernate.connection.url", props.getProperty("database.url"));
                props.put("hibernate.connection.username", props.getProperty("database.username"));
                props.put("hibernate.connection.password", props.getProperty("database.password"));
                props.put("hibernate.current_session_context_class", "thread");
                props.put("hibernate.dialect", props.getProperty("database.dialect"));
                props.put("hibernate.connection.pool_size", props.getProperty("database.pool_size"));

                configuration.setProperties(props);

                //we can set mapping file or class with annotation
                //addClass(Employee1.class) will look for resource
                configuration.addAnnotatedClass(Users.class);
                configuration.addAnnotatedClass(Groups.class);
                configuration.addAnnotatedClass(Permissions.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                logger.info("Hibernate Java Config serviceRegistry created");

               sessionFactory = configuration.buildSessionFactory(serviceRegistry);

                return sessionFactory;
            }
            catch (Exception ex) {
                logger.error("Initial Hibernate SessionFactory creation failed.",ex);
                return null;
            }
        }else
            return sessionFactory;
    }


    static{
        logger = LoggerFactory.getLogger(Utils.class);
    }
}
