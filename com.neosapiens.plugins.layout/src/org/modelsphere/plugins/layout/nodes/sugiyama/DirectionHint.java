package org.modelsphere.plugins.layout.nodes.sugiyama;

/**
 * Allows the class user to specify whether he prefers his scan to be performed bottom-up or
 * top-down (which can make a big difference on the results depending on the input data).
 * 
 * @author Gabriel
 */
public enum DirectionHint {

    /** The Top down. */
    TopDown(1),
    /** The Bottom up. */
    BottomUp(2);

    /** The value. */
    private int value;

    /**
     * Instantiates a new direction hint.
     * 
     * @param hint
     *            the hint
     */
    DirectionHint(int hint) {
        value = hint;
    }

    /**
     * Gets the value.
     * 
     * @return the value
     */
    public int getValue() {
        return value;
    }
}
