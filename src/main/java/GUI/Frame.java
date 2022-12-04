package GUI;

import Settings.Themes;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Frame extends JFrame {
    private static final ArrayList<Card> cards = new ArrayList<>();
    private static JPanel cardPanel;
    private CellsTable table;
    private RightPanel rightPanel;

    /**Method for generating the frame which holds the GUI*/
    public Frame() {
        /*Create a frame, give it a size, set it to exit on close.*/
        this.setTitle("Analysis");


        AltMenuBar amb = new AltMenuBar();
        this.setJMenuBar(amb.getMenuBar());
        this.setSize(new Dimension(1000, 500));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*One panel containing other panels, centered so it will resize with frame*/
        this.setLayout(new BorderLayout());
        this.add(window(), BorderLayout.CENTER);

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

    private JPanel window(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.8; c.weighty = 0.3;
        c.gridy = 0; c. gridx = 0;
        panel.add(windowPanelLeft(), c);

        //this.add(windowPanelTop(), BorderLayout.NORTH);

        GridBagConstraints d = new GridBagConstraints();
        d.anchor = GridBagConstraints.FIRST_LINE_END;
        d.fill = GridBagConstraints.BOTH;
        d.weightx = 0.1; d.weighty = 0.3;
        d.gridx = 3;
        panel.add(windowPanelRight(), d);

        GridBagConstraints e = new GridBagConstraints();
        e.anchor = GridBagConstraints.CENTER;
        e.fill = GridBagConstraints.BOTH;
        e.weightx = 0.1; e.weighty = 0.3;
        e.gridx = 2;
        panel.add(windowPanelMiddle(), e);

        JScrollPane pane = new JScrollPane(panel);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return(panel);
    }

    /**Method which adds the cells table panel to a panel which will be placed to the left of the application.
     *@return The panel containing the cells table panel.*/
    private JPanel windowPanelLeft(){
        JPanel panel = new JPanel(new BorderLayout());
        table = new CellsTable();
        JScrollPane pane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(pane, BorderLayout.CENTER);

        return(panel);
    }

    /**Method which adds the middle panel to a panel which will be placed in the middle of the application.
     *@return The panel containing the middle panel.*/
    private JPanel cardPanel(){
        JPanel panel = new JPanel();
        CardLayout layout = new CardLayout();
        panel.setLayout(layout);

        cardPanel = panel;

        //The "cards" in the card layout, able to be cycled through. Name indicates layout.
        cards.add(new MiddlePanel());
        cards.add(new MiddlePanelTwo());
        cards.add(new MiddlePanelThree());
        cards.add(new MiddlePanelFour());
        cards.add(new MiddlePanelFive());
        cards.add(new MiddlePanelSix());
        cards.add(new BlankMiddleCard());

        for(Card c : cards){
            panel.add(c, c.getType().getName());
        }

        panel.add(new GraphsComboBox().graphsComboBoxPanel());

        layout.show(panel, CardTypes.BLANK.getName());

        return(panel);
    }

    private JPanel windowPanelMiddle(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(cardPanel(), BorderLayout.CENTER);
        panel.add(new GraphsComboBox().graphsComboBoxPanel(), BorderLayout.SOUTH);
        return panel;
    }

    /**Method which adds the right panel to a panel which will be placed to the right of the application.
     *@return The panel containing the right panel.*/
    private JPanel windowPanelRight(){
        JPanel panel = new JPanel(new BorderLayout());

        rightPanel = new RightPanel();

        panel.add(rightPanel, BorderLayout.CENTER);
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

    /**
     * Sets the look and feel as well as zoom for the program
     * @param laf The New look and feel for the application
     * @param userZoom The zoom percentage the user selected
     */
    public void setLookAndFeel(String laf, float userZoom) throws UnsupportedLookAndFeelException {
        LookAndFeel l = Themes.getTheme(laf);

        //If theme is different from current theme, then change it
        if(!UIManager.getLookAndFeel().equals(l)){
            UIManager.setLookAndFeel(l);
        }

        FontUIResource f = (FontUIResource) l.getDefaults().get("defaultFont");

        int zoom = Math.round(12F * (userZoom/100.0F));

        FontUIResource newFont = (new FontUIResource(f.getFontName(), f.getStyle(), zoom));

        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", newFont);

        SwingUtilities.updateComponentTreeUI(this);

        table.setGridColor(Color.GRAY);
    }

    public CellsTable getCellsTable(){
        return table;
    }

    public void updateRightPanelForLoad(){
        rightPanel.updateForLoad();
    }
}