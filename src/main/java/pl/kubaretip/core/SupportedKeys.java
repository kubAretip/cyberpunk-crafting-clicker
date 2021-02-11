package pl.kubaretip.core;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * type 0 = keyboard events
 * type 1 = mouse events
 */
public enum SupportedKeys {
    Z(KeyEvent.getKeyText(KeyEvent.VK_Z), KeyEvent.VK_Z, 0),
    X(KeyEvent.getKeyText(KeyEvent.VK_X), KeyEvent.VK_X, 0),
    MOUSE_L_BUTTON("Left mouse button", InputEvent.BUTTON1_DOWN_MASK, 1);

    private final String name;
    private final int event;
    private final int type;

    SupportedKeys(String name, int event, int type) {
        this.name = name;
        this.event = event;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getEvent() {
        return event;
    }

    public int getType() {
        return type;
    }
}
