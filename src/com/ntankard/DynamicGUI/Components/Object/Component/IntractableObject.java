package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Updatable;

public abstract class IntractableObject extends Updatable.UpdatableJPanel {

    /**
     * The member connected to this panel
     */
    private Member baseMember;

    /**
     * The instance of the baseMember to interact with
     */
    private Object baseInstance;

    /**
     * Constructor
     * @param baseMember   The member connected to this panel
     * @param baseInstance The instance of the baseMember to interact with
     * @param master       The top level GUI
     */
    protected IntractableObject(Member baseMember, Object baseInstance, Updatable master) {
        super(master);
        this.baseMember = baseMember;
        this.baseInstance = baseInstance;
    }

    /**
     * Get the member connected to this panel
     * @return The member connected to this panel
     */
    public Member getBaseMember() {
        return baseMember;
    }

    /**
     * Gets the instance of the baseMember to interact with
     * @return The instance of the baseMember to interact with
     */
    public Object getBaseInstance() {
        return baseInstance;
    }
}