package GUI;
import BackEndUtilities.MeasureManager;
import FrontEndUtilities.GUIDataMaster;
import FrontEndUtilities.GUIMeasure;
import Graphing.GraphTypes;
import Interfaces.IMeasure;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class RightPanel extends JPanel{
    private static JList<String> functionList;
    private static List<GraphTypes> graphsList;
    private static boolean selected = false;
    private static int functionIndex;
    /**Method which returns the panel which will be on the right side of the frame.
     *@return The panel which contains a scroll pane and button.*/
    public RightPanel(){
        /*Create a panel with a border layout*/
        this.setLayout(new BorderLayout());

        /*Add the scroll bane and buttons panel to the right panel.*/
        this.add(scrollPane(), BorderLayout.CENTER);
        this.add(topRightPanel(), BorderLayout.NORTH);
    }

    private JPanel topRightPanel(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttons(), BorderLayout.NORTH);
        panel.add(dropDown(), BorderLayout.SOUTH);
        return(panel);
    }


    /**Method which creates the scroll pane.
     *@return The scroll pane.*/
    private JScrollPane scrollPane() {
        JScrollPane pane = new JScrollPane(functionsBox());
        pane.setPreferredSize(new Dimension(250, 250));
        return (pane);
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
                    selected = true;
                    int a = functionList.getSelectedIndex();
                    if(a==-1){
                        return;
                    }
                    GUIMeasure m = GUIDataMaster.getGUIMeasure(functionList.getSelectedIndex());
                    GUIDataMaster.swapMiddleCard(m.getCardType(), m);
                    IMeasure i = MeasureManager.getMeasure(m.getName());
                    setGraphsList(i.getValidGraphs());
                    GraphsComboBox.getModel().removeAllElements();
                    GraphsComboBox.setModel();
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

    private JComboBox dropDown(){
        String[] names = MeasureManager.getAllMeasureNames().toArray(new String[0]);
        JComboBox box = new JComboBox(names);
        box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                functionIndex = box.getSelectedIndex();
            }
        });
        return(box);
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
                FrontEndUtilities.GUIDataMaster.newGUIMeasure(dropDown().getItemAt(functionIndex).toString());
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

        if(functionList.getModel().getSize() < 1){
            GUIDataMaster.swapMiddleCard(CardTypes.BLANK, null);

            GraphsComboBox.hide();
        }
    }

    private void setGraphsList(List<GraphTypes> list){
        graphsList = list;
    }

    public static String[] getGraphsListStr(){
        String[] arr = new String[graphsList.size()];
        int i = 0;
        for (GraphTypes value : graphsList) {
            arr[i] = value.getName();
            i++;
        }
        return(arr);
    }

    public static List<GraphTypes> getGraphsList(){
        return(graphsList);
    }


    public static int getCurrentMeasureIndex(){
        return functionList.getSelectedIndex();
    }

    public static boolean getSelected(){return selected;}

    public void updateForLoad(){
        updateList(true);
    }
}
