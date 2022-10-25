package GUI;

import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiddlePanel {
    private static JTextArea dataArea;

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
        JScrollPane pane = new JScrollPane(dataArea());
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return (pane);
    }

    /**Creates a text area that user can input data into.
     *@return The text area*/
    private JTextArea dataArea(){
        dataArea = new JTextArea("Select Data from Chart", 20, 15);
        dataArea.setEditable(false);
        dataArea.setLineWrap(true);

        dataArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateMeasureData();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateMeasureData();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        return(dataArea);
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
        JButton btnImport = new JButton("Import From Chart");

        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dataArea.getText().contains("Select Data from Chart")){
                    dataArea.setText(CellsTable.getSelectedData());
                }
                else{
                    dataArea.append(CellsTable.getSelectedData() + ",");
                }
                updateMeasureData();
            }
        });

        return(btnImport);
    }

    private void updateMeasureData(){
        String[] data = dataArea.getText().split(",");

        int s = RightPanel.getCurrentMeasureIndex();
        if(s < 0){
            return;
        }

        GUIDataMaster.getGUIMeasure(s).addData(false, 0, data);
    }

    public static void changeData(String data){
        dataArea.setText(data);
    }
}
