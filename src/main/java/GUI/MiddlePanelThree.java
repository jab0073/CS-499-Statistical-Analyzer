package GUI;

import FrontEndUtilities.ErrorManager;
import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiddlePanelThree extends Card{
    private final CardTypes type = CardTypes.NO_DATA_ONE_VARIABLE;

    private static JTextField variableArea0;

    private JLabel variableLabel0;

    public MiddlePanelThree(){
        this.setLayout(new GridBagLayout());

        /*Create the constraints for gridbag layout and apply them to the scroll pane.*/
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx = 2;
        c.gridy = 1;
        this.add(dataArea(), c);

        /*Change the y position value for the gridbag constraints and apply to the panel containing the
         * button and label.*/
        c.gridy = 0;
        this.add(topPanel(), c);
    }

    /**Creates a scroll pane for the text area.
     *@return A scroll pane containing a text area.
    private JScrollPane middlePanel() {
        JScrollPane pane = new JScrollPane(variableArea0());
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return (pane);
    }*/
    /**Creates a text area that user can input data into.
     *@return The text area*/
    private JTextField dataArea(){
        variableArea0 = new JTextField();
        variableArea0.setBackground(Color.WHITE);
        variableArea0.setEditable(false);

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

    /**Method which creates the label.
     *@return The label*/
    private JLabel dataLabel(){
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
    private void updateMeasureData(){
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
        ErrorManager.sendErrorMessage("GUI", "Program attempted to set data for a data field which does not exist");
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
        ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
    }

    @Override
    public void setVariableLabel(int index, String label) {
        switch (index) {
            case 0 -> variableLabel0.setText(label);
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
        }
    }
}
