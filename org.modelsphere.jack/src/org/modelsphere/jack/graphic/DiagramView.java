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
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.*;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.graphic.tool.ToolButtonGroup;
import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.actions.ZoomAction;

// normally (not necessarily) mounted in a JSrollPane.
public class DiagramView extends JPanel implements Scrollable, MouseListener, MouseMotionListener,
        KeyListener, DiagramListener {

    public static final String ZOOM_FACTOR_PROPERTY = "DefaultZoom"; //NOT LOCALIZABLE, property
    public static final float ZOOM_FACTOR_PROPERTY_DEFAULT = 1.0f;

    public static final float MAXIMUM_ZOOM_FACTOR = 10.0f; // 1000%

    private static final int SCROLL_UNIT_INCR = (int) (Diagram.PIXELS_PER_MM * 10);
    private static final int SCROLL_BLOCK_OVERLAP = (int) (Diagram.PIXELS_PER_MM * 10);
    private static final int DRAG_TRESHOLD = (int) (Diagram.PIXELS_PER_MM * 2);
    private static final Font nullFont = new Font("nullFont", 0, 0); //NOT LOCALIZABLE

    protected Diagram model;
    private boolean processRightMouseButton = true;
    private int drawingMode = GraphicComponent.DRAW_WHOLE;
    private boolean pageBreak = true;
    private float zoomFactor = 1.0f;
    private HashMap fontTable = null; // accumulates the zoomed fonts for the current zoom factor.

    private Tool currentTool = null; // a DiagramView may have a Tool without a ToolGroup.
    private ToolButtonGroup toolGroup = null;
    private Tool[] tools = null;
    private int currentToolInd;
    private int masterToolInd;

    // information taken at mouse pressed to detect that a mouse drag has begun.
    private Tool toolPressed;
    private int xPressed, yPressed;
    private GraphicComponent gcPressed;
    private boolean inDrag;

    private Grid grid;

    private PropertyChangeListener gridChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            initGrid();
        }
    };

    public DiagramView(Diagram model) {
        super(null, true); // no layout manager
        this.model = model;
        setViewSize(zoomFactor);
        //setAutoscrolls(true);
        setToolTipText(""); // register to the tool tip manager
        addMouseListener(this);
        addMouseMotionListener(this);

        MouseWheelListener listener = new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    float count = (float) (getZoomFactor() - (e.getWheelRotation() * 0.10));
                    if (count < 0.02)
                        count = (float) 0.02;
                    setZoomFactor(count);
                    try {
                        ZoomAction actionIn = (ZoomAction) ApplicationContext.getActionStore()
                                .getAction(AbstractActionsStore.ZOOM_IN);
                        ZoomAction actionOut = (ZoomAction) ApplicationContext.getActionStore()
                                .getAction(AbstractActionsStore.ZOOM_OUT);
                        actionIn.updateZoomCombo();
                        actionOut.updateZoomCombo();
                    } catch (Exception ex) {/* do nothing */
                    }
                } else {
                    Rectangle r = getViewRect();
                    if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                        r.y += e.getUnitsToScroll() * 20;
                        scrollRectToVisible(r);
                    } else {
                        r.y += e.getScrollAmount();
                        scrollRectToVisible(r);
                    }
                }
            }
        };

        addMouseWheelListener(listener);

        addKeyListener(this);
        model.addDiagramListener(this);

        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
        options.addPropertyChangeListener(Grid.class, Grid.PROPERTY_HIDE_GRID, gridChangeListener);
        options.addPropertyChangeListener(Grid.class, Grid.PROPERTY_GRID_COLOR, gridChangeListener);
        options.addPropertyChangeListener(Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_X,
                gridChangeListener);
        options.addPropertyChangeListener(Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_Y,
                gridChangeListener);
        options.addPropertyChangeListener(Grid.class, Grid.PROPERTY_PERCENT_OF_CELL,
                gridChangeListener);
        initGrid();

    }

    private void initGrid() {
        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
        boolean showGrid = !options.getPropertyBoolean(Grid.class, Grid.PROPERTY_HIDE_GRID,
                new Boolean(Grid.PROPERTY_HIDE_GRID_DEFAULT));
        boolean damaged = false;
        if (grid != null) {
            grid.flush();
            grid = null;
            damaged = true;
        }
        if (showGrid) {
            grid = new Grid();
            damaged = true;
        }
        if (damaged) {
            repaint();
        }
    }

    public void delete() {
        model.removeDiagramListener(this);
        PropertiesManager.APPLICATION_PROPERTIES_SET.removePrefixPropertyChangeListener(Grid.class,
                Grid.PROPERTY_GRID_ACTIVE, gridChangeListener);
        if (grid != null)
            grid.flush();
    }

    public final Diagram getModel() {
        return model;
    }

    public final ToolButtonGroup getToolGroup() {
        return toolGroup;
    }

    public final void setToolGroup(ToolButtonGroup toolGroup) {
        this.toolGroup = toolGroup;
        tools = toolGroup.getOrigTools(this);
        setMasterTool(toolGroup.getOrigMaster());
        setCurrentTool(masterToolInd);
    }

    public final Tool[] getTools() {
        return tools;
    }

    public final void setTools(Tool[] tools) {
        this.tools = tools;
    }

    public final int getMasterToolInd() {
        return masterToolInd;
    }

    public final void setMasterTool(int ind) {
        masterToolInd = ind;
    }

    public final int getCurrentToolInd() {
        return currentToolInd;
    }

    public final void setCurrentTool(int ind) {
        currentToolInd = ind;
        setCurrentTool(tools[ind]);
    }

    public final Tool getCurrentTool() {
        return currentTool;
    }

    // This overload for setting current tool is used only if the diagramView has no toolGroup.
    public final void setCurrentTool(Tool tool) {
        Tool oldTool = currentTool;
        if (currentTool != null) {
            currentTool.reset();
            toolPressed = null;
        }
        currentTool = tool;

        Cursor curs = (currentTool != null ? currentTool.getCursor() : null);
        setCursor(curs != null ? curs : Cursor.getDefaultCursor());

        if (oldTool != currentTool) {
            if (oldTool != null) {
                oldTool.toolDesactivated();
            }
            if (currentTool != null) {
                currentTool.toolActivated();
            }
        }
    }

    // Called by the tool itself when it completes.
    public final void toolCompleted() {
        toolPressed = null; // prevent the tool from receiving the rest of the mouse pressed event sequence
        if (toolGroup != null)
            toolGroup.setSelectedTool(masterToolInd);
    }

    public final void setProcessRightMouseButton(boolean state) {
        processRightMouseButton = state;
    }

    public final void setDrawingMode(int drawingMode) {
        this.drawingMode = drawingMode;
        repaint();
    }

    public final boolean hasPageBreak() {
        return pageBreak;
    }

    public final void setPageBreak(boolean pageBreak) {
        if (this.pageBreak != pageBreak) {
            this.pageBreak = pageBreak;
            repaint();
        }
    }

    public final float getZoomFactor() {
        return zoomFactor;
    }

    public final void setZoomFactor(float newZoomFactor) {
        if (newZoomFactor > MAXIMUM_ZOOM_FACTOR) {
            newZoomFactor = MAXIMUM_ZOOM_FACTOR;
        }
        if (zoomFactor == newZoomFactor)
            return;
        setViewSize(newZoomFactor);
        boolean enlarge = (newZoomFactor > zoomFactor);
        zoomFactor = newZoomFactor; // must be done after setViewSize
        fontTable = null;
        if (enlarge) {
            GraphicComponent[] selComps = model.getSelectedComponents();
            if (selComps.length > 0) {
                scrollRectToCenter(selComps[0].getRectangle());
            }
        }
    }

    public final float getPanoramaZoomFactor() {
        Rectangle drawingArea = model.getDrawingArea();
        Dimension viewSize = (getParent() instanceof JViewport ? ((JViewport) getParent())
                .getExtentSize() : getSize());
        float factor = Math.min((float) viewSize.width / drawingArea.width, (float) viewSize.height
                / drawingArea.height);
        return Math.min(1.0f, factor);
    }

    public final Rectangle getViewRect() {
        Rectangle viewRect;
        if (getParent() instanceof JViewport)
            viewRect = ((JViewport) getParent()).getViewRect();
        else
            viewRect = new Rectangle(0, 0, getWidth(), getHeight());
        viewRect = unzoom(viewRect);
        GraphicUtil.confineToRect(viewRect, model.getDrawingArea());
        return viewRect;
    }

    // called when drawing area is resized or when zoom factor is changed
    public final void setViewSize(float newZoomFactor) {
        Rectangle drawingArea = model.getDrawingArea();
        if (drawingArea == null) {
            return;
        }

        Dimension viewSize = new Dimension((int) (drawingArea.width * newZoomFactor),
                (int) (drawingArea.height * newZoomFactor));
        setPreferredSize(viewSize);
        Container parent = getParent();
        if (parent != null && parent instanceof JViewport) {
            JViewport viewPort = (JViewport) parent;
            Rectangle viewRect = viewPort.getViewRect();
            float relZoomFactor = newZoomFactor / zoomFactor;
            Point viewPos = new Point((int) (viewRect.x * relZoomFactor),
                    (int) (viewRect.y * relZoomFactor));
            int d = viewPos.x + viewRect.width - viewSize.width;
            if (d > 0) {
                viewPos.x -= d;
                if (viewPos.x < 0)
                    viewPos.x = 0;
            }
            d = viewPos.y + viewRect.height - viewSize.height;
            if (d > 0) {
                viewPos.y -= d;
                if (viewPos.y < 0)
                    viewPos.y = 0;
            }
            viewPort.setViewSize(viewSize);
            viewPort.setViewPosition(viewPos);
            viewPort.getParent().validate();
        }
        repaint();
    }

    // convert to view coordinates before scrolling
    public final void scrollRectToVisible(Rectangle rect) {
        super.scrollRectToVisible(zoom(rect));
    }

    public final void scrollRectToCenter(Rectangle rect) {
        if (!(getParent() instanceof JViewport))
            return;
        JViewport viewPort = (JViewport) getParent();
        Dimension viewSize = viewPort.getExtentSize();
        viewSize.width = (int) (viewSize.width / zoomFactor);
        viewSize.height = (int) (viewSize.height / zoomFactor);
        rect = new Rectangle(rect.x + (rect.width - viewSize.width) / 2, rect.y
                + (rect.height - viewSize.height) / 2, viewSize.width, viewSize.height);
        GraphicUtil.confineToRect(rect, model.getDrawingArea());
        Point viewPos = new Point((int) (rect.x * zoomFactor), (int) (rect.y * zoomFactor));
        viewPort.setViewPosition(viewPos);
    }

    // return handle size in model coordinates
    public final int getHandleSize() {
        return (int) (GraphicComponent.HANDLE_SIZE / zoomFactor);
    }

    // return top and bottom spaces to prevent cell selection
    public final int getTopBottomInsets() {
        return Math.max(4, (int) (GraphicComponent.TOP_BOTTOM_INSETS / zoomFactor));
    }

    // convert a rectangle from model to view coordinates
    public final Rectangle zoom(Rectangle rect) {
        return zoom(rect, zoomFactor);
    }

    // convert a rectangle from view to model coordinates
    public final Rectangle unzoom(Rectangle rect) {
        return zoom(rect, 1 / zoomFactor);
    }

    private final Rectangle zoom(Rectangle rect, float factor) {
        if (factor == 1.0f)
            return rect;
        int x = (int) (rect.x * factor);
        int y = (int) (rect.y * factor);
        int width = (int) ((rect.x + rect.width) * factor) - x;
        int height = (int) ((rect.y + rect.height) * factor) - y;
        return new Rectangle(x, y, width, height);
    }

    // convert a polygon from model to view coordinates
    public final Polygon zoom(Polygon poly) {
        if (zoomFactor == 1.0f)
            return poly;
        Polygon newPoly = new Polygon(poly.xpoints, poly.ypoints, poly.npoints);
        for (int i = newPoly.npoints; --i >= 0;) {
            newPoly.xpoints[i] = (int) (newPoly.xpoints[i] * zoomFactor);
            newPoly.ypoints[i] = (int) (newPoly.ypoints[i] * zoomFactor);
        }
        return newPoly;
    }

    // convert a font from model to view coordinates
    // BEWARE: may return null if resulting font too small.
    public final Font zoom(Graphics g, Font font) {
        if (zoomFactor == 1.0f)
            return font;
        if (fontTable == null)
            fontTable = new HashMap();
        Font newFont = (Font) fontTable.get(font);
        if (newFont == null) { // first time for this font
            FontMetrics fm = g.getFontMetrics(font);
            int size = (int) (font.getSize() * zoomFactor);
            int height = (int) (fm.getHeight() * zoomFactor);
            while (true) {
                if (size < 2) {
                    newFont = nullFont;
                    break;
                }
                newFont = new Font(font.getName(), font.getStyle(), size);
                FontMetrics newFm = g.getFontMetrics(newFont);
                if (newFm.getHeight() <= height)
                    break; // be sure to return a font small enough.
                size--;
            }
            fontTable.put(font, newFont);
        }
        return (newFont == nullFont ? null : newFont);
    }

    public void paintComponent(Graphics g) {
        GraphicUtil.configureGraphics((Graphics2D) g);
        paintBackground(g);
        Rectangle clipRect = g.getClipBounds();
        if (clipRect.isEmpty())
            return;
        int left = (int) (clipRect.x / zoomFactor);
        int right = (int) ((clipRect.x + clipRect.width) / zoomFactor);
        int top = (int) (clipRect.y / zoomFactor);
        int bottom = (int) ((clipRect.y + clipRect.height) / zoomFactor);
        model.paint(g, this, drawingMode, 0, left, right, top, bottom);
        if (drawingMode != GraphicComponent.DRAW_FRAME)
            model.paintSelHandles(g, this, left, right, top, bottom);
        if (currentTool != null)
            currentTool.paint(g);
    }

    private final void paintBackground(Graphics g) {
        Rectangle clipRect = g.getClipBounds();
        Rectangle drawingArea = zoom(model.getDrawingArea());
        if (clipRect.x + clipRect.width > drawingArea.width
                || clipRect.y + clipRect.height > drawingArea.height) {
            // fill with gray the zone outside the drawing area.
            g.setColor(Color.gray);
            g.fillRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);
            g.clipRect(0, 0, drawingArea.width, drawingArea.height);
            clipRect = g.getClipBounds();
        }
        g.setColor(Color.white);
        g.fillRect(clipRect.x, clipRect.y, clipRect.width, clipRect.height);

        //access the grid object (painted in the background, before the 4 layers)
        //paint the grid, if required
        if (grid != null) {
            int left = (int) (clipRect.x / zoomFactor);
            int right = (int) ((clipRect.x + clipRect.width) / zoomFactor);
            int top = (int) (clipRect.y / zoomFactor);
            int bottom = (int) ((clipRect.y + clipRect.height) / zoomFactor);
            grid.paint(g, this, left, top, right, bottom, model.getPageSize());
        }

        // draw the page breaks
        if (pageBreak) {
            g.setColor(Color.gray);
            Dimension pageSize = model.getPageSize();
            int i = pageSize.width * Math.max(1, (int) (clipRect.x / zoomFactor) / pageSize.width);
            for (;; i += pageSize.width) {
                int pos = (int) (i * zoomFactor);
                if (pos > clipRect.x + clipRect.width)
                    break;
                g.drawLine(pos, clipRect.y, pos, clipRect.y + clipRect.height);
            }
            i = pageSize.height * Math.max(1, (int) (clipRect.y / zoomFactor) / pageSize.height);
            for (;; i += pageSize.height) {
                int pos = (int) (i * zoomFactor);
                if (pos > clipRect.y + clipRect.height)
                    break;
                g.drawLine(clipRect.x, pos, clipRect.x + clipRect.width, pos);
            }
        }
    }

    public final String getToolTipText(MouseEvent e) {
        if (zoomFactor >= 1.0f)
            return null;
        GraphicComponent gc = model.graphicAt(this, (int) (e.getX() / zoomFactor),
                (int) (e.getY() / zoomFactor), 1 << Diagram.LAYER_GRAPHIC, false);
        return (gc != null ? gc.getToolTipText() : null);
    }

    ///////////////////////////////////
    // Scrollable SUPPORT
    // We just need to override the unit increment and the block increment.
    //
    public final int getScrollableUnitIncrement(Rectangle visibleRect, int orientation,
            int direction) {
        return SCROLL_UNIT_INCR;
    }

    public final int getScrollableBlockIncrement(Rectangle visibleRect, int orientation,
            int direction) {
        int incr = (orientation == JScrollBar.VERTICAL ? visibleRect.height : visibleRect.width)
                - SCROLL_BLOCK_OVERLAP;
        if (incr < SCROLL_UNIT_INCR)
            incr = SCROLL_UNIT_INCR;
        return incr;
    }

    public final Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public final boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public final boolean getScrollableTracksViewportHeight() {
        return false;
    }

    //
    // end of Scrollable SUPPORT
    //////////////////////////////////////////////////

    ///////////////////////////////////
    // MouseListener/MouseMotionListener/KeyListener SUPPORT
    // Convert the mouse pointer to model coordinates.
    //
    public final void mouseClicked(MouseEvent e) {
        if (currentTool != null && currentTool == toolPressed)
            currentTool.mouseClicked(unzoom(e));
    }

    public final void mousePressed(MouseEvent e) {
        boolean bMultipleSelection = false;
        Object[] objs = ApplicationContext.getFocusManager().getSelectedObjects();
        if (objs != null)
            if (objs.length > 1)
                bMultipleSelection = true;
        model.removeEditor(CellEditor.LOST_FOCUS_ENDING);
        if (!bMultipleSelection)
            requestFocus();
        if (toolGroup != null)
            toolGroup.setDiagramView(this);
        toolPressed = null;
        xPressed = e.getX();
        yPressed = e.getY();
        inDrag = false;
        if (e.isPopupTrigger() && processRightMouseButton) { // select the cell clicked for drag & drop or popup menu
            int x = (int) (xPressed / zoomFactor);
            int y = (int) (yPressed / zoomFactor);
            gcPressed = model.graphicAt(this, x, y, 0xffffffff, false);
            if (gcPressed != null
                    && !(gcPressed instanceof Line)
                    && !gcPressed.getSelectionAreaRectangle().contains(xPressed / zoomFactor,
                            yPressed / zoomFactor))
                gcPressed = null;
            if (gcPressed != null) {
                if (gcPressed instanceof ZoneBox) {
                    ZoneBox box = (ZoneBox) gcPressed;
                    CellID cellID = box.cellAt(this, x, y);
                    if (cellID != null && !box.isCellSelected(cellID) && !bMultipleSelection) {
                        model.deselectAll();
                        box.selectCell(cellID, true);
                        box.setSelected(true);
                    }
                }
                if (!gcPressed.isSelected() && !bMultipleSelection) {
                    model.deselectAll();
                    gcPressed.setSelected(true);
                }
            }
            //if (gcPressed == null)
            //  model.deselectAll();
            Point ptClicked = new Point((int) (xPressed / zoomFactor),
                    (int) (yPressed / zoomFactor));
            JPopupMenu popup = model.getPopupMenu(ptClicked, gcPressed);
            if (popup != null)
                popup.show(this, xPressed, yPressed);
        } else if (SwingUtilities.isLeftMouseButton(e) && currentTool != null) {
            toolPressed = currentTool;
            currentTool.mousePressed(unzoom(e));
        }
    }

    public final void mouseReleased(MouseEvent e) {
        boolean bMultipleSelection = false;
        Object[] objs = ApplicationContext.getFocusManager().getSelectedObjects();
        if (objs != null)
            if (objs.length > 1)
                bMultipleSelection = true;
        xPressed = e.getX();
        yPressed = e.getY();
        inDrag = false;
        if (e.isPopupTrigger() && processRightMouseButton) { // select the cell clicked for drag & drop or popup menu
            int x = (int) (xPressed / zoomFactor);
            int y = (int) (yPressed / zoomFactor);
            gcPressed = model.graphicAt(this, x, y, 0xffffffff, false);
            if (gcPressed != null
                    && !(gcPressed instanceof Line)
                    && !gcPressed.getSelectionAreaRectangle().contains(xPressed / zoomFactor,
                            yPressed / zoomFactor))
                gcPressed = null;
            if (gcPressed != null) {
                if (gcPressed instanceof ZoneBox) {
                    ZoneBox box = (ZoneBox) gcPressed;
                    CellID cellID = box.cellAt(this, x, y);
                    if (cellID != null && !box.isCellSelected(cellID) && !bMultipleSelection) {
                        model.deselectAll();
                        box.selectCell(cellID, true);
                        box.setSelected(true);
                    }
                }
                if (!gcPressed.isSelected() && !bMultipleSelection) {
                    model.deselectAll();
                    gcPressed.setSelected(true);
                }
            }
            if (gcPressed == null && e.getButton() == MouseEvent.BUTTON1)
                model.deselectAll();
            Point ptClicked = new Point((int) (xPressed / zoomFactor),
                    (int) (yPressed / zoomFactor));
            JPopupMenu popup = model.getPopupMenu(ptClicked, gcPressed);
            if (popup != null)
                popup.show(this, xPressed, yPressed);
        }
        if (currentTool != null && currentTool == toolPressed)
            currentTool.mouseReleased(unzoom(e));
    }

    public final void mouseEntered(MouseEvent e) {
        if (currentTool != null)
            currentTool.mouseEntered(unzoom(e));
    }

    public final void mouseExited(MouseEvent e) {
        if (currentTool != null)
            currentTool.mouseExited(unzoom(e));
    }

    // wait for an initial move of at least a handle size before initiating the drag
    public final void mouseDragged(MouseEvent e) {
        if (!inDrag) {
            if (Math.abs(e.getX() - xPressed) < DRAG_TRESHOLD
                    && Math.abs(e.getY() - yPressed) < DRAG_TRESHOLD)
                return;
            inDrag = true;
        }
        if (currentTool != null && currentTool == toolPressed)
            currentTool.mouseDragged(unzoom(e));
    }

    public final void mouseMoved(MouseEvent e) {
        if (currentTool != null)
            currentTool.mouseMoved(unzoom(e));
    }

    final MouseEvent unzoom(MouseEvent e) {
        if (zoomFactor == 1.0f)
            return e;
        return new MouseEvent(this, e.getID(), e.getWhen(), e.getModifiers(),
                (int) (e.getX() / zoomFactor), (int) (e.getY() / zoomFactor), e.getClickCount(), e
                        .isPopupTrigger());
    }

    public final void keyPressed(KeyEvent e) {
        if (currentTool != null)
            currentTool.keyPressed(e);
    }

    public final void keyReleased(KeyEvent e) {
        if (currentTool != null)
            currentTool.keyReleased(e);
    }

    public final void keyTyped(KeyEvent e) {
        if (currentTool != null)
            currentTool.keyTyped(e);
    }

    //
    // end of MouseListener/MouseMotionListener/KeyListener SUPPORT
    //////////////////////////////////////////////////

    ///////////////////////////////////
    // DiagramListener SUPPORT
    //
    public final void drawingAreaResized() {
        setViewSize(zoomFactor);
    }

    public final void areaDamaged(Rectangle rect) {
        if (rect == null)
            repaint();
        else {
            if (zoomFactor == 1.0f)
                rect = new Rectangle(rect); // take a copy of the rect before modifying it
            else
                rect = zoom(rect);
            rect.width++; // drawRect covers one pixel more
            rect.height++;
            rect.grow(GraphicComponent.HANDLE_SIZE, GraphicComponent.HANDLE_SIZE);
            repaint(rect);
        }
    }

    public final void paintSelHandles(GraphicComponent gc) {
        if (drawingMode == GraphicComponent.DRAW_FRAME)
            return;
        if (true) { // repaint instead of XOR-draw because of a bug in JDK 1.2.2
            areaDamaged(gc.getRectangle());
        } else {
            // Check first that the graphic component is in the visible part of the drawing area
            Rectangle rect = new Rectangle(gc.getRectangle());
            rect = zoom(rect);
            rect.grow(GraphicComponent.HANDLE_SIZE, GraphicComponent.HANDLE_SIZE);
            Rectangle visibleRect = getVisibleRect();
            if (!rect.intersects(visibleRect))
                return;
            Graphics g = getGraphics();
            if (g != null) {
                //        g.setXORMode(Color.white);
                gc.paintSelHandles(g, this);
                g.dispose();
            }
        }
    }

    public final void diagramDeleted() {
        delete();
    }

    public final Grid getGrid() {
        return grid;
    }

}
