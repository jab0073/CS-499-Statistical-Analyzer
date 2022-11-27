package GUI;

import FrontEndUtilities.ErrorManager;
import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiddlePanelTwo extends Card{
    private final CardTypes type = CardTypes.TWO_DATA_NO_VARIABLE;

    private static JTextArea dataArea0;
    private static JTextArea dataArea1;

    private JLabel dataLabel0;
    private JLabel dataLabel1;

    public MiddlePanelTwo(){
        this.setLayout(new GridBagLayout());

        /*Create the constraints for gridbag layout and apply them to the scroll pane.*/
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 1;
        this.add(middlePanel(), c);
        c.gridy = 2;
        this.add(topPanel2(), c);
        c.gridy = 3;
        this.add(middlePanel2(), c);

        /*Change the y position value for the gridbag constraints and apply to the panel containing the
         * button and label.*/
        c.gridy = 0;
        this.add(topPanel(), c);
    }

    /**Creates a scroll pane for the text area.
     *@return A scroll pane containing a text area.*/
    private JScrollPane middlePanel() {
        JScrollPane pane = new JScrollPane(dataArea());
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return (pane);
    }

    private JScrollPane middlePanel2() {
        JScrollPane pane = new JScrollPane(dataArea2());
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return (pane);
    }
    /**Creates a text area that user can input data into.
     *@return The text area*/
    private JTextArea dataArea(){
        dataArea0 = new JTextArea("Select Data from Chart", 10, 20);
        dataArea0.setEditable(false);
        dataArea0.setLineWrap(true);

        return(dataArea0);
    }

    private JTextArea dataArea2(){
        dataArea1 = new JTextArea("Select Data from Chart", 10, 20);
        dataArea1.setEditable(false);
        dataArea1.setLineWrap(true);

        return(dataArea1);
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
        return(new JLabel("Data"));
    }

    private JLabel dataLabel2(){
        return(new JLabel("Data"));
    }

    /**Method which creates the button for importing data from chart.
     *@return The import button,*/
    private JButton importButton2(){
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
                    dataArea1.append("," + d);
                }else{
                    dataArea1.setText(d);
                }
                updateMeasureData();
            }
        });

        return(btnImport);
    }

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

                updateMeasureData2();
            }
        });

        return(btnImport);
    }

    private void updateMeasureData(){
        String[] data = dataArea1.getText().split(",");

        int s = RightPanel.getCurrentMeasureIndex();
        if(s < 0){
            return;
        }

        GUIDataMaster.getGUIMeasure(s).addData(false, 1, data);
    }

    private void updateMeasureData2(){
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
            case 1 -> dataArea1.setText(data);
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
            case 1 -> dataLabel1.setText(label);
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
        }
    }

    @Override
    public void setVariableLabel(int index, String label) {
        ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
    }
}
