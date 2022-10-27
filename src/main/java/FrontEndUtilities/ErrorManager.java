package FrontEndUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ErrorManager {
    private static ArrayList<String> errors = new ArrayList<>();

    public static void displayErrors(){
        if(errors.size() == 0) return;

        JDialog frame = new JDialog();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        frame.setTitle("Error");
        frame.setLayout(new BorderLayout());

        JTextPane textPane = new JTextPane();

        for(String s : errors){
            textPane.setText(textPane.getText() + "\n" + s);
        }

        JButton btnOK = new JButton();
        btnOK.setText("Acknowledge");
        btnOK.addActionListener(e -> frame.dispose());

        textPane.setEditable(false);

        frame.add(textPane);
        frame.add(btnOK, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Pushes an error message into the error manager
     * @param message the message to display
     * @param sender the name of the error sender
     */
    public static void sendErrorMessage(String sender, String message){
        errors.add(sender + ":\n-" + message);
    }

    public static void clearErrors(){
        errors.clear();
    }

}
