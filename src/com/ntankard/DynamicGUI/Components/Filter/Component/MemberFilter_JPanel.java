package com.ntankard.DynamicGUI.Components.Filter.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.util.function.Predicate;

/**
 * Creates a predicate to filter a specific member based on GUI inputs
 */
public abstract class MemberFilter_JPanel extends JPanel implements Updatable {

    /**
     * The top level GUI
     */
    private Updatable master;

    /**
     * The member connected to this panel
     */
    protected Member baseMember;

    /**
     * Constructor
     * @param baseMember The member connected to this panel
     * @param master The top level GUI
     */
    public MemberFilter_JPanel(Member baseMember,Updatable master){
        this.master = master;
        this.baseMember = baseMember;
    }

    /**
     * Get the predicate generated from this panels content
     * @return The predicate generated from this panels content
     */
    public abstract Predicate getPredicate();

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyUpdate() {
        master.notifyUpdate();
    }
}