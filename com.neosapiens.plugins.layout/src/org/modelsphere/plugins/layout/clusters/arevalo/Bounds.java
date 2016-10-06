package org.modelsphere.plugins.layout.clusters.arevalo;

import java.awt.Rectangle;

/**
 * A bounds is the dimension of a cluster and his position in the diagram.
 * Bounds are Comparable but not in the usual way. 
 * Take a look at method compareTo for more details.
 */
public class Bounds extends Rectangle implements Comparable<Bounds> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new bounds.
     */
    public Bounds() {
        this(0, 0, 0, 0);
    }

    /**
     * Instantiates a new bounds.
     * 
     * @param width
     *            the width
     * @param height
     *            the height
     */
    public Bounds(int width, int height) {
        this(0, 0, width, height);
    }

    /**
     * Instantiates a new bounds.
     * 
     * @param x
     *            the x
     * @param y
     *            the y
     * @param width
     *            the width
     * @param height
     *            the height
     */
    public Bounds(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.x = x > 0 ? x : 0;
        this.y = y > 0 ? y : 0;
        this.width = width > 0 ? width : 0;
        this.height = height > 0 ? height : 0;
    }

    /**
     * Instantiates a new bounds.
     * 
     * @param rectangle
     *            the rectangle
     */
    public Bounds(Rectangle rectangle) {
        super(rectangle);
        x = x > 0 ? x : 0;
        y = y > 0 ? y : 0;
        width = width > 0 ? width : 0;
        height = height > 0 ? height : 0;
    }

    /**
     * Note: this class has a natural ordering that is inconsistent with equals. Ordering is done
     * with Rectangle dimension but not position.
     * 
     * @param bounds
     *            the bounds
     * @return the int
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Bounds bounds) {

        if (width > bounds.width && width > bounds.height) {
            // this > bounds
            return 1;

        } else if (height > bounds.width && height > bounds.height) {
            // this > bounds
            return 1;

        } else if (bounds.width > width && bounds.width > height) {
            // this < bounds
            return -1;

        } else if (bounds.height > width && bounds.height > height) {
            // this < bounds
            return -1;

        } else {
            Integer thisArea = width * height;
            Integer oArea = bounds.width * bounds.height;

            return thisArea.compareTo(oArea);
        }
    }

    /**
     * Copy from.
     * 
     * @param source
     *            the source
     */
    public void copyFrom(Bounds source) {
        height = source.height;
        width = source.width;
        x = source.x;
        y = source.y;
    }

}
