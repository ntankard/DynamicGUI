package com.ntankard.DynamicGUI.Components.Filter.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Updatable;

import java.util.function.Predicate;

/**
 * Creates a predicate to filter a specific member based on GUI inputs
 */
public abstract class MemberFilter extends Updatable.UpdatableJPanel{

    /**
     * The member connected to this panel
     */
    private Member baseMember;

    /**
     * Constructor
     * @param baseMember The member connected to this panel
     * @param master The top level GUI
     */
    public MemberFilter(Member baseMember, Updatable master){
        super(master);
        this.baseMember = baseMember;
    }

    /**
     * Get the predicate generated from this panels content
     * @return The predicate generated from this panels content
     */
    public abstract Predicate getPredicate();

    /**
     * Get the member connected to this panel
     * @return The member connected to this panel
     */
    protected Member getBaseMember(){
        return baseMember;
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
    }
}