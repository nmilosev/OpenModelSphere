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

import org.modelsphere.jack.graphic.shape.GraphicShape;

/**
 * Default Peer component for DbGraphicalObject.
 * 
 * @see org.modelsphere.jack.baseDb.db.DbGraphicalObjectI
 */
public class GraphicComponent {

    public static final int HANDLE_SIZE = ((int) (Diagram.PIXELS_PER_MM * 2.0) & 0xFFFE) + 1; // odd number

    // top and bottom margin to prevent cell selection (give extra space for box selection)
    public static final int TOP_BOTTOM_INSETS = ((int) (Diagram.PIXELS_PER_MM * 1.05) & 0xFFFE) + 1; // odd number

    public static final int DRAW_WHOLE = 0;
    public static final int DRAW_FRAME = 1;

    public static final int UPPER_LEFT = 0;
    public static final int CENTER_LEFT = 1;
    public static final int LOWER_LEFT = 2;
    public static final int UPPER_RIGHT = 3;
    public static final int CENTER_RIGHT = 4;
    public static final int LOWER_RIGHT = 5;

    public static final int LINE_PLAIN = 0;
    public static final int LINE_BOLD = 1;
    public static final int LINE_DASHED = 2;
    public static final int LINE_DASHED_BOLD = 3;
    public static final int LINE_BOLD_WIDTH = 3;

    public static final int NO_FIT = 0;
    public static final int HEIGHT_FIT = 1;
    public static final int TOTAL_FIT = 2;

    public static final int LEFT_BORDER = 0;
    public static final int RIGHT_BORDER = 1;
    public static final int TOP_BORDER = 2;
    public static final int BOTTOM_BORDER = 3;

    private static final int LINE_SELECTION_DELTA = 6;

