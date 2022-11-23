package GUI;
import FrontEndUtilities.GUIDataMaster;
import Settings.Themes;
import TableUtilities.Cell;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Frame extends JFrame {
    private static final ArrayList<Card> cards = new ArrayList<>();
    private static JPanel cardPanel;
    private CellsTable table;

    /**Method for generating the frame which holds the GUI*/
    public Frame() {
        /*Create a frame, give it a size, set it to exit on close.*/
        this.setTitle("Analysis");


        AltMenuBar amb = new AltMenuBar();
        this.setJMenuBar(amb.getMenuBar());
        this.setSize(1000, 750);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /*Create a layout for the frame and add in the panels in their appropriate positions.*/
        this.setLayout(new BorderLayout());
        //this.add(windowPanelTop(), BorderLayout.NORTH);
        this.add(windowPanelLeft(), BorderLayout.WEST);
        this.add(fullWindowPanelMiddle(), BorderLayout.CENTER);
        this.add(windowPanelRight(), BorderLayout.EAST);

        /**JButton button = new JButton("Change");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) cardPanel.getLayout();
                layout.next(cardPanel);
            }
        });
        window.add(button, BorderLayout.SOUTH);*/

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e)
            {
                // TODO: Add dialog box to prompt if user wants to save or export before closing

                // TODO: Save Table contents as DataSet or export to preferred file format

                Frame.closeDialogs();
                e.getWindow().dispose();
            }
        });

        /*Set the frame to start maximized and visible.*/
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    /**Method which adds the menu bar panel to a panel which will be placed at the top of the application.
     *@return The panel containing the menu bar panel.*/
    private JPanel windowPanelTop(){
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(new MenuBar().topPanel(), BorderLayout.PAGE_START);

        return(panel);
    }

    /**Method which adds the cells table panel to a panel which will be placed to the left of the application.
     *@return The panel containing the cells table panel.*/
    private JPanel windowPanelLeft(){
        JPanel panel = new JPanel(new BorderLayout());

        table = new CellsTable();

        panel.add(table, BorderLayout.LINE_START);

        return(panel);
    }

    /**Method which adds the middle panel to a panel which will be placed in the middle of the application.
     *@return The panel containing the middle panel.*/
    private JPanel windowPanelMiddle(){
        JPanel panel = new JPanel();
        CardLayout layout = new CardLayout();
        panel.setLayout(layout);

        cardPanel = panel;

        /**The "cards" in the card layout, able to be cycled through. Name indicates layout.*/
        cards.add(new MiddlePanel());
        cards.add(new MiddlePanelTwo());
        cards.add(new MiddlePanelThree());
        cards.add(new MiddlePanelFour());
        cards.add(new MiddlePanelFive());
        cards.add(new MiddlePanelSix());

        for(Card c : cards){
            panel.add(c, c.getType().getName());
        }

        panel.add(new GraphsComboBox().graphsComboBoxPanel());

        layout.show(panel, CardTypes.ONE_DATA_ONE_VARIABLE.getName());

        return(panel);
    }

    private JPanel fullWindowPanelMiddle(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        panel.add(windowPanelMiddle(), c);
        c.gridy = 1;
        panel.add(new GraphsComboBox().graphsComboBoxPanel(), c);
        return panel;
    }

    /**Method which adds the right panel to a panel which will be placed to the right of the application.
     *@return The panel containing the right panel.*/
    private JPanel windowPanelRight(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new RightPanel().rightPanel(), BorderLayout.CENTER);
        return(panel);
    }


    public static void closeDialogs() {
        Window[] children = Frame.getWindows();
        for (Window win : children) {
            if (win instanceof JDialog) {
                win.dispose();
            }
        }
    }

    public static Card swapCard(CardTypes card){
        CardLayout layout = (CardLayout) cardPanel.getLayout();
        layout.show(cardPanel, card.getName());

        return cards.stream().filter(c -> c.getType() == card).findFirst().orElse(null);

    }

    public void setLookAndFeel(String laf) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(Themes.getTheme(laf));

        SwingUtilities.updateComponentTreeUI(this);

        table.setGridColor(Color.GRAY);
    }

    public void setZoom(int zoom){
        int fontSize = (int) (12 * ((double)zoom/100.0));

        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", new Font("Segoe UI", Font.PLAIN, fontSize));

        SwingUtilities.updateComponentTreeUI(this);

        table.setGridColor(Color.GRAY);
    }
}