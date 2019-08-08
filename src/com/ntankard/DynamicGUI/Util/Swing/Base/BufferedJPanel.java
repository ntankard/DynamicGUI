package com.ntankard.DynamicGUI.Util.Swing.Base;

import com.ntankard.DynamicGUI.Util.Updatable;

import java.awt.*;

/**
 * A  panel that can be used to buffer inputs from the user until execute is called. Changes will result in the
 * panel going to an edited state until execute is called
 *
 * @param <B> The type of the value to buffer
 */
public abstract class BufferedJPanel<B> extends UpdatableJPanel {

    /**
     * The color to use when the panel is in an edited state
     */
    private static Color EDITED_COLOR = Color.YELLOW;

    /**
     * Should the action of the panel be done as soon as an update is received? or on command
     */
    private boolean saveOnUpdate;

    /**
     * The value set by the user that has not been executed yet
     */
    private B bufferedValue;

    /**
     * Is a buffed value waiting to be executed?
     */
    private boolean isEdited = false;

    /**
     * Constructor
     *
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param master       The parent of this object to be notified if data changes
     */
    protected BufferedJPanel(boolean saveOnUpdate, Updatable master) {
        super(master);
        this.saveOnUpdate = saveOnUpdate;
    }

    /**
     * Execute the action of this panel based on the buffed value
     */
    public void execute() {
        if (!saveOnUpdate && isEdited) {
            executeValue(bufferedValue);
            setBackground(null);
            isEdited = false;
            notifyUpdate();
        }
    }

    /**
     * Is a buffed value waiting to be executed?
     *
     * @return True if a buffed value waiting to be executed?
     */
    public boolean isEdited() {
        return isEdited;
    }

    /**
     * Discard the change made by the user and revert the panel to its original state
     */
    public void discard() {
        if (!saveOnUpdate && isEdited) {
            revert();
            setBackground(null);
            isEdited = false;
        }
    }

    /**
     * Revert the panel to the state it was in before the user made a change
     */
    abstract protected void revert();

    /**
     * Perform the action required when a value is changed
     *
     * @param value The value to use
     */
    abstract protected void executeValue(B value);

    /**
     * Notify that the value has changed on the GUI. Execute the change or save based on saveOnUpdate
     *
     * @param value The value on the GUI
     */
    protected void valueChanged(B value) {
        if (saveOnUpdate) {
            executeValue(value);
            notifyUpdate();
        } else {
            bufferedValue = value;
            setBackground(EDITED_COLOR);
            isEdited = true;
        }
    }
}