    public static final BasicStroke[] lineStrokes = new BasicStroke[] {
            new BasicStroke(1.0f), // LINE_PLAIN
            new BasicStroke((float) LINE_BOLD_WIDTH), // LINE_BOLD
            new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
                    new float[] { 6.0f }, 0.0f), // LINE_DASHED
            new BasicStroke((float) LINE_BOLD_WIDTH, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_MITER, 10.0f, new float[] { 6.0f }, 0.0f) // LINE_DASHED_BOLD
    };

    protected Diagram diagram;
    protected int lineStyle = LINE_PLAIN;
    protected Color lineColor = Color.black;
    protected Color fillColor = Color.white;
    protected Color textColor = Color.black;
    protected int autoFitMode = NO_FIT;

    // the rectangle property covered by the drawing of the component
    private Rectangle m_rectangle = null;

    public final Rectangle getRectangle() {
        return m_rectangle;
    }

    // SHAPE PROPERTY
    private GraphicShape m_shape; // may be null if not a zoneBox

    public final GraphicShape getShape() {
        return m_shape;
    }

    public final void setShape(GraphicShape newShape) {
        m_shape = newShape;
        repaint();
    }

    public GraphicComponent(Diagram diagram, GraphicShape shape) {
        this.diagram = diagram;
        m_shape = shape;
        diagram.addComponent(this);
    }

    // all = true means we are called by diagram.removeAll
    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        if (!all) {
            repaint();
            diagram.removeComponent(this);
        }
        diagram = null;
    }

    public final boolean isValid() {
        return (diagram != null);
    }

    public Rectangle getSelectionAreaRectangle() {
        return getRectangle();
    }

    public final boolean isOverVerticalBorder(int x, int y) {
        int i = getOverWhichBorder(x, y);
        if (i != -1 && (i == LEFT_BORDER || i == RIGHT_BORDER))
            return true;
        return false;
    }

    public final boolean isOverBottomBorder(int x, int y) {
        int i = getOverWhichBorder(x, y);
        if (i != -1 && i == BOTTOM_BORDER)
            return true;
        return false;
    }

    // return -1 if not over any border
    public final int getOverWhichBorder(int x, int y) {
        Rectangle rect = this.getRectangle();
        BasicStroke lineStroke = this.getLineStroke();
        int lineWidth = (int) lineStroke.getLineWidth();
        Rectangle leftRect = new Rectangle(rect.x - Math.max(LINE_SELECTION_DELTA, lineWidth) / 2,
                rect.y, LINE_SELECTION_DELTA, rect.height);
        if (leftRect.contains(x, y))
            return LEFT_BORDER;
        Rectangle rightRect = new Rectangle((rect.x + rect.width)
                - Math.max(LINE_SELECTION_DELTA, lineWidth) / 2, rect.y, LINE_SELECTION_DELTA,
                rect.height);
        if (rightRect.contains(x, y))
            return RIGHT_BORDER;
        Rectangle topRect = new Rectangle(rect.x, rect.y
                - Math.max(LINE_SELECTION_DELTA, lineWidth) / 2, rect.width, LINE_SELECTION_DELTA);
        if (topRect.contains(x, y))
            return TOP_BORDER;
        Rectangle bottomRect = new Rectangle(rect.x, (rect.y + rect.height)
                - Math.max(LINE_SELECTION_DELTA, lineWidth) / 2, rect.width, LINE_SELECTION_DELTA);
        if (bottomRect.contains(x, y))
            return BOTTOM_BORDER;
        return -1;
    }

    public final Diagram getDiagram() {
        return diagram;
    }

    // Overriding methods must call super.setRectangle
    public void setRectangle(Rectangle rect) {
        // do not test for rectangle equality, we want a fit and a repaint
        // anyway.
        repaint(); // old rectangle
        if (diagram.inComputePos()) { // if not called by computePositionData
            if (m_rectangle != null
                    && (rect.width != m_rectangle.width || rect.height != m_rectangle.height))
                diagram.setComputePos(this);
        }

        /*
         * if(!(this instanceof LineLabel || this instanceof SrAttachment || this instanceof Line)){
         * if(!ApplicationDiagram.lockGridAlignment && Grid.getGrid().isActive()) { int x = rect.x;
         * int y = rect.y; Point pt = Grid.getGrid().getClosestGridBoundary(new Point(x, y));
         * 
         * //if the values changed,we need to compensate for the offset if(pt.x != x || pt.y != y){
         * rect.x = pt.x; rect.y = pt.y; } } }
         */

        m_rectangle = rect;

        repaint(); // new rectangle
    }

    public final boolean isSelected() {
        return diagram.isSelected(this);
    }

    public final boolean setSelected(boolean state) {
        return diagram.setSelected(this, state);
    }

    public final int getLineStyle() {
        return lineStyle;
    }

    public final BasicStroke getLineStroke() {
        return lineStrokes[lineStyle];
    }

    public final void setLineStyle(Boolean isHighlight, Boolean isDashStyle) {
        setLineStyle(isHighlight.booleanValue(), isDashStyle.booleanValue());
    }

    public final void setLineStyle(boolean isHighlight, boolean isDashStyle) {
        setLineStyle(isHighlight ? (isDashStyle ? LINE_DASHED_BOLD : LINE_BOLD)
                : (isDashStyle ? LINE_DASHED : LINE_PLAIN));
    }

    public final void setLineStyle(int lineStyle) {
        if (this.lineStyle != lineStyle) {
            this.lineStyle = lineStyle;
            diagram.setComputePos(this); // content rect may change
        }
    }

    public final Color getLineColor() {
        return lineColor;
    }

    public final void setLineColor(Color color) {
        if (lineColor != color) {
            lineColor = color;
            repaint();
        }
    }

    public final Color getFillColor() {
        return fillColor;
    }

    public final void setFillColor(Color color) {
        if (fillColor != color) {
            fillColor = color;
            repaint();
        }
    }

    public final Color getTextColor() {
        return textColor;
    }

    public final void setTextColor(Color color) {
        if (textColor != color) {
            textColor = color;
            repaint();
        }
    }

    public final int getAutoFitMode() {
        return autoFitMode;
    }

    public final void setAutoFitMode(int autoFitMode) {
        if (this.autoFitMode != autoFitMode) {
            this.autoFitMode = autoFitMode;
            if (!(this instanceof Line || this instanceof LineEndZone || this instanceof LineLabel || this instanceof Attachment))
                diagram.setComputePos(this);
        }
    }

    public final boolean isAutoFit() {
        return (autoFitMode != NO_FIT);
    }

    public final void setAutoFit(boolean state) {
        setAutoFitMode(state ? getDefaultFitMode() : NO_FIT);
    }

    public final void repaint() {
        if (m_rectangle != null)
            diagram.fireAreaDamaged(m_rectangle);
    }

    // BEWARE: diagView is null for printing
    // overridden
    public void paint(Graphics g, DiagramView diagView, int drawingMode, int renderingFlags) {
        if (m_shape != null)
            m_shape.paint(g, diagView, this, renderingFlags);
    }

    // overridden
    public void paintSelHandles(Graphics g, DiagramView diagView) {
        Rectangle rect = diagView.zoom(m_rectangle);
        g.setColor(autoFitMode == TOTAL_FIT ? Color.gray : Color.black);
        // Note that handle size is not zoomed
        if (autoFitMode == HEIGHT_FIT) {
            drawHandle(g, rect.x - HANDLE_SIZE / 2, rect.y + rect.height / 2);
            drawHandle(g, rect.x + rect.width + HANDLE_SIZE / 2, rect.y + rect.height / 2);
        } else {
            drawHandle(g, rect.x - HANDLE_SIZE / 2, rect.y - HANDLE_SIZE / 2);
            drawHandle(g, rect.x - HANDLE_SIZE / 2, rect.y + rect.height + HANDLE_SIZE / 2);
            drawHandle(g, rect.x + rect.width + HANDLE_SIZE / 2, rect.y - HANDLE_SIZE / 2);
            drawHandle(g, rect.x + rect.width + HANDLE_SIZE / 2, rect.y + rect.height + HANDLE_SIZE
                    / 2);
        }
    }

    protected final void drawHandle(Graphics g, int x, int y) {
        g.fillRect(x - HANDLE_SIZE / 2, y - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
    }

    // does the graphic component contain the point <x,y> ?
    // overridden
    public boolean contains(DiagramView diagView, int x, int y) {
        if (m_rectangle == null) {
            return false;
        }

        if (m_rectangle.contains(x, y))
            return (m_shape != null ? m_shape.contains(this, x, y) : true);
        return false;
    }

    // return -1 if <x,y> is not on a handle (or the component has no handles,
    // like lines)
    // assume the component is selected
    // overridden
    public int handleAt(DiagramView diagView, int x, int y) {
        if (autoFitMode == TOTAL_FIT)
            return -1;
        int d = diagView.getHandleSize();
        if (x >= m_rectangle.x - d && x <= m_rectangle.x + m_rectangle.width + d
                && y >= m_rectangle.y - d && y <= m_rectangle.y + m_rectangle.height + d) {
            if (autoFitMode == HEIGHT_FIT) {
                int mid = m_rectangle.y + (m_rectangle.height - d) / 2;
                if (y >= mid && y <= mid + d) {
                    if (x <= m_rectangle.x)
                        return CENTER_LEFT;
                    else if (x >= m_rectangle.x + m_rectangle.width)
                        return CENTER_RIGHT;
                }
            } else {
                if (y <= m_rectangle.y) {
                    if (x <= m_rectangle.x)
                        return UPPER_LEFT;
                    else if (x >= m_rectangle.x + m_rectangle.width)
                        return UPPER_RIGHT;
                } else if (y >= m_rectangle.y + m_rectangle.height) {
                    if (x <= m_rectangle.x)
                        return LOWER_LEFT;
                    else if (x >= m_rectangle.x + m_rectangle.width)
                        return LOWER_RIGHT;
                }
            }
        }
        return -1;
    }

    public Rectangle getContentRect() {
        return (m_shape != null ? m_shape.getContentRect(this) : m_rectangle);
    }

    public final Dimension getShapeSize(Dimension contentSize) {
        return (m_shape != null ? m_shape.getShapeSize(this, contentSize) : contentSize);
    }

    // <g> = any screen graphics, only used to get font metrics
    // overridden
    void computePositionData(Graphics g) {
        repaint();
    }

    // overridden
    public Dimension getPreferredSize() {
        return null; // means no preferred size
    }

    // overridden
    public Dimension getMinimumSize() {
        return new Dimension((int) (3 * Diagram.PIXELS_PER_MM), (int) (3 * Diagram.PIXELS_PER_MM));
    }

    // overridden
    public String getToolTipText() {
        return null;
    }

    // overridden (used by Diagram.getSelectedObjects())
    public boolean selectAtCellLevel() {
        return false;
    }

    // overridden
    public int getLayer() {
        return Diagram.LAYER_GRAPHIC;
    }

    // overridden
    public int getDefaultFitMode() {
        return TOTAL_FIT;
    }
}
