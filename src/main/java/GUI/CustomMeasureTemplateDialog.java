package GUI;

import BackEndUtilities.DynamicJavaClassLoader;
import Enums.CardTypes;
import Enums.GraphTypes;
import FrontEndUtilities.GUIDataMaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class CustomMeasureTemplateDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel classLabel;
    private JTextField classTextArea;
    private JTextField reqVarsTextField;
    private JLabel reqVarsLabel;
    private JLabel numSamplesLabel;
    private JSpinner numSamplesSpinner;
    private JLabel returnTypeLabel;
    private JComboBox<String> returnTypeComboBox;
    private JCheckBox isGraphableBox;
    private JLabel graphableLabel;
    private JList<GraphTypes> graphsList;
    private JLabel graphListLabel;
    private JList<CardTypes> cardList;
    private JLabel cardLabel;
    private JLabel errorLabel;

    public CustomMeasureTemplateDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        final String ID_PATTERN = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";
        final Pattern classNamePattern = Pattern.compile(ID_PATTERN);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String className = classTextArea.getText();
                if (!classNamePattern.matcher(className).matches()) {
                    errorLabel.setText("Class Name invalid");
                    errorLabel.setForeground(Color.RED);
                } else {
                    errorLabel.setText("");
                    errorLabel.setForeground(Color.RED);
                    onOK();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

        model.addAll(Arrays.asList("Double", "String", "List<Double>", "List<String>"));

        this.returnTypeComboBox.setModel(model);

        SpinnerNumberModel spinModel = new SpinnerNumberModel();
        spinModel.setValue(1);
        spinModel.setMinimum(1);
        spinModel.setStepSize(1);
        spinModel.setMaximum(99999);
        this.numSamplesSpinner.setModel(spinModel);

        this.graphsList.setCellRenderer(new CheckboxListCellRenderer());
        DefaultListModel<GraphTypes> listModel = new DefaultListModel<>();
        listModel.addElement(GraphTypes.X_Y);
        listModel.addElement(GraphTypes.PIE_CHART);
        listModel.addElement(GraphTypes.HORIZONTAL_BAR);
        listModel.addElement(GraphTypes.VERTICAL_BAR);
        listModel.addElement(GraphTypes.NORMAL_CURVE);
        this.graphsList.setModel(listModel);
        this.graphsList.setSelectionModel(new DefaultListSelectionModel() {
            public void setSelectionInterval(int index0, int index1) {
                if (isSelectedIndex(index0))
                    super.removeSelectionInterval(index0, index1);
                else
                    super.addSelectionInterval(index0, index1);
            }
        });
        this.graphsList.setEnabled(false);

        this.isGraphableBox.addActionListener(a -> {
            if(!this.isGraphableBox.isSelected())
                this.graphsList.setEnabled(false);
            else
                this.graphsList.setEnabled(true);
        });

        DefaultListModel<CardTypes> cardListModel = new DefaultListModel<>();
        cardListModel.addElement(CardTypes.NO_DATA_ONE_VARIABLE);
        cardListModel.addElement(CardTypes.NO_DATA_TWO_VARIABLE);
        cardListModel.addElement(CardTypes.ONE_DATA_NO_VARIABLE);
        cardListModel.addElement(CardTypes.ONE_DATA_ONE_VARIABLE);
        cardListModel.addElement(CardTypes.ONE_DATA_TWO_VARIABLE);
        cardListModel.addElement(CardTypes.TWO_DATA_NO_VARIABLE);
        this.cardList.setModel(cardListModel);

        this.classTextArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                String className = classTextArea.getText();
                if (!classNamePattern.matcher(className).matches()) {
                    errorLabel.setText("Class Name invalid");
                    errorLabel.setForeground(Color.RED);
                } else {
                    errorLabel.setText("");
                    errorLabel.setForeground(Color.RED);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        this.pack();

        this.setLocationRelativeTo(GUIDataMaster.getFrameReference());

        this.setVisible(true);

    }

    private void onOK() {

        String className = this.classTextArea.getText();

        String reqVars = String.join( ",",Arrays.stream(this.reqVarsTextField.getText().strip().split(",")).map(v -> "\"" + v + "\"").toList());

        String numberSamples = this.numSamplesSpinner.getValue().toString();

        String returnType = Objects.requireNonNull(this.returnTypeComboBox.getSelectedItem()).toString();

        String isGraphable = this.isGraphableBox.isSelected() ? "true": "false";

        String selectedGraphs;

        if(this.isGraphableBox.isSelected()) {
            selectedGraphs = String.join(",", this.graphsList.getSelectedValuesList().stream().map(g -> {
                return "GraphTypes."+g.name();
            }).toList());
        }
        else
            selectedGraphs = "GraphTypes.NONE";

        String cardType = "CardTypes." + this.cardList.getSelectedValue().name();

        DynamicJavaClassLoader.generateTemplate(className, reqVars, isGraphable, selectedGraphs, cardType, numberSamples, returnType);

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}

class CheckboxListCellRenderer extends JCheckBox implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {

        setComponentOrientation(list.getComponentOrientation());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setSelected(isSelected);
        setEnabled(list.isEnabled());

        setText(value == null ? "" : value.toString());

        return this;
    }
}
