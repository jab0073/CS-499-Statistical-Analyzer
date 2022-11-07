package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame extends JFrame {
    /**Method for generating the frame which holds the GUI*/
    public void frame() {
        /*Create a frame, give it a size, set it to exit on close.*/
        JFrame window = new JFrame("Analysis");


        AltMenuBar amb = new AltMenuBar();
        window.setJMenuBar(amb.getMenuBar());
        window.setSize(1000, 750);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*Create a layout for the frame and add in the panels in their appropriate positions.*/
        window.setLayout(new BorderLayout());
        window.add(windowPanelTop(), BorderLayout.NORTH);
        window.add(windowPanelLeft(), BorderLayout.WEST);
        window.add(windowPanelMiddle(), BorderLayout.CENTER);
        window.add(windowPanelRight(), BorderLayout.EAST);

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)
            {
                // TODO: Add dialog box to prompt if user wants to save or export before closing

                // TODO: Save Table contents as DataSet or export to preferred file format

                Frame.closeDialogs();
                e.getWindow().dispose();
            }
        });

        /*Set the frame to start maximized and visible.*/
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
    }

    /**Method which adds the menu bar panel to a panel which will be placed at the top of the application.
     *@return The panel containing the menu bar panel.*/
    private JPanel windowPanelTop(){
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new MenuBar().topPanel(), BorderLayout.PAGE_START);

        return(panel);
    }

    /**Method which adds the cells table panel to a panel which will be placed to the left of the application.
     *@return The panel containing the cells table panel.*/
    private JPanel windowPanelLeft(){
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new CellsTable().cellsPanel(), BorderLayout.LINE_START);

        return(panel);
    }

    /**Method which adds the middle panel to a panel which will be placed in the middle of the application.
     *@return The panel containing the middle panel.*/
    private JPanel windowPanelMiddle(){
        JPanel panel = new JPanel(new CardLayout());

        panel.add(new MiddlePanel().dataPanel());

        return(panel);
    }

    /**Method which adds the right panel to a panel which will be placed to the right of the application.
     *@return The panel containing the right panel.*/
    private JPanel windowPanelRight(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new RightPanel().rightPanel(), BorderLayout.CENTER);
        return(panel);
    }

    public static void closeDialogs(){
        Window[] children = Frame.getWindows();
        for (Window win : children){
            if (win instanceof JDialog){
                win.dispose();
            }
        }
    }
}