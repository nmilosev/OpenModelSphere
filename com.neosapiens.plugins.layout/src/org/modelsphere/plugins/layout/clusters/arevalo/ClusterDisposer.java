

package org.modelsphere.plugins.layout.clusters.arevalo;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.Vector;

/**
 * The Class Cluster.
 * 
 * @author David
 */
public class ClusterDisposer {

    // Lower factor avoid having diagram with a lot of free space
    /** The Constant INCREMENT_FACTOR. */
    private static final double INCREMENT_FACTOR = 1.5;

    /** The bounds. */
    private Rectangle bounds;

    /** The cluster bounds. */
    private Vector<Bounds> clusterBounds;

    /** The anchors. */
    private Vector<Anchor> anchors;

    /** The area used. */
    private long areaUsed;

    /** The id. */
    private int id;

    /**
     * Instantiates a new cluster.
     * 
     * @param layerId
     *            the layer id
     */
    public ClusterDisposer(int layerId) {
        bounds = new Rectangle(0, 0, 2, 2);
        clusterBounds = new Vector<Bounds>();
        anchors = new Vector<Anchor>();
        anchors.add(new Anchor(0, 0));
        areaUsed = 0;
        id = layerId;
    }

    /**
     * Instantiates a new cluster.
     * 
     * @param layerId
     *            the layer id
     * @param bounds
     *            the bounds
     * @param rectangles
     *            the rectangles
     * @param anchors
     *            the anchors
     */
    ClusterDisposer(int layerId, Rectangle bounds, Vector<Bounds> rectangles, Vector<Anchor> anchors) {
        this.bounds = bounds;
        clusterBounds = rectangles;
        this.anchors = anchors;
        areaUsed = 0;
        id = layerId;
    }

    /**
     * Gets the bounds.
     * 
     * @return the bounds
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Gets the rectangles.
     * 
     * @return the rectangles
     */
    public Vector<Bounds> getRectangles() {
        return clusterBounds;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the area used.
     * 
     * @return the area used
     */
    public long getAreaUsed() {
        return areaUsed;
    }

    /**
     * Gets the area bounds.
     * 
     * @return the area bounds
     */
    public long getAreaBounds() {
        return bounds.width * bounds.height;
    }

    /**
     * Add the given rectangle.
     * 
     * @param bounds
     *            the bounds
     * @return true, if successful
     */
    boolean addInFreeArea(Bounds bounds) {
        // Find a valid spot among available anchors.

        boolean freeAreaFound = false;
        Point posFound = null;

        for (Point anchor : anchors) {
            Bounds tryBounds = new Bounds(anchor.x, anchor.y, bounds.width, bounds.height);

            if (fitInFreeArea(tryBounds)) {
                bounds.copyFrom(tryBounds);
                freeAreaFound = true;
                posFound = anchor;
                break;
            }
        }

        if (freeAreaFound) {
            // Remove the used anchor point
            anchors.remove(posFound);

            // Sometimes, anchors end up displaced from the optimal position
            // due to irregular sizes of the subrects.
            // So, try to adjust it up & left as much as possible.
            int x;
            for (x = 1; x <= bounds.x; x++) {
                if (!fitInFreeArea(new Bounds(bounds.x - x, bounds.y, bounds.width, bounds.height))) {
                    break;
                }
            }

            int y;
            for (y = 1; y <= bounds.y; y++) {
                if (!fitInFreeArea(new Bounds(bounds.x, bounds.y - y, bounds.width, bounds.height))) {
                    break;
                }
            }

            if (y > x) {
                // bounds.y -= y - 1;
            } else {
                // bounds.x -= x - 1;
            }

            addBounds(bounds);
        }

        return freeAreaFound;
    }

    /**
     * Add a rectangle of the given size, growing our area if needed Area grows only until the max
     * given. Returns the placement of the rect in the rect's x,y coords
     * 
     * @param bounds
     *            the bounds
     * @param maxWidth
     *            the max width
     * @param maxHeight
     *            the max height
     * @return true, if successful
     */
    public boolean addAtEmptySpotAutoGrow(Bounds bounds, int maxWidth, int maxHeight) {

        int boundsWidth = this.bounds.width;
        int boundsHeight = this.bounds.height;

        // Try to add it in the existing space
        while (!addInFreeArea(bounds)) {
            int pw = this.bounds.width;
            int ph = this.bounds.height;

            // Sanity check - if area is complete.
            if (pw >= maxWidth && ph >= maxHeight) {
                this.bounds.width = boundsWidth;
                this.bounds.height = boundsHeight;
                return false;
            }

            // Try growing the smallest dim
            if (pw < maxWidth && (pw < ph || ((pw == ph) && (bounds.width >= bounds.height)))) {
                this.bounds.width = (int) Math.round(pw * INCREMENT_FACTOR);
            } else {
                this.bounds.height = (int) Math.round(ph * INCREMENT_FACTOR);
            }
            if (addInFreeArea(bounds)) {
                break;
            }

            // Try growing the other dim instead
            if (pw != this.bounds.width) {
                this.bounds.width = pw;
                if (ph < maxWidth) {
                    this.bounds.height = (int) Math.round(ph * INCREMENT_FACTOR);
                }
            } else {
                this.bounds.height = ph;
                if (pw < maxWidth) {
                    this.bounds.width = (int) Math.round(pw * INCREMENT_FACTOR);
                }
            }

            if (pw != this.bounds.width || ph != this.bounds.height) {
                if (addInFreeArea(bounds)) {
                    break;
                }
            }

            // Grow both if possible, and reloop.
            this.bounds.width = pw;
            this.bounds.height = ph;
            if (pw < maxWidth) {
                this.bounds.width = (int) Math.round(pw * INCREMENT_FACTOR);
            }
            if (ph < maxHeight) {
                this.bounds.height = (int) Math.round(ph * INCREMENT_FACTOR);
            }
        }
        return true;
    }

    /**
     * Add new anchor point.
     * 
     * @param point
     *            the point
     */
    void addAnchor(Anchor point) {

        anchors.add(point);

        // Each time an anchor is added we must reorder the Vector
        Collections.sort(anchors);
    }

    /**
     * Add the given rect and updates anchor points.
     * 
     * @param bounds
     *            the bounds
     */
    void addBounds(Bounds bounds) {
        clusterBounds.add(bounds);
        areaUsed += bounds.width * bounds.height;

        // Add two new anchor points
        addAnchor(new Anchor(bounds.x, bounds.y + bounds.height));
        addAnchor(new Anchor(bounds.x + bounds.width, bounds.y));
    }

    /**
     * Check if the given rectangle is partially or totally used.
     * 
     * @param bounds
     *            the bounds
     * @return true, if successful
     */
    boolean fitInFreeArea(Bounds bounds) {
        if (this.bounds.contains(bounds)) {
            for (Bounds nextBounds : clusterBounds) {
                if (nextBounds.intersects(bounds)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the position.
     * 
     * @param x
     *            the x
     * @param y
     *            the y
     */
    public void setPosition(int x, int y) {

        for (Bounds bounds : clusterBounds) {
            bounds.x = x + bounds.x - this.bounds.x;
            bounds.y = y + bounds.y - this.bounds.y;
        }

        for (Point anchor : anchors) {
            anchor.x = x + anchor.x - bounds.x;
            anchor.y = y + anchor.y - bounds.y;
        }

        bounds.x = x;
        bounds.y = y;
    }

}
