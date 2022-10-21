package GUI;
import javax.swing.*;
import java.awt.*;

public class RightPanel {
    /**Method which returns the panel which will be on the right side of the frame.
     *@return The panel which contains a scroll pane and button.*/
    public JPanel rightPanel(){
        /*Create a panel with a border layout*/
        JPanel panel = new JPanel(new BorderLayout());

        /*Add the scroll bane and buttons panel to the right panel.*/
        panel.add(scrollPane(), BorderLayout.CENTER);
        panel.add(buttons(), BorderLayout.NORTH);
        return(panel);
    }

    /**Method which creates the scroll pane.
     *@return The scroll pane.*/
    private JScrollPane scrollPane() {
        return (new JScrollPane(functionsBox()));
    }

    /**Method which creates the uneditable text area which will contain the names of the user's selected
     * equations.
     *@return An uneditable text area.*/
    private JTextArea functionsBox(){
        JTextArea functionsList = new JTextArea("Hello", 30, 20);
        functionsList.setEditable(false);
        return(functionsList);
    }

    /**Method which creates the panel containing both buttons.
     *@return The panel.*/
    private JPanel buttons(){
        JPanel panel = new JPanel();
        panel.add(addButton());
        panel.add(label());
        panel.add(removeButton());

        return(panel);
    }

    /**Method which creates a label for the "|" character
     *@return The jlabel.*/
    private JLabel label(){
        JLabel label = new JLabel("|");

        return(label);
    }

    /**Method which creates the add button.
     *@return The add button.*/
    private JButton addButton() {
        JButton add = new JButton("Add");
        add.setOpaque(false);
        add.setContentAreaFilled(false);
        add.setBorderPainted(false);

        return (add);
    }

    /**Method which creates the remove button.
     *@return The remove button.*/
    private JButton removeButton() {
        JButton remove = new JButton("Remove");
        remove.setOpaque(false);
        remove.setContentAreaFilled(false);
        remove.setBorderPainted(false);

        return (remove);
    }
}
