package GUI.Panels;

import Enums.CardTypes;
import GUI.Card;
import GUI.CellsTable;
import Managers.ErrorManager;
import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class MiddlePanel extends Card {
    private final CardTypes type = CardTypes.ONE_DATA_NO_VARIABLE;
    private static JTextArea dataArea0;

    private JLabel dataLabel0;

    public MiddlePanel(){
        /*Create a JPanel with a grid bag layout*/
        this.setLayout(new GridBagLayout());

        /*Create the constraints for gridbag layout and apply them to the scroll pane.*/
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 1;
        this.add(scrollPane(), c);

        /*Change the y position value for the gridbag constraints and apply to the panel containing the
         * button and label.*/
        c.gridy = 0;
        this.add(topPanel(), c);
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
        dataArea0 = new JTextArea("Select Data from Chart", 20, 15);
        dataArea0.setEditable(false);
        dataArea0.setLineWrap(true);

        dataArea0.getDocument().addDocumentListener(new DocumentListener() {
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

        return(dataArea0);
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
                String d = CellsTable.getSelectedData();

                if(d == null){
                    return;
                }

                //If Shift is held when clicking
                if((e.getModifiers() & 1) != 0){
                    dataArea0.append("," + d);
                }else{
                    dataArea0.setText(d);
                }

                updateMeasureData();
            }
        });

        return(btnImport);
    }

    private void updateMeasureData(){
        String[] data = dataArea0.getText().split(",");

        int s = RightPanel.getCurrentMeasureIndex();
        if(s < 0){
            return;
        }

        GUIDataMaster.getGUIMeasure(s).addData(false, 0, data);
    }

    @Override
    public CardTypes getType() {
        return type;
    }

    @Override
    public void setDataArea(int index, String data) {
        switch (index) {
            case 0 -> dataArea0.setText(data);
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set data for a data field which does not exist");
        }
    }

    @Override
    public void setVariableArea(int index, String data) {
        ErrorManager.sendErrorMessage("GUI", "Program attempted to set data for a data field which does not exist");
    }

    @Override
    public void setDataLabel(int index, String label) {
        switch (index) {
            case 0 -> dataLabel0.setText(label);
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
        }
    }

    @Override
    public void setVariableLabel(int index, String label) {
        ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
    }
}
