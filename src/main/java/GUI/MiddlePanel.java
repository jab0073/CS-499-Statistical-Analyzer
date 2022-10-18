package GUI;

import javax.swing.*;
import java.awt.*;

public class MiddlePanel {
    /**Method which returns a panel which contains one scroll pane, a button, and a label.
     *@return One of the data panel options.*/
    public JPanel dataPanel(){
        /*Create a JPanel with a grid bag layout*/
        JPanel panel = new JPanel(new GridBagLayout());

        /*Create the constraints for gridbag layout and apply them to the scroll pane.*/
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 1;
        panel.add(scrollPane(), c);

        /*Change the y position value for the gridbag constraints and apply to the panel containing the
        * button and label.*/
        c.gridy = 0;
        panel.add(topPanel(), c);
        return(panel);
    }

    /**Creates a scroll pane for the text area.
     *@return A scroll pane containing a text area.*/
    private JScrollPane scrollPane() {
        return (new JScrollPane(dataArea()));
    }

    /**Creates a text area that user can input data into.
     *@return The text area*/
    private JTextArea dataArea(){
        return(new JTextArea("Data", 20, 15));
    }

    /**Method which creates panel to contain the button and label.
     *@return The panel.*/
    private JPanel topPanel(){
        JPanel panel = new JPanel();

        panel.add(dataLabel());
        panel.add(importButton());
        return(panel);
    }

    /**Method which creates the label.
     *@return The label*/
    private JLabel dataLabel(){
        return(new JLabel("Data"));
    }

    /**Method which creates the button for importing data from chart.
     *@return The import button,*/
    private JButton importButton(){
        return(new JButton("Import From Chart"));
    }
}
