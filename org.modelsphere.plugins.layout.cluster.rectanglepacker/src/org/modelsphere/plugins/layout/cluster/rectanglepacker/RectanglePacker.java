package org.modelsphere.plugins.layout.cluster.rectanglepacker;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Based on works by Ivan Montes <drslump@drslump.biz>, <http://blog.netxus.es> - RectanglePacker.js
 * - An algorithm implementation in JavaScript for rectangle packing. - Published under LGPL (Lesser
 * General Public License). - Original credits: - Algorithm based on
 * <http://www.blackpawn.com/texts/lightmaps/default.html>
 * 
 * Class: RectanglePacker A class that finds an 'efficient' position for a rectangle inside another
 * rectangle without overlapping the space already taken.
 * 
 * Algorithm based on <http://www.blackpawn.com/texts/lightmaps/default.html>
 * 
 * It uses a binary tree to partition the space of the parent rectangle and allocate the passed
 * rectangles by dividing the partitions into filled and empty.
 * 
 */

public class RectanglePacker {
    private static class Rect implements Cloneable {
        int width;
        int height;
        int x;
        int y;
        Rect leftRect;
        Rect rightRect;
        boolean used = false;

        Rect() {
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            Rect clone = (Rect) super.clone();
            clone.leftRect = null;
            clone.rightRect = null;
            clone.used = false;
            return clone;
        }
    }

    private Rect root = new Rect();
    private int usedWidth = 0;
    private int usedHeight = 0;

    public RectanglePacker(int width, int height) {
        reset(width, height);
    }

    public void reset(int width, int height) {
        root.x = 0;
        root.y = 0;
        root.width = width;
        root.height = height;
        this.usedWidth = 0;
        this.usedHeight = 0;
        root.leftRect = null;
        root.rightRect = null;
    }

    public Dimension getUsedDimension() {
        return new Dimension(usedWidth, usedHeight);
    }

    public Point findCoords(int w, int h) {
        // perform the search
        Point coords = recursiveFindCoords(this.root, w, h);
        // if fitted then recalculate the used dimensions
        if (coords != null) {
            if (this.usedWidth < coords.x + w)
                this.usedWidth = (int) (coords.x + w);
            if (this.usedHeight < coords.y + h)
                this.usedHeight = (int) (coords.y + h);
        }
        return coords;
    }

    private Point recursiveFindCoords(Rect rect, int w, int h) {
        // if we are not at a leaf then go deeper
        if (rect.leftRect != null) {
            // check first the left branch if not found then go by the right
            Point coords = recursiveFindCoords(rect.leftRect, w, h);
            if (coords != null) {
                return coords;
            } else {
                return recursiveFindCoords(rect.rightRect, w, h);
            }
        } else {
            // if already used or it's too big then return
            if (rect.used || w > rect.width || h > rect.height)
                return null;

            // if it fits perfectly then use this gap
            if (w == rect.width && h == rect.height) {
                rect.used = true;
                return new Point(rect.x, rect.y);
            }

            // initialize the left and right leafs by clonning the current one
            try {
                rect.leftRect = (Rect) rect.clone();
                rect.rightRect = (Rect) rect.clone();
            } catch (CloneNotSupportedException e) {
            }

            // checks if we partition in vertical or horizontal
            if (rect.width - w > rect.height - h) {
                rect.leftRect.width = w;
                rect.rightRect.x = rect.x + w;
                rect.rightRect.width = rect.width - w;
            } else {
                rect.leftRect.height = h;
                rect.rightRect.y = rect.y + h;
                rect.rightRect.height = rect.height - h;
            }

            return recursiveFindCoords(rect.leftRect, w, h);
        }

    }

}
