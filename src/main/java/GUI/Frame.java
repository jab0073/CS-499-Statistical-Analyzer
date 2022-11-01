package GUI;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Frame extends JFrame {
    private static String CARD_PANEL ="2";
    private JPanel cardPanel;

    /**Method for generating the frame which holds the GUI*/
    public void frame() {
        /*Create a frame, give it a size, set it to exit on close.*/
        JFrame window = new JFrame("Analysis");
        window.setSize(1000, 750);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*Create a layout for the frame and add in the panels in their appropriate positions.*/
        window.setLayout(new BorderLayout());
        window.add(windowPanelTop(), BorderLayout.NORTH);
        window.add(windowPanelLeft(), BorderLayout.WEST);
        window.add(windowPanelMiddle(), BorderLayout.CENTER);
        window.add(windowPanelRight(), BorderLayout.EAST);

        /**JButton button = new JButton("Change");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) cardPanel.getLayout();
                layout.next(cardPanel);
            }
        });
        window.add(button, BorderLayout.SOUTH);*/

        /*Set the frame to start maximized and visible.*/
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
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

        panel.add(new CellsTable().cellsPanel(), BorderLayout.LINE_START);

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
        JPanel oneBox = new MiddlePanel().dataPanel();
        JPanel twoBoxes = new MiddlePanelTwo().dataPanel();
        JPanel oneLine = new MiddlePanelThree().dataPanel();
        JPanel twoLines = new MiddlePanelFour().dataPanel();
        JPanel boxLine = new MiddlePanelFive().dataPanel();

        panel.add(oneBox, "1");
        panel.add(twoBoxes, "2");
        panel.add(oneLine, "3");
        panel.add(twoLines, "4");
        panel.add(boxLine, "5");

        layout.show(panel, CARD_PANEL);

        return(panel);
    }

    /**Method which adds the right panel to a panel which will be placed to the right of the application.
     *@return The panel containing the right panel.*/
    private JPanel windowPanelRight(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new RightPanel().rightPanel(), BorderLayout.CENTER);
        return(panel);
    }
}