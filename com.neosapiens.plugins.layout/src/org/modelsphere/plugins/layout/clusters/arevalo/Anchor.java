/**
 * 
 */
package org.modelsphere.plugins.layout.clusters.arevalo;

import java.awt.Point;

/**
 * An anchor is a free point where a new bounds may be added.
 * 
 * @author David
 */
public class Anchor extends Point implements Comparable<Point> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4845201005371500563L;

    /**
     * Instantiates a new anchor.
     */
    public Anchor() {
        this(0, 0);
    }

    /**
     * Instantiates a new anchor.
     * 
     * @param x
     *            the x
     * @param y
     *            the y
     */
    public Anchor(int x, int y) {
        super(x, y);
        this.x = x > 0 ? x : 0;
        this.y = y > 0 ? y : 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Point point) {

        Integer thisX = x;
        Integer thisY = y;

        switch (thisX.compareTo(point.x)) {
        case 1:
            return 1;

        case 0:
            return thisY.compareTo(point.y);

        case -1:
            return -1;

        default:
            // Should never happen
            return 0;
        }
    }

}
