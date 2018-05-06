package sales;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.sun.javafx.runtime.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sales.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class Main {

    private static Logger logger;

    public static void main(String[] args) {
        initialize();

        logger.info("Application has been started");
        MainFrame.getInstance().setVisible(true);
        logger.info("Showing MainFrame");

        logger.info("Checking connection to database");
        while(Utils.getSessionFactory() == null){

            logger.warn("There is no connection to database, showing user error");
            int result = JOptionPane.showConfirmDialog(MainFrame.getInstance(),Utils.getResourceBundle().getString("ThereIsNoConnectionToDatabase"),
                    Utils.getResourceBundle().getString("NoDatabaseConnection"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

            if(result == JOptionPane.YES_OPTION){
                logger.info("Showing user database settings dialog");
            }else{
                logger.info("User doesn't want to edit database settings, exiting application");
                System.exit(0);
            }

        }
    }

    private static void initialize(){
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("res/sales.properties"));
        } catch (IOException e) {
            logger.error("Could not read application properties file");
        }
        logger = LoggerFactory.getLogger(Main.class);

        logger.debug("Setting default locale");
        Locale locale = new Locale(props.getProperty("application.locale.country"),props.getProperty("application.locale.region"));
        Locale.setDefault(locale);

        UIManager.put("OptionPane.yesButtonText", Utils.getResourceBundle().getString("Yes"));
        UIManager.put("OptionPane.noButtonText", Utils.getResourceBundle().getString("No"));
        UIManager.put("OptionPane.okButtonText", Utils.getResourceBundle().getString("Ok"));
        UIManager.put("OptionPane.cancelButtonText", Utils.getResourceBundle().getString("Cancel"));

        logger.debug("Setting application look & feel");
        if(props.getProperty("application.laf").equals("default")){
            try {
                if(System.getProperty("os.name").contains("Linux"))
                    UIManager.setLookAndFeel(new GTKLookAndFeel());
                else
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
                logger.error("Couldn't set application look & feel", e);
            }
        }else{
            try {
                UIManager.setLookAndFeel(props.getProperty("application.laf"));
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                logger.error("Couldn't set application look & feel", e);
            }
        }


    }
}
