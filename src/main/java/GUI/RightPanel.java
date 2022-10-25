package GUI;
import BackEndUtilities.MeasureManager;
import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class RightPanel {
    private static JList<String> functionList;
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
    private JList functionsBox(){
        functionList = new JList<>();

        functionList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    int a = functionList.getSelectedIndex();
                    if(a==-1){
                        return;
                    }
                    MiddlePanel.changeData(GUIDataMaster.getGUIMeasure(functionList.getSelectedIndex()).getDataAsString());
                }
            }
        });

        return(functionList);
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
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrontEndUtilities.GUIDataMaster.newGUIMeasure(MeasureManager.getRandomMeasureName());
                updateList(true);
            }
        });

        return (add);
    }

    /**Method which creates the remove button.
     *@return The remove button.*/
    private JButton removeButton() {
        JButton remove = new JButton("Remove");
        remove.setOpaque(false);
        remove.setContentAreaFilled(false);
        remove.setBorderPainted(false);
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrontEndUtilities.GUIDataMaster.removeGUIMeasure(functionList.getSelectedIndex());
                updateList(false);
            }
        });

        return (remove);
    }

    private void updateList(boolean add){
        int i = functionList.getSelectedIndex();

        String[] names = FrontEndUtilities.GUIDataMaster.getMeasureNames();

        functionList.setListData(new Vector<String>(Arrays.asList(names)));

        if(add){
            functionList.setSelectedIndex(names.length-1);
        }else{
            if(i > names.length-1) {
                functionList.setSelectedIndex(i - 1);
            }else{
                functionList.setSelectedIndex(i);
            }
        }
    }

    public static int getCurrentMeasureIndex(){
        return functionList.getSelectedIndex();
    }
}
