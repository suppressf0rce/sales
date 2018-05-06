package sales.view;

import sales.Utils;

import javax.swing.*;

/**
 * This class represents main window of the application
 */
public class MainFrame extends JFrame {
    private static MainFrame ourInstance = new MainFrame();

    public static MainFrame getInstance() {
        return ourInstance;
    }


    /**
     * Main Menu of the application
     */
    private MainMenu mainMenu = new MainMenu();

    private MainFrame() {

        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("icon.png"));
        setIconImage(icon.getImage());
        setTitle(Utils.getResourceBundle().getString("Sales"));

        setJMenuBar(mainMenu);

        setExtendedState(MAXIMIZED_BOTH);
    }
}
