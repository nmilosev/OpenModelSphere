/*************************************************************************

Copyright (C) 2009 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.graphic;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;

/**
 * Display a grid in the diagram background
 */
public class Grid {
    public static final String PROPERTY_HIDE_GRID = "grid.hide"; // NOT LOCALIZABLE, property
    public static final boolean PROPERTY_HIDE_GRID_DEFAULT = true;
    public static final String PROPERTY_NB_OF_CELLS_IN_X = "grid.numberofcells.x"; // NOT LOCALIZABLE, property
    public static final int PROPERTY_NB_OF_CELLS_IN_X_DEFAULT = 8;
    public static final String PROPERTY_NB_OF_CELLS_IN_Y = "grid.numberofcells.y"; // NOT LOCALIZABLE, property
    public static final int PROPERTY_NB_OF_CELLS_IN_Y_DEFAULT = 12;
    public static final String PROPERTY_PERCENT_OF_CELL = "grid.percentofcell"; // NOT LOCALIZABLE, property
    public static final int PROPERTY_PERCENT_OF_CELL_DEFAULT = 100;
    public static final String PROPERTY_GRID_COLOR = "grid.color"; // NOT LOCALIZABLE, property
    public static final int PROPERTY_GRID_COLOR_DEFAULT = Color.lightGray.getRGB();
    public static final String PROPERTY_GRID_ACTIVE = "grid.active"; // NOT LOCALIZABLE, property
    public static final Boolean PROPERTY_GRID_ACTIVE_DEFAULT = Boolean.FALSE;

    // Properties set by the constructor
    private boolean active;
    private int cellCountX;
    private int cellCountY;
    private int fillPercent;
    private Color color;

    private double xDelta = 0;
    private double yDelta = 0;

    // Pattern dependent properties - pattern is valid as long as zoomFactor and
    // pageSize don't change.
    private float zoomFactor = 1.0f;
    private Dimension pageSize;
    private BufferedImage pattern;
    private TexturePaint texturePaint;

    Grid() {
        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
        active = options.getPropertyBoolean(Grid.class, Grid.PROPERTY_GRID_ACTIVE, new Boolean(
                Grid.PROPERTY_GRID_ACTIVE_DEFAULT));
        cellCountX = options.getPropertyInteger(Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_X,
                new Integer(Grid.PROPERTY_NB_OF_CELLS_IN_X_DEFAULT));
        cellCountY = options.getPropertyInteger(Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_Y,
                new Integer(Grid.PROPERTY_NB_OF_CELLS_IN_Y_DEFAULT));
        fillPercent = options.getPropertyInteger(Grid.class, Grid.PROPERTY_PERCENT_OF_CELL,
                new Integer(Grid.PROPERTY_PERCENT_OF_CELL_DEFAULT));
        int rgb = options.getPropertyInteger(Grid.class, Grid.PROPERTY_GRID_COLOR,
                new Integer(Grid.PROPERTY_GRID_COLOR_DEFAULT)).intValue();
        color = new Color(rgb);
    }

    // Called by Diagram.paintAux()
    void paint(Graphics g, DiagramView diagView, int left, int top, int right, int bottom,
            Dimension currentPageSize) {
        if (diagView == null) {
            return;
        }
        float currentZoomFactor = diagView.getZoomFactor();

        // Too small on screen, unreadable, do not paint unless printing or
        // saving image
        if (currentZoomFactor <= 0.25f)
            return;

        // The pattern is valid until pageSize or zoomFactor change
        if (currentZoomFactor != zoomFactor || !currentPageSize.equals(pageSize)) {
            invalidate();
        }

        if (pattern == null) {
            // Create the pattern
            zoomFactor = currentZoomFactor;
            pageSize = currentPageSize;
            // dimension of the grid
            xDelta = (double) pageSize.getWidth() * zoomFactor / (double) cellCountX;
            yDelta = (double) pageSize.getHeight() * zoomFactor / (double) cellCountY;
            pattern = createGridPattern();
        }

        Graphics2D g2 = (Graphics2D) g;
        Paint oldPaint = g2.getPaint();

        // Fill the view area with the texture
        g2.setPaint(texturePaint);
        g2.fillRect(2, 2, (int) (diagView.getWidth()), (int) (diagView.getHeight()));

        g2.setPaint(oldPaint);
    }

    public boolean isActive() {
        return active;
    }

