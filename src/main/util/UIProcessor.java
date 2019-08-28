package util;


import javax.swing.*;
import java.awt.*;

/**
 * Helper class that allows javaSwing objects to be passed into it and modified to a desired theme.
 * Set a colour scheme that can be modified.
 */
public final class UIProcessor {
    //Collection of Colors
    Color shadeJListColor;
    Color shadeJPannelShadeone;
    Color shadeThree;
    Color shadeFour;
    Color shadeFive;
    Color shadeSix;
    Color shadeSeven;
    Color shadeEight;
    Color shadeNine;

    public UIProcessor() {
        shadeJListColor = new Color(169, 183, 198);
        shadeJPannelShadeone = new Color(49, 51, 53);
    }

    public void process(JList object) {
        object.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(shadeJListColor);

                return c;
            }
        });
    }

    public void process(JPanel object) {
        object.setOpaque(true);
        object.setBackground(shadeJPannelShadeone);
    }

    public void process(JButton object) {

    }
}
