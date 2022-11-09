package GUI;

import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiddlePanelFive extends Card{
    private static JTextArea dataArea;
    private static JTextField dataArea2;

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
        c.gridy = 2;
        panel.add(topPanel2(), c);
        c.gridy = 3;
        panel.add(dataAreaTwo(), c);

        /*Change the y position value for the gridbag constraints and apply to the panel containing the
        * button and label.*/
        c.gridy = 0;
        panel.add(topPanel(), c);
        return(panel);
    }

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
        this.add(dataAreaTwo(), c);

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

    private JTextField dataAreaTwo(){
        dataArea2 = new JTextField();
        dataArea2.setEditable(false);
        dataArea2.setBackground(Color.WHITE);

        dataArea2.getDocument().addDocumentListener(new DocumentListener() {
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

        return(dataArea2);
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

    private JButton importButton2(){
        JButton btnImport = new JButton("Import From Chart");

        btnImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataArea2.setText(CellsTable.getSelectedData());
                updateMeasureDataTwo();
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

    private void updateMeasureDataTwo(){
        String[] data = dataArea2.getText().split(",");

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
