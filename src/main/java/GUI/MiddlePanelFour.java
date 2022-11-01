package GUI;

import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiddlePanelFour {
    private static JTextField dataArea;
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
        panel.add(dataArea(), c);
        c.gridy = 2;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(topPanel2(), c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridy = 3;
        panel.add(dataAreaTwo(), c);

        /*Change the y position value for the gridbag constraints and apply to the panel containing the
        * button and label.*/
        c.gridy = 0;
        panel.add(topPanel(), c);
        return(panel);
    }

    /**Creates a scroll pane for the text area.
     *@return A scroll pane containing a text area.
    private JScrollPane middlePanel() {
        JScrollPane pane = new JScrollPane(dataArea());
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return (pane);
    }*/
    /**Creates a text area that user can input data into.
     *@return The text area*/
    private JTextField dataArea(){
        dataArea = new JTextField();
        dataArea.setBackground(Color.WHITE);
        dataArea.setEditable(false);

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
                dataArea.setText(CellsTable.getSelectedData());
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
