package GUI;

import Enums.CardTypes;

import javax.swing.*;

/**
 * Interface for classes which implement a card for use in the middle panel of the gui
 */
public abstract class Card extends JPanel {

    abstract public CardTypes getType();

    abstract public void setDataArea(int index, String data);
    abstract public void setVariableArea(int index, String data);

    abstract public void setDataLabel(int index, String label);
    abstract public void setVariableLabel(int index, String label);
}