    public Point getClosestGridBoundary(Point p) {

        if (zoomFactor <= 0.25f) // Too small on screen, unreadable, do not
            // paint unless printing or saving image
            return p;
        if (!active)
            return p;

        int x = p.x, y = p.y;
        double xLocalDelta = xDelta / zoomFactor;
        double yLocalDelta = yDelta / zoomFactor;
        if (xLocalDelta == 0 || yLocalDelta == 0)
            return p;

        double pagewidth = xLocalDelta * cellCountX + 1;
        for (double i = xLocalDelta; i < pagewidth; i += xLocalDelta) {
            if (p.x <= i - 40 / zoomFactor) {
                x = (int) (i - xLocalDelta);
                break;
            }
        }
        double pageheight = yLocalDelta * cellCountY + 1;
        for (double j = yLocalDelta; j < pageheight; j += yLocalDelta) {
            if (p.y <= j - 30 / zoomFactor) {
                y = (int) (j - yLocalDelta);
                break;
            }
        }
        return new Point(x, y);
    }

    public double getHorizontalOffset() {
        return (xDelta / zoomFactor);
    }

    public double getVerticalOffset() {
        return (yDelta / zoomFactor);
    }

    private BufferedImage createGridPattern() {
        BufferedImage pattern = new BufferedImage((int) xDelta, (int) yDelta,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D patternGraphics = (Graphics2D) pattern.createGraphics();
        Composite composite = patternGraphics.getComposite();
        patternGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        patternGraphics.fillRect(0, 0, (int) xDelta, (int) yDelta);

        patternGraphics.setComposite(composite);
        patternGraphics.setColor(color);

        // if percentOfCell = 100, then percentOfCell = 0.5 (two halves drawn
        // means a whole grid)
        double portionToDraw = fillPercent * 0.005;
        double xlength = xDelta * portionToDraw;
        double ylength = yDelta * portionToDraw;
        double xmiddle = xDelta / 2D;
        double ymiddle = yDelta / 2D;

        // Paint the intersections in the middle of the pattern.
        patternGraphics.drawLine((int) (xmiddle - xlength), (int) ymiddle,
                (int) (xmiddle + xlength), (int) ymiddle);
        patternGraphics.drawLine((int) xmiddle, (int) (ymiddle - ylength), (int) xmiddle,
                (int) (ymiddle + ylength));

        patternGraphics.dispose();

        // Create the texture
        Rectangle2D textureBounds = new Rectangle2D.Double(-xDelta / 2D, -yDelta / 2D, xDelta,
                yDelta);
        texturePaint = new TexturePaint(pattern, textureBounds);

        return pattern;
    }

    private int[] computeGridX(DiagramView view) {
        double delta = getHorizontalOffset();
        Rectangle drawingArea = view.getModel().getDrawingArea();
        int[] gridx = new int[(int) (drawingArea.getWidth() / delta) + 1];
        for (int i = 0; i < gridx.length - 1; i++) {
            gridx[i] = (int) (delta * i);
        }
        gridx[gridx.length - 1] = (int) drawingArea.getWidth();
        return gridx;
    }

    private int[] computeGridY(DiagramView view) {
        double delta = getVerticalOffset();
        Rectangle drawingArea = view.getModel().getDrawingArea();
        int[] gridy = new int[(int) (drawingArea.getHeight() / delta) + 1];
        for (int i = 0; i < gridy.length; i++) {
            gridy[i] = (int) (delta * i);
        }
        gridy[gridy.length - 1] = (int) drawingArea.getHeight();
        return gridy;
    }

    void invalidate() {
        flush();
    }

    void flush() {
        if (pattern == null)
            return;
        pattern.flush();
        pattern = null;
    }

    public Rectangle snapTo(DiagramView view, Rectangle rect) {
        if (rect == null)
            return rect;
        Rectangle snapedRect = new Rectangle(rect);

        int[] gridx = computeGridX(view);
        int[] gridy = computeGridY(view);

        int tx = 0;
        int ty = 0;

        int tx1 = snap(gridx, rect.x);
        int tx2 = snap(gridx, rect.x + rect.width);
        if (Math.abs(tx1) <= Math.abs(tx2)) {
            tx = tx1;
        } else {
            tx = tx2;
        }

        int ty1 = snap(gridy, rect.y);
        int ty2 = snap(gridy, rect.y + rect.height);
        if (Math.abs(ty1) <= Math.abs(ty2)) {
            ty = ty1;
        } else {
            ty = ty2;
        }

        snapedRect.translate(tx, ty);
        return snapedRect;
    }

    private int snap(int[] axis, int coordinate) {
        // find closest axis to coordinate - result is the required translation
        // to snap
        int result = -1;
        for (int i = 0; i < axis.length; i++) {
            if (axis[i] >= coordinate) {
                if (i == 0) {
                    result = axis[0];
                } else {
                    if (coordinate - axis[i - 1] < axis[i] - coordinate)
                        result = axis[i - 1] - coordinate;
                    else
                        result = axis[i] - coordinate;
                }
                break;
            } else if (i == axis.length - 1) {
                result = axis[axis.length - 1] - coordinate;
            }
        }
        // if (result < 0){
        // if (axis.length > 0){
        // result = coordinate - axis[axis.length - 1];
        // } else {
        // result = -coordinate;
        // }
        // }
        return result;
    }

}
