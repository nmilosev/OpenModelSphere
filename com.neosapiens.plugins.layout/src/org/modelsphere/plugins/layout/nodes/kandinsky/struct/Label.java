package org.modelsphere.plugins.layout.nodes.kandinsky.struct;

import java.awt.Rectangle;

/**
 * The Class Label.
 */
public class Label {

    /**
     * The Enum PreferedPosition.
     */
    public enum PreferedPosition {

        /** The source. */
        source,
        /** The center. */
        center,
        /** The target. */
        target
    }

    /** The label text. */
    private String labelText;

    /** The position. */
    private PreferedPosition position;

    /**
     * Instantiates a new label.
     * 
     * @param labelText
     *            the label text
     * @param labelPosition
     *            the label position
     */
    public Label(String labelText, PreferedPosition labelPosition) {
        this.labelText = labelText;
        position = labelPosition;
    }

    /**
     * Gets the prefered position.
     * 
     * @return the prefered position
     */
    public PreferedPosition getPreferedPosition() {
        return position;
    }

    /**
     * Gets the text.
     * 
     * @return the text
     */
    public String getText() {
        return labelText;
    }

    /**
     * Gets the size.
     * 
     * @return the size
     */
    public Rectangle getSize() {
        // TODO
        return null;
    }
}
