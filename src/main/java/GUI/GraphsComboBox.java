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
    public JPanel graphsComboBoxPanel(){
        JPanel panel = new JPanel();
        panel.add(label());
        panel.add(dropDown());
        return panel;
    }

    private JLabel label(){
        return(new JLabel("Select Graph:"));
    }

    public static void setModel(){
        String[] names = RightPanel.getGraphsListStr();
        for (int i = 0; i < names.length; i++) {
            model.addElement(names[i]);
        }
    }

    public static DefaultComboBoxModel<String> getModel(){
        return(model);
    }

    private JComboBox dropDown(){
        JComboBox box = new JComboBox(model);
        box.setPrototypeDisplayValue("Select Graph");
        box.addActionListener(e -> setGraph(box.getSelectedIndex()));
        return(box);
    }

    private void setGraph(int graphsIndex){
        if(dropDown().getItemAt(graphsIndex) != null) {
            String graph = dropDown().getItemAt(graphsIndex).toString();
            for (Object value : RightPanel.getGraphsList()) {
                if (Objects.equals(graph, String.valueOf(value))) {
                    GUIMeasure m = GUIDataMaster.getGUIMeasure(RightPanel.getCurrentMeasureIndex());
                    m.setSelectedGraph((GraphTypes) value);
                }
            }
        }
    }
}
