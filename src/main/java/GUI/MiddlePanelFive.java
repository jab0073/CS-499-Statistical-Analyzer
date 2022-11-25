package GUI;

import FrontEndUtilities.ErrorManager;
import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiddlePanelFive extends Card{
    private static final CardTypes type = CardTypes.ONE_DATA_ONE_VARIABLE;
    private JTextArea dataArea0;
    private JTextField variableArea0;

    private JLabel dataLabel0;
    private JLabel variableLabel0;

    public MiddlePanelFive(){
        /*Create a JPanel with a grid bag layout*/
        this.setLayout(new GridBagLayout());

        /*Create the constraints for gridbag layout and apply them to the scroll pane.*/
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 1;
        this.add(scrollPane(), c);
        c.gridy = 2;
        this.add(topPanel2(), c);
        c.gridy = 3;
        this.add(variableArea(), c);

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

    private JTextField variableArea(){
        variableArea0 = new JTextField();
        variableArea0.setEditable(false);
        variableArea0.setBackground(Color.WHITE);

        variableArea0.getDocument().addDocumentListener(new DocumentListener() {
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

        return(variableArea0);
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
        return(dataLabel0 = new JLabel("Data"));
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
                dataArea0.setText(CellsTable.getSelectedData());
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
                variableArea0.setText(CellsTable.getSelectedData());
                updateMeasureDataTwo();
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

    private void updateMeasureDataTwo(){
        String[] data = variableArea0.getText().split(",");

        int s = RightPanel.getCurrentMeasureIndex();
        if(s < 0){
            return;
        }

        GUIDataMaster.getGUIMeasure(s).setVariable(variableLabel0.getText(), data[0]);
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
        switch (index) {
            case 0 -> variableArea0.setText(data);
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set data for a data field which does not exist");
        }
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
        switch (index) {
            case 0 -> variableLabel0.setText(label);
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
        }
    }
}
