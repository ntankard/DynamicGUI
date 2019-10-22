package com.ntankard.DynamicGUI.Components.Filter.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import java.util.function.Predicate;

/**
 * Creates a predicate to filter a specific member based on GUI inputs
 */
public abstract class MemberFilter extends UpdatableJPanel {

    /**
     * The member that this panel is built around
     */
    private Member baseMember;

    /**
     * Constructor
     *
     * @param baseMember The member connected to this panel
     * @param master     The top level GUI
     */
    MemberFilter(Member baseMember, Updatable master) {
        super(master);
        this.baseMember = baseMember;
    }

    /**
     * Get the predicate generated from this panels content
     *
     * @return The predicate generated from this panels content
     */
    public abstract Predicate getPredicate();

    /**
     * Get the member that this panel is built around
     *
     * @return The member that this panel is built around
     */
    protected Member getBaseMember() {
        return baseMember;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void update() {
    }
}
