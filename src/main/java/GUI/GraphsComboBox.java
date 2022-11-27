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
    private static final DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
    private static JPanel panel;
    private static JComboBox box;
    public JPanel graphsComboBoxPanel(){
        panel = new JPanel();
        panel.add(label());
        panel.add(dropDown());
        panel.setVisible(false);
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

        DefaultComboBoxModel<String> tempModel = new DefaultComboBoxModel<>();

        int preSelect = 0;
        GraphTypes current = GUIDataMaster.getGUIMeasure(RightPanel.getCurrentMeasureIndex()).getSelectedGraph();
        for (int i = 0; i < names.length; i++) {
            tempModel.addElement(names[i]);

            if(current != null && names[i].equals(current.getName())){
                preSelect = i;
            }
        }

        box.setModel(tempModel);

        box.setSelectedIndex(preSelect);
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
            for (GraphTypes value : RightPanel.getGraphsList()) {
                String name = value.getName();
                if (Objects.equals(graph, name)) {
                    GUIMeasure m = GUIDataMaster.getGUIMeasure(RightPanel.getCurrentMeasureIndex());
                    m.setSelectedGraph(value);
                }
            }
        }
    }

    public static void hide(){
        panel.setVisible(false);
    }
}
