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

package org.modelsphere.jack.graphic.tool;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Enumeration;

import javax.swing.KeyStroke;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;

public abstract class SelectTool extends Tool {
    private static final String HELP_NOSELECTION = LocaleMgr.misc.getString("HelpSelect0");
    private static final String HELP_SELECTION = LocaleMgr.misc.getString("HelpSelect1");
    private static final String HELP_SHIFT = LocaleMgr.misc.getString("HelpSelect2");
    private static final String HELP_RESIZE_AUTOFIT_ON = LocaleMgr.misc.getString("HelpSelect4");
    private static final String HELP_RESIZE_AUTOFIT_OFF = LocaleMgr.misc.getString("HelpSelect3");

    public static final int SELECT = 1;
    public static final int MOVE = 2;
    public static final int RESIZE = 3;
    public static final int RESHAPE_LINE = 4;
    public static final int CELL_CREATION = 5;

    private int operation;
    private int xPressed, yPressed;
    private int xDragged, yDragged;
    private boolean inMergeSituation = false; // indicates a merge possible situation (change cursor)

    // move operation using arrow keys
    private int keyMoveX, keyMoveY;
    private Rectangle keyMoveRect;
    private boolean shiftPressed = false;

    private boolean inDrag = false; // indicates a drag operation
    private boolean dragDrawn = false; // indicates that the drag drawing is currently shown
    private GraphicComponent clickComp;
    private int handle; // (RESIZE)
    private Rectangle moveRect; // rectangle delimiting the selected components (MOVE)
    private int lineInd; // index of the point clicked or segment clicked (RESHAPE_LINE)
    private boolean onPoint; // true if click on a point, false if click on a segment (RESHAPE_LINE)
    private boolean onEndPoint; // true if click on an end point; onPoint also true (RESHAPE_LINE)
    private boolean startHorz; // true if first segment of right-angle line is horizontal (RESHAPE_LINE)
    private Rectangle dragRect;
    private CellID editCellID; // cell that will be edited at mouse released if no drag
    private Polygon linePolygon; // Drag line operation - initial polygon

    private Cursor oldCursor;
    private Cursor mergeCursor = AwtUtil.loadCursor(SelectTool.class, "resources/mergecursor.gif",
            new Point(8, 8), "merge", false); //NOT LOCALIZABLE

    private BufferedImage moveImage;

    private static boolean snapToGridEnable;

