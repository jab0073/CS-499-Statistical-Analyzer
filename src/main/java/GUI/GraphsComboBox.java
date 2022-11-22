package GUI;

import BackEndUtilities.MeasureManager;
import FrontEndUtilities.GUIDataMaster;
import FrontEndUtilities.GUIMeasure;
import Graphing.GraphTypes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

public class GraphsComboBox {
    private static DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
    private static JPanel panel;
    private static JComboBox box;
    public JPanel graphsComboBoxPanel(){
        panel = new JPanel();
        panel.add(label());
        panel.add(dropDown());
        return panel;
    }

    private JLabel label(){
        return(new JLabel("Select Graph:"));
    }

    public static void setModel(){
        String[] names = RightPanel.getGraphsListStr();

        if(names == null || names.length == 0){
            panel.setVisible(false);
            return;
        }else{
            panel.setVisible(true);
        }

        for (int i = 0; i < names.length; i++) {
            model.addElement(names[i]);
        }
    }

    public static DefaultComboBoxModel<String> getModel(){
        return(model);
    }

    private JComboBox dropDown(){
        box = new JComboBox(model);
        box.setPrototypeDisplayValue("Select Graph");
        box.addActionListener(e -> setGraph(box.getSelectedIndex()));
        return(box);
    }

    private void setGraph(int graphsIndex){
        if(box.getItemAt(graphsIndex) != null) {
            String graph = box.getItemAt(graphsIndex).toString();
            for (Object value : RightPanel.getGraphsList()) {
                if (Objects.equals(graph, String.valueOf(value))) {
                    GUIMeasure m = GUIDataMaster.getGUIMeasure(RightPanel.getCurrentMeasureIndex());
                    m.setSelectedGraph((GraphTypes) value);
                }
            }
        }
    }
}
