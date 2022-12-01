package GUI.Cards;

import Enums.CardTypes;
import GUI.CellsTable;
import Managers.ErrorManager;
import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiddlePanelFour extends Card {
    private final CardTypes type = CardTypes.NO_DATA_TWO_VARIABLE;
    private static JTextField variableArea0;
    private static JTextField variableArea1;

    private JLabel variableLabel0;
    private JLabel variableLabel1;

    public MiddlePanelFour(){
        /*Create a JPanel with a grid bag layout*/
        this.setLayout(new GridBagLayout());

        /*Create the constraints for gridbag layout and apply them to the scroll pane.*/
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 1;
        this.add(dataArea(), c);
        c.gridy = 2;
        c.insets = new Insets(10, 0, 0, 0);
        this.add(topPanel2(), c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridy = 3;
        this.add(dataAreaTwo(), c);

        /*Change the y position value for the gridbag constraints and apply to the panel containing the
         * button and label.*/
        c.gridy = 0;
        this.add(topPanel(), c);
    }


    /**Creates a text area that user can input data into.
     *@return The text area*/
    private JTextField dataArea(){
        variableArea0 = new JTextField();
        variableArea0.setEditable(false);

        return(variableArea0);
    }

    private JTextField dataAreaTwo(){
        variableArea1 = new JTextField();
        variableArea1.setEditable(false);

        return(variableArea1);
    }

    /**Method which creates panel to contain the button and label.
     *@return The panel.*/
    private JPanel topPanel(){
        JPanel panel = new JPanel();

        panel.add(dataLabel());
        panel.add(importButton());
        return(panel);
    }

    private JPanel topPanel2(){
        JPanel panel = new JPanel();

        panel.add(dataLabel2());
        panel.add(importButton2());
        return(panel);
    }

    /**Method which creates the label.
     *@return The label*/
    private JLabel dataLabel(){
        return(variableLabel1 = new JLabel("Data"));
    }

    private JLabel dataLabel2(){
        return(variableLabel0 = new JLabel("Data"));
    }

    /**Method which creates the button for importing data from chart.
     *@return The import button,*/
    private JButton importButton(){
        JButton btnImport = new JButton("Import From Chart");

        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                variableArea0.setText(CellsTable.getSelectedData());
                updateMeasureData();
            }
        });

        return(btnImport);
    }

    private JButton importButton2(){
        JButton btnImport = new JButton("Import From Chart");

        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                variableArea1.setText(CellsTable.getSelectedData());
                updateMeasureDataTwo();
            }
        });

        return(btnImport);
    }

    private void updateMeasureData(){
        String[] data = variableArea0.getText().split(",");

        int s = RightPanel.getCurrentMeasureIndex();
        if(s < 0){
            return;
        }

        GUIDataMaster.getGUIMeasure(s).setVariable(variableLabel0.getText(), data[0]);
    }

    private void updateMeasureDataTwo(){
        String[] data = variableArea1.getText().split(",");

        int s = RightPanel.getCurrentMeasureIndex();
        if(s < 0){
            return;
        }

        GUIDataMaster.getGUIMeasure(s).setVariable(variableLabel1.getText(), data[0]);
    }

    @Override
    public CardTypes getType() {
        return type;
    }

    @Override
    public void setDataArea(int index, String data) {
        ErrorManager.sendErrorMessage("GUI", "Program attempted to set data for a data field which does not exist");
    }

    @Override
    public void setVariableArea(int index, String data) {
        switch (index) {
            case 0 -> variableArea0.setText(data);
            case 1 -> variableArea1.setText(data);
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set data for a data field which does not exist");
        }
    }

    @Override
    public void setDataLabel(int index, String label) {
        ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
    }

    @Override
    public void setVariableLabel(int index, String label) {
        switch (index) {
            case 0 -> variableLabel0.setText(label);
            case 1 -> variableLabel1.setText(label);
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
        }
    }
}