    private static PropertyChangeListener snapToGridListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            initStapToGrid();
        }
    };

    public SelectTool(int userId, String name, Image icon) {
        super(userId, name, icon);
    }

    static {
        initStapToGrid();
        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
        if (options != null) {
            options.addPropertyChangeListener(Grid.class, Grid.PROPERTY_GRID_ACTIVE,
                    snapToGridListener);
        }

    }

    private static void initStapToGrid() {
        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
        if (options != null) {
            snapToGridEnable = options.getPropertyBoolean(Grid.class, Grid.PROPERTY_GRID_ACTIVE,
                    Grid.PROPERTY_GRID_ACTIVE_DEFAULT);
        }
    }

    protected CellID getEditCellID() {
        return editCellID;
    }

    protected abstract void translateComponents(GraphicComponent[] gcs, int dx, int dy);

    protected abstract void resizeComponent(GraphicComponent gc, Rectangle rect);

    protected abstract boolean isMergeAllowed(int x, int y);

    protected abstract void doMerge(int x, int y);

    protected abstract void setLinePoly(Line line, Polygon poly);

    protected abstract void setLineNode(Line line, GraphicNode node, Polygon poly, int where);

    public void mousePressed(MouseEvent e) {
        boolean bMultipleSelection = false;
        Object[] objs = ApplicationContext.getFocusManager().getSelectedObjects();
        if (objs != null)
            if (objs.length > 1)
                bMultipleSelection = true;
        xPressed = e.getX();
        yPressed = e.getY();
        inDrag = false;
        dragDrawn = false;
        editCellID = null;
        if (!model.getDrawingArea().contains(xPressed, yPressed)) {
            view.toolCompleted();
            return;
        }
        clickComp = model.graphicAt(view, xPressed, yPressed, 0xffffffff, true);
        if (clickComp != null && !(clickComp instanceof Line)
                && !clickComp.getSelectionAreaRectangle().contains(xPressed, yPressed)
                && clickComp.handleAt(view, xPressed, yPressed) == -1)
            clickComp = null;
        if (clickComp == null) {
            operation = SELECT;
            if ((e.getModifiers() & (InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK)) == 0)
                model.deselectAll();
        } else {
            if (clickComp instanceof Line)
                if (bMultipleSelection)
                    operation = MOVE;
                else {
                    Line line = (Line) clickComp;
                    if (line.getNode1() == null && line.getNode2() == null) {
                        if (e.isControlDown() || e.isShiftDown() || e.isAltDown())
                            operation = MOVE;
                        else
                            operation = RESHAPE_LINE;
                    } else
                        operation = RESHAPE_LINE;
                }
            else
                operation = MOVE;
            // do nothing on CTRL if no cell
            if (((e.getModifiers() & InputEvent.CTRL_MASK) != 0) && (clickComp instanceof ZoneBox)) {
                CellID cellID = ((ZoneBox) clickComp).cellAt(view, xPressed, yPressed);
                if (cellID == null) {
                    view.toolCompleted();
                    return;
                }
            }
            if (!clickComp.isSelected()) {
                if ((e.getModifiers() & (InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK)) == 0)
                    model.deselectAll();
                // skip cell selection on SHIFT
                if (clickComp instanceof ZoneBox
                        && ((e.getModifiers() & InputEvent.SHIFT_MASK) == 0)) {
                    // If click on a cell, select the cell before selecting the
                    // component, to have the cell selection frame drawn.
                    ZoneBox box = (ZoneBox) clickComp;
                    CellID cellID = box.cellAt(view, xPressed, yPressed);
                    if (cellID != null)
                        box.selectCell(cellID, true);
                }
                clickComp.setSelected(true);
            } else {
                handle = clickComp.handleAt(view, xPressed, yPressed);
                if (handle != -1) {
                    operation = RESIZE;
                } else if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {
                    clickComp.setSelected(false);
                    view.toolCompleted();
                } else if (clickComp instanceof ZoneBox) {
                    ZoneBox box = (ZoneBox) clickComp;
                    CellID cellID = box.cellAt(view, xPressed, yPressed);
                    if (cellID != null && ((e.getModifiers() & InputEvent.CTRL_MASK) != 0)
                            && box.isCellSelected(cellID)) {
                        box.selectCell(cellID, false);
                        box.repaint();
                    } else if (cellID != null) {
                        if (box.isCellSelected(cellID) && !bMultipleSelection) {
                            model.deselectAll();
                            box.setSelected(true);
                            box.selectCell(cellID, true);
                            editCellID = cellID;
                            box.repaint();
                        } else {
                            if ((e.getModifiers() & InputEvent.CTRL_MASK) != 0) {
                                box.selectCell(cellID, true);
                                box.repaint();
                            } else if (!bMultipleSelection) {
                                model.deselectAll();
                                box.selectCell(cellID, true);
                                box.setSelected(true);
                            }
                        }
                    }
                }
            }
        }
        if (operation == MOVE) {
            initMoveImage();
        }
    }

    private Rectangle computeSelectionRect() {
        GraphicComponent[] selectedComponents = view.getModel().getSelectedComponents();
        if (selectedComponents == null)
            return null;
        Rectangle rect = null;
        for (int i = 0; i < selectedComponents.length; i++) {
            if (!(selectedComponents[i] instanceof Line)) {
                if (rect == null)
                    rect = selectedComponents[i].getRectangle();
                else
                    rect = rect.union(selectedComponents[i].getRectangle());
            }
        }
        return rect;
    }

    private void initMoveImage() {
        GraphicComponent[] selectedComponents = view.getModel().getSelectedComponents();
        boolean drawImage = false;
        for (int i = 0; i < selectedComponents.length; i++) {
            if (!(selectedComponents[i] instanceof Line)) {
                drawImage = true;
                break;
            }
        }
        if (drawImage) {
            if (moveImage != null) {
                moveImage.flush();
                moveImage = null;
            }
            view.getModel().sort(selectedComponents);
            Rectangle rect = computeSelectionRect();
            rect = view.zoom(rect);
            moveImage = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = moveImage.createGraphics();
            GraphicUtil.configureGraphics(g2);
            g2.translate(-rect.x, -rect.y);
            g2.setClip(rect);
            for (int i = 0; i < selectedComponents.length; i++) {
                if (selectedComponents[i] instanceof Line) {
                    Line line = (Line) selectedComponents[i];
                    GraphicNode node1 = line.getNode1();
                    GraphicNode node2 = line.getNode2();
                    if (node1 != null && node2 != null) {
                        boolean node1InSelection = false;
                        boolean node2InSelection = false;
                        for (int j = 0; j < selectedComponents.length; j++) {
                            if (selectedComponents[j] == node1) {
                                node1InSelection = true;
                            }
                            if (selectedComponents[j] == node2) {
                                node2InSelection = true;
                            }
                            if (node1InSelection && node2InSelection)
                                break;
                        }
                        if (node1InSelection && node2InSelection)
                            selectedComponents[i].paint(g2, view, 0, 0);
                    }

                } else {
                    selectedComponents[i].paint(g2, view, 0, 0);
                }
            }
            g2.dispose();

        } else {
            if (moveImage != null) {
                moveImage.flush();
                moveImage = null;
            }
        }
    }

    public final void mouseDragged(MouseEvent e) {
        if (!inDrag) {
            if (!initDrag()) {
                view.toolCompleted();
                return;
            }
            inDrag = true;
        }
        this.getDiagramView().areaDamaged(dragRect);
        dragDrawn = false;
        Rectangle drawingArea = model.getDrawingArea();
        int oldXDragged = xDragged;
        int oldYDragged = yDragged;
        xDragged = GraphicUtil.confineXToRect(e.getX(), drawingArea);
        yDragged = GraphicUtil.confineYToRect(e.getY(), drawingArea);

        if (isMergeAllowed(xDragged, yDragged)) {
            if (!inMergeSituation) {
                inMergeSituation = true;
                oldCursor = view.getCursor();
                view.setCursor(mergeCursor);
            }
        } else if (inMergeSituation) {
            view.setCursor(oldCursor);
            inMergeSituation = false;
        }

        if (!scrollToVisible(xDragged, yDragged)) {
            switch (operation) {
            case SelectTool.SELECT:
                computeSelectDrag();
                break;
            case SelectTool.MOVE:
                computeMoveDrag();
                break;
            case SelectTool.RESIZE:
                computeResizeDrag();
                break;
            case SelectTool.RESHAPE_LINE:
                break;
            }
            dragDrawn = true;
            this.getDiagramView().areaDamaged(dragRect);
        }
        Rectangle rect = new Rectangle();
        rect.x = Math.min(oldXDragged, xDragged);
        rect.y = Math.min(oldYDragged, yDragged);
        rect.width = Math.abs(oldXDragged - xDragged);
        rect.height = Math.abs(oldYDragged - yDragged);
        // remove any garbage left by the dragging gesture
        this.getDiagramView().areaDamaged(rect);
        if (clickComp != null)
            this.clickComp.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        if (dragDrawn) { // false if no drag operation or mouse released while scrolling
            reset();
            switch (operation) {
            case SelectTool.SELECT:
                doSelect();
                break;
            case SelectTool.MOVE:
                doMove();
                break;
            case SelectTool.RESIZE:
                doResize();
                break;
            case SelectTool.RESHAPE_LINE:
                doReshapeLine();
                break;
            }
        } else if (editCellID != null && clickComp.isValid() && !inDrag) {
            model.setEditor(view, (ZoneBox) clickComp, editCellID);
        }
        view.toolCompleted();
        updateHelp();
    }

    public final void paint(Graphics g) {
        if (!dragDrawn)
            return;
        if (inMergeSituation)
            g.setColor(Color.BLACK);
        else
            g.setColor(Color.red);
        if (operation == RESHAPE_LINE)
            drawReshapeLine(g);
        else {
            Rectangle rect = null;
            if (keyMoveRect != null) {
                rect = keyMoveRect;
            } else if (dragRect != null) {
                rect = dragRect;
            }
            rect = view.zoom(rect);
            if (moveImage != null && rect != null) {
                Composite composite = ((Graphics2D) g).getComposite();
                ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        inMergeSituation ? 0.10f : 0.75f));

                g.drawImage(moveImage, rect.x, rect.y, null);
                ((Graphics2D) g).setComposite(composite);
            }
            if (rect != null)
                g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    public final void reset() {
        if (dragDrawn) {
            dragDrawn = false;
            if (dragRect != null)
                view.areaDamaged(dragRect);
            if (moveRect != null)
                view.areaDamaged(moveRect);
            if (linePolygon != null) {
                Polygon currentPolygon = new Polygon(linePolygon.xpoints, linePolygon.ypoints,
                        linePolygon.npoints);
                currentPolygon.addPoint(xDragged, yDragged);
                view.areaDamaged(currentPolygon.getBounds());
                linePolygon = null;
            }
        }
        if (moveImage != null) {
            moveImage.flush();
            moveImage = null;
        }
        if (keyMoveRect != null) {
            view.areaDamaged(keyMoveRect);
            keyMoveRect = null;
        }
    }

    private long firstWhen = 0;
    private int lastKey = 0;

    public final void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            reset();
            view.toolCompleted();
        } else if (e.isShiftDown()) {
            if (!shiftPressed) {
                shiftPressed = true;
                updateHelp();
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT
                    || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (keyMoveRect == null) {
                    keyMoveRect = computeSelectionRect();
                    if (keyMoveRect != null) {
                        firstWhen = e.getWhen();
                        lastKey = e.getKeyCode();
                        keyMoveX = keyMoveRect.x;
                        keyMoveY = keyMoveRect.y;
                        dragDrawn = true;
                        initMoveImage();
                    }
                }
                if (keyMoveRect != null && dragDrawn) {
                    getDiagramView().areaDamaged(keyMoveRect);

                    long when = e.getWhen();
                    int increament = 1;

                    Rectangle newRect = new Rectangle(keyMoveRect.x, keyMoveRect.y,
                            keyMoveRect.width, keyMoveRect.height);

                    if (lastKey == e.getKeyCode()) {
                        if ((when - firstWhen) > 1000) {
                            increament = 2;
                        } else if ((when - firstWhen) > 2000) {
                            increament = 3;
                        }
                    }

                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        newRect.x = newRect.x + increament;
                    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        newRect.x = newRect.x - increament;
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        newRect.y = newRect.y - increament;
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        newRect.y = newRect.y + increament;
                    }
                    lastKey = e.getKeyCode();

                    GraphicUtil.confineToRect(newRect, model.getDrawingArea());
                    int scrollToX = newRect.x;
                    int scrollToY = newRect.y;
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        scrollToX += newRect.width;
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        scrollToY += newRect.height;
                    }
                    scrollToVisible(scrollToX, scrollToY);

                    keyMoveRect = newRect;
                    getDiagramView().areaDamaged(keyMoveRect);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (keyMoveRect != null
                && (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT
                        || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)) {
            doMoveFromKeys();
            keyMoveRect.add(new Point(keyMoveX, keyMoveY));
            this.getDiagramView().areaDamaged(keyMoveRect);
            reset();
            view.toolCompleted();
        }
        if (!e.isShiftDown()) {
            shiftPressed = false;
            updateHelp();
        }
    }

    private final boolean initDrag() {
        boolean ret = true;
        switch (operation) {
        case SelectTool.MOVE:
            moveRect = new Rectangle(clickComp.getRectangle());
            GraphicComponent[] selComps = model.getSelectedComponents();
            for (int i = 0; i < selComps.length; i++) {
                if (selComps[i] instanceof Line) {
                    Line line = (Line) selComps[i]; // free lines are movable
                    if (line.getNode1() != null || line.getNode2() != null)
                        continue;
                }
                moveRect.add(selComps[i].getRectangle());
            }
            break;

        case SelectTool.RESHAPE_LINE:
            Line line = (Line) clickComp;
            linePolygon = line.getPoly();
            lineInd = line.segmentAt(view, xPressed, yPressed);
            onPoint = onEndPoint = false;
            int i = line.pointAt(view, xPressed, yPressed, lineInd);
            if (i != -1) {
                lineInd = i;
                onPoint = true;
                if (i == 0 || i == linePolygon.npoints - 1)
                    onEndPoint = true;
            }
            if (line.isRightAngle())
                startHorz = Line.startHorz(linePolygon);
            break;
        }
        return ret;
    }

    private final void computeSelectDrag() {
        int x, y, width, height;
        if (xPressed < xDragged) {
            x = xPressed;
            width = xDragged - xPressed;
        } else {
            x = xDragged;
            width = xPressed - xDragged;
        }
        if (yPressed < yDragged) {
            y = yPressed;
            height = yDragged - yPressed;
        } else {
            y = yDragged;
            height = yPressed - yDragged;
        }
        dragRect = new Rectangle(x, y, width, height);
    }

    private final void computeMoveDrag() {
        dragRect = new Rectangle(moveRect);
        dragRect.translate(xDragged - xPressed, yDragged - yPressed);
        GraphicUtil.confineToRect(dragRect, model.getDrawingArea());
    }

    private final void computeResizeDrag() {
        dragRect = new Rectangle(clickComp.getRectangle());
        dragRect.width += dragRect.x;
        dragRect.height += dragRect.y;
        Dimension minSize = clickComp.getMinimumSize();
        switch (handle) {
        case GraphicComponent.UPPER_LEFT:
            dragRect.x = Math.min(xDragged, dragRect.width - minSize.width);
            dragRect.y = Math.min(yDragged, dragRect.height - minSize.height);
            break;

        case GraphicComponent.CENTER_LEFT:
            dragRect.x = Math.min(xDragged, dragRect.width - minSize.width);
            break;

        case GraphicComponent.LOWER_LEFT:
            dragRect.x = Math.min(xDragged, dragRect.width - minSize.width);
            dragRect.height = Math.max(yDragged, dragRect.y + minSize.height);
            break;

        case GraphicComponent.UPPER_RIGHT:
            dragRect.width = Math.max(xDragged, dragRect.x + minSize.width);
            dragRect.y = Math.min(yDragged, dragRect.height - minSize.height);
            break;

        case GraphicComponent.CENTER_RIGHT:
            dragRect.width = Math.max(xDragged, dragRect.x + minSize.width);
            break;

        case GraphicComponent.LOWER_RIGHT:
            dragRect.width = Math.max(xDragged, dragRect.x + minSize.width);
            dragRect.height = Math.max(yDragged, dragRect.y + minSize.height);
            break;
        }
        dragRect.width -= dragRect.x;
        dragRect.height -= dragRect.y;
    }

    private final void drawReshapeLine(Graphics g) {
        Polygon poly = ((Line) clickComp).getPoly();
        int i = (onPoint ? lineInd - 1 : lineInd);
        if (i >= 0)
            drawReshapeLineAux(g, poly.xpoints[i], poly.ypoints[i], xDragged, yDragged);
        i = lineInd + 1;
        if (i < poly.npoints)
            drawReshapeLineAux(g, xDragged, yDragged, poly.xpoints[i], poly.ypoints[i]);
    }

    private final void drawReshapeLineAux(Graphics g, int x1, int y1, int x2, int y2) {
        float zoomFactor = view.getZoomFactor();
        x1 = (int) (x1 * zoomFactor);
        y1 = (int) (y1 * zoomFactor);
        x2 = (int) (x2 * zoomFactor);
        y2 = (int) (y2 * zoomFactor);
        if (((Line) clickComp).isRightAngle()) {
            boolean horz = ((lineInd & 1) == 0 ? startHorz : !startHorz);
            int x, y;
            if (horz) {
                x = x2;
                y = y1;
            } else {
                x = x1;
                y = y2;
            }
            g.drawLine(x1, y1, x, y);
            g.drawLine(x, y, x2, y2);
        } else
            g.drawLine(x1, y1, x2, y2);
    }

    private final void doSelect() {
        Rectangle drawingArea = model.getDrawingArea();
        int loX = (dragRect.x <= 0 ? Integer.MIN_VALUE : dragRect.x);
        int loY = (dragRect.y <= 0 ? Integer.MIN_VALUE : dragRect.y);
        int hiX = dragRect.x + dragRect.width;
        if (hiX >= drawingArea.x + drawingArea.width - 1)
            hiX = Integer.MAX_VALUE;
        int hiY = dragRect.y + dragRect.height;
        if (hiY >= drawingArea.y + drawingArea.height - 1)
            hiY = Integer.MAX_VALUE;

        Enumeration enumeration = model.components();
        while (enumeration.hasMoreElements()) {
            GraphicComponent gc = (GraphicComponent) enumeration.nextElement();
            Rectangle rect = gc.getRectangle();
            if (rect.x >= loX && rect.y >= loY && rect.x + rect.width <= hiX
                    && rect.y + rect.height <= hiY)
                gc.setSelected(true);
        }
    }

    // Exclude the lines from the move, except free lines.
    // Exclude the line labels from the move, except if we move a single line label.
    private final void doMove() {
        if (isMergeAllowed(xDragged, yDragged)) {
            doMerge(xDragged, yDragged);
            inMergeSituation = false;
        } else {
            doMoveImpl(true, dragRect.x - moveRect.x, dragRect.y - moveRect.y);
        }
    }

    // Exclude the lines from the move, except free lines.
    // Exclude the line labels from the move, except if we move a single line label.
    private final void doMoveFromKeys() {
        doMoveImpl(false, keyMoveRect.x - keyMoveX, keyMoveRect.y - keyMoveY);
    }

    // Exclude the lines from the move, except free lines.
    // Exclude the line labels from the move, except if we move a single line label.
    private final void doMoveImpl(boolean merge, int xDelta, int yDelta) {
        GraphicComponent[] selComps = model.getSelectedComponents();
        int i, nb;
        Rectangle rect = null;
        for (i = nb = 0; i < selComps.length; i++) {
            if (selComps[i] instanceof Line) {
                Line line = (Line) selComps[i];
                if (line.getNode1() != null || line.getNode2() != null)
                    continue;
            } else if (selComps[i] instanceof LineLabel || selComps[i] instanceof Attachment) {
                if (selComps.length != 1)
                    continue;
            }
            if (!(selComps[i] instanceof Line) && !(selComps[i] instanceof Attachment)
                    && !(selComps[i] instanceof LineLabel)) {
                if (rect == null) {
                    rect = (Rectangle) selComps[i].getRectangle().clone();
                } else {
                    rect.add(selComps[i].getRectangle());
                }
            }
            selComps[nb++] = selComps[i];
        }
        GraphicComponent[] gcs = new GraphicComponent[nb];
        System.arraycopy(selComps, 0, gcs, 0, nb);
        if (rect != null && snapToGrid(view)) {
            // translate to desired location
            rect.translate(xDelta, yDelta);
            // snap
            Rectangle snapedRect = view.getGrid().snapTo(view, rect);
            xDelta += snapedRect.x - rect.x;
            yDelta += snapedRect.y - rect.y;
        }

        translateComponents(gcs, xDelta, yDelta);
    }

    private final void doResize() {
        if (clickComp.isValid())
            resizeComponent(clickComp, dragRect);
    }

    private final void doReshapeLine() {
        if (!clickComp.isValid())
            return;

        Line line = (Line) clickComp;
        if (onEndPoint && !line.isFreeLine()) {
            GraphicComponent gc = model.graphicAt(view, xDragged, yDragged,
                    1 << Diagram.LAYER_GRAPHIC, false);
            if (!(gc instanceof GraphicNode))
                gc = null;
            if (gc != null) {
                Point pt = GraphicUtil.rectangleGetCenter(gc.getRectangle());
                xDragged = pt.x;
                yDragged = pt.y;
            }
            Polygon newPoly = (line.isRightAngle() ? computeNewRightAnglePoly() : computeNewPoly());
            if (newPoly != null)
                setLineNode(line, (GraphicNode) gc, newPoly, (lineInd == 0 ? Line.AT_END1
                        : Line.AT_END2));
        } else {
            Polygon newPoly = (line.isRightAngle() ? computeNewRightAnglePoly() : computeNewPoly());
            if (newPoly != null)
                setLinePoly(line, newPoly);
        }
    }

    private final Polygon computeNewPoly() {
        Polygon poly = ((Line) clickComp).getPoly();
        int ind = lineInd;
        int nb = poly.npoints;
        int[] xs = new int[nb + 1]; // one more in case of insertion
        int[] ys = new int[nb + 1];
        System.arraycopy(poly.xpoints, 0, xs, 0, ind + 1);
        System.arraycopy(poly.ypoints, 0, ys, 0, ind + 1);
        boolean samePoint = false;
        int i;
        if (!onEndPoint) {
            int delta = view.getHandleSize();
            i = (onPoint ? ind - 1 : ind);
            samePoint = (GraphicUtil.samePoint(xDragged, yDragged, poly.xpoints[i],
                    poly.ypoints[i], delta) || GraphicUtil.samePoint(xDragged, yDragged,
                    poly.xpoints[ind + 1], poly.ypoints[ind + 1], delta));
        }
        if (samePoint) {
            if (!onPoint)
                return null;
            // existing point dragged on the previous or next point: remove the point
            nb--;
            System.arraycopy(poly.xpoints, ind + 1, xs, ind, nb - ind);
            System.arraycopy(poly.ypoints, ind + 1, ys, ind, nb - ind);
        } else {
            // onPoint true = drag from a point: change the point;
            //         false = drag somewhere on a segment: insert a new point.
            i = ind;
            if (!onPoint) {
                i++;
                nb++;
            }
            System.arraycopy(poly.xpoints, ind + 1, xs, i + 1, poly.npoints - ind - 1);
            System.arraycopy(poly.ypoints, ind + 1, ys, i + 1, poly.npoints - ind - 1);
            xs[i] = xDragged;
            ys[i] = yDragged;
        }
        return new Polygon(xs, ys, nb);
    }

    private final Polygon computeNewRightAnglePoly() {
        Polygon poly = ((Line) clickComp).getPoly();
        int ind = lineInd;
        int nb = poly.npoints;
        int[] xs = new int[nb + 3]; // room for adding 3 points
        int[] ys = new int[nb + 3];
        if (onPoint) {
            // Point moved; the 2 adjacent points are constrained by the point moved;
            // if an adjacent point is an end point, duplicate it (an end point cannot be modified).
            if (ind == 1) { // duplicate the first point.
                System.arraycopy(poly.xpoints, 0, xs, 1, nb);
                System.arraycopy(poly.ypoints, 0, ys, 1, nb);
                xs[0] = xs[1];
                ys[0] = ys[1];
                nb++;
                ind++;
                startHorz = !startHorz;
            } else {
                System.arraycopy(poly.xpoints, 0, xs, 0, nb);
                System.arraycopy(poly.ypoints, 0, ys, 0, nb);
            }
            if (ind == nb - 2) { // duplicate the last point
                xs[nb] = xs[nb - 1];
                ys[nb] = ys[nb - 1];
                nb++;
            }
        } else {
            // Drag on a segment: add 2 points; the 2nd one is the drag point; the 1st one is constrained
            // by the before point and the drag point; finally the after point is constrained by the drag point.
            // If the after point is the end point, duplicate it (3 points added in this case).
            System.arraycopy(poly.xpoints, 0, xs, 0, ind + 1);
            System.arraycopy(poly.ypoints, 0, ys, 0, ind + 1);
            System.arraycopy(poly.xpoints, ind + 1, xs, ind + 3, nb - ind - 1);
            System.arraycopy(poly.ypoints, ind + 1, ys, ind + 3, nb - ind - 1);
            xs[ind + 1] = xs[ind];
            ys[ind + 1] = ys[ind];
            ind += 2;
            nb += 2;
            if (ind == nb - 2) {
                xs[nb] = xs[nb - 1];
                ys[nb] = ys[nb - 1];
                nb++;
            }
        }

        // Constrain the 2 points adjacent to the drag point (3 points modified).
        xs[ind] = xDragged;
        ys[ind] = yDragged;
        if ((ind & 1) == 0 ? startHorz : !startHorz) {
            if (ind > 0)
                xs[ind - 1] = xDragged;
            if (ind < nb - 1)
                ys[ind + 1] = yDragged;
        } else {
            if (ind < nb - 1)
                xs[ind + 1] = xDragged;
            if (ind > 0)
                ys[ind - 1] = yDragged;
        }

        // Remove overlapping points; look at 5 points:
        // the 3 modified points and the 2 unmodified points adjacent to these 3 points.
        nb = mergeRightAnglePoints(xs, ys, nb, Math.max(0, ind - 2), Math.min(nb - 1, ind + 2));
        return new Polygon(xs, ys, nb);
    }

    // Scan points iFirst to iLast, to remove overlapping points.
    private final int mergeRightAnglePoints(int[] xs, int[] ys, int nb, int iFirst, int iLast) {
        int delta = view.getHandleSize();
        for (int i = iFirst; i < iLast && nb > 3;) {
            if (!GraphicUtil.samePoint(xs[i], ys[i], xs[i + 1], ys[i + 1], delta)) {
                i++;
                continue;
            }
            /*
             * Two points overlap: there are 3 cases: 1) First 2 points: remove the second point,
             * and constrain the 3rd point to the 1st one. 2) Last 2 points: remove the before
             * point, and constraint the 2nd before point to the last one. 3) Middle 2 points:
             * remove both points and constrain the 2 adjacent points (after point constrained by
             * the before point, or vice-versa if the after point is the end point).
             */
            int iAdjust;
            if (i == 0 || (i == 1 && nb == 4)) {
                System.arraycopy(xs, 2, xs, 1, nb - 2);
                System.arraycopy(ys, 2, ys, 1, nb - 2);
                nb--;
                iLast--;
                startHorz = !startHorz;
                iAdjust = 0;
            } else if (i == nb - 2) {
                xs[i] = xs[i + 1];
                ys[i] = ys[i + 1];
                nb--;
                iLast--;
                iAdjust = i - 1;
            } else {
                System.arraycopy(xs, i + 2, xs, i, nb - i - 2);
                System.arraycopy(ys, i + 2, ys, i, nb - i - 2);
                nb -= 2;
                iLast -= 2;
                iAdjust = i - 1;
            }
            boolean horz = ((iAdjust & 1) == 0 ? startHorz : !startHorz);
            int[] is = (horz ? ys : xs);
            if (iAdjust == nb - 2)
                is[iAdjust] = is[iAdjust + 1];
            else
                is[iAdjust + 1] = is[iAdjust];
        }
        return nb;
    }

    protected static boolean snapToGrid(DiagramView view) {
        return snapToGridEnable && view != null && view.getGrid() != null;
    }

    private void setHelpNoSelection() {
        String shift = KeyEvent.getKeyText(KeyEvent.VK_SHIFT);
        setHelpText(MessageFormat.format(HELP_NOSELECTION, shift));
    }

    private void setHelpSelection() {
        String shift = KeyEvent.getKeyText(KeyEvent.VK_SHIFT);
        boolean autofit = clickComp != null && clickComp.isAutoFit();
        setHelpText(MessageFormat.format(HELP_SELECTION, autofit ? HELP_RESIZE_AUTOFIT_ON
                : HELP_RESIZE_AUTOFIT_OFF, shift));
    }

    private void setHelpShift() {
        setHelpText(HELP_SHIFT);
    }

    public void updateHelp() {
        if (shiftPressed) {
            setHelpShift();
        } else {
            GraphicComponent[] selection = getDiagramView().getModel().getSelectedComponents();
            if (selection != null && selection.length > 0) {
                setHelpSelection();
            } else {
                setHelpNoSelection();
            }
        }
    }

    @Override
    public void toolDesactivated() {
        shiftPressed = false;
    }

}
