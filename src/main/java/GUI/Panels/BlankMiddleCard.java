package GUI.Panels;

import Enums.CardTypes;
import GUI.Card;
import Managers.ErrorManager;
import java.awt.*;

public class BlankMiddleCard extends Card {
    private final CardTypes type = CardTypes.BLANK;


    public BlankMiddleCard() {
        /*Create a JPanel with a grid bag layout*/
        this.setLayout(new GridBagLayout());
    }

    @Override
    public CardTypes getType() {
        return type;
    }

    @Override
    public void setDataArea(int index, String data) {
        switch (index) {
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set data for a data field which does not exist");
        }
    }

    @Override
    public void setVariableArea(int index, String data) {
        ErrorManager.sendErrorMessage("GUI", "Program attempted to set data for a data field which does not exist");
    }

    @Override
    public void setDataLabel(int index, String label) {
        switch (index) {
            default -> ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
        }
    }

    @Override
    public void setVariableLabel(int index, String label) {
        ErrorManager.sendErrorMessage("GUI", "Program attempted to set name for a label which does not exist");
    }
}
