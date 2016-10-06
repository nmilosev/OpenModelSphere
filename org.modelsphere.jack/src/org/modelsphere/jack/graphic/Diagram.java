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
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.util.*;

import javax.swing.*;

import org.modelsphere.jack.graphic.zone.CellEditor;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.ComboBoxCellEditor;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.jack.util.SrVector;

public abstract class Diagram implements Pageable, Printable {

    public static final float PIXELS_PER_MM = Toolkit.getDefaultToolkit().getScreenResolution() / 25.4f; // 1 inch = 25.4 mm
    public static final Font defaultFont = new Font("SansSerif", Font.PLAIN, 8); // NOT
    // LOCALIZABLE

    public static final int LAYER_BACKGROUND = 0;
    public static final int LAYER_FREE_GRAPHICS = 1;
    public static final int LAYER_LINE = 2;
    public static final int LAYER_LINE_LABEL = 3;
    public static final int LAYER_GRAPHIC = 4;

    public static final int PAGE_NO_NONE = 0;
    public static final int PAGE_NO_LEFT = 0x01;
    public static final int PAGE_NO_MIDDLE = 0x02;
    public static final int PAGE_NO_RIGHT = 0x04;
    public static final int PAGE_NO_BOTTOM = 0x08;
    public static final int PAGE_NO_MARGIN = (int) (PIXELS_PER_MM * 2);

    // graphic components in 4 layers: 0 background, 1 free graphics, 2 lines, 3
    // line labels, 4 all other graphics
    private SrVector[] layers = new SrVector[] { new SrVector(), new SrVector(), new SrVector(),
            new SrVector(), new SrVector() };
    // selection handles are drawn on top and in XOR mode
    private HashSet selItems = null;
    // graphic components that need to recompute their position data
    private HashSet computePosGraphics = null;
    private HashSet computePosLines = null;

    private PageFormat pageFormat;
    private int printScale;
    private Dimension nbPages;
    private Dimension pageSize;
    private Rectangle drawingArea;
    private int pageNoPos = PAGE_NO_NONE;
    private Font pageNoFont = defaultFont;
    private SrVector diagramListeners = new SrVector();

    private CellEditor currentEditor = null;
    private JComponent editorComp;
    private CellID editorCell = null;

    private SrVector diagramSelectionListeners = new SrVector();
    private boolean selectionChangePosted = false;
    private Runnable selectionChangeRunnable = new Runnable() {
        public void run() {
            selectionChangePosted = false;
            EventObject event = new EventObject(Diagram.this);
            for (int i = diagramSelectionListeners.size(); --i >= 0;) {
                ((DiagramSelectionListener) diagramSelectionListeners.elementAt(i))
                        .selectionChanged(event);
            }
        }
    };

    // Constructor
    public Diagram() {
    }

    public abstract DiagramView getMainView();

    // BEWARE: overriding methods must call super.delete as last action
    public void delete() {
        removeAll();
        for (int i = diagramListeners.size(); --i >= 0;) {
            ((DiagramListener) diagramListeners.elementAt(i)).diagramDeleted();
        }
    }

    final void addComponent(GraphicComponent gc) {
        layers[gc.getLayer()].addElement(gc);
    }

    final void removeComponent(GraphicComponent gc) {
        if (editorCell != null && editorCell.zone.getBox() == gc)
            removeEditor(CellEditor.CANCEL);
        layers[gc.getLayer()].removeElement(gc);
        HashSet computePosSet = (gc instanceof Line || gc instanceof Attachment ? computePosLines
                : computePosGraphics);
        if (computePosSet != null)
            computePosSet.remove(gc);
        if (selItems != null) {
            if (selItems.remove(gc))
                fireSelectionChanged();
        }
    }

    public final void removeAll() {
        for (int layer = layers.length; --layer >= 0;) {
            SrVector gcs = layers[layer];
            for (int i = gcs.size(); --i >= 0;)
                ((GraphicComponent) gcs.elementAt(i)).delete(true);
            layers[layer] = new SrVector();
        }
        selItems = null;
        if (computePosGraphics != null)
            computePosGraphics.clear();
        if (computePosLines != null)
            computePosLines.clear();
        fireAreaDamaged(null);
    }

    public final PageFormat getPageFormat() {
        return pageFormat;
    }

    public final int getPrintScale() {
        return printScale;
    }

    public final Rectangle getDrawingArea() {
        return drawingArea;
    }

    public final Dimension getPageSize() {
        return pageSize;
    }

    public static Dimension getPageSize(PageFormat pageFormat, int printScale) {
        if ((pageFormat == null) || (printScale == 0)) {
            return null;
        }

        double width = pageFormat.getImageableWidth() * 100.0 / (double) printScale;
        double height = pageFormat.getImageableHeight() * 100.0 / (double) printScale;
        Dimension dim = new Dimension((int) width, (int) height);
        return dim;
    } // end getPageSize()

    public final Dimension getNbPages() {
        return nbPages;
    }

    public final void setDrawingArea(PageFormat pageFormat, int printScale, Dimension nbPages) {
        this.pageFormat = pageFormat;
        this.printScale = printScale;
        this.nbPages = nbPages;
        pageSize = getPageSize(pageFormat, printScale);
        if (pageSize != null) {
            drawingArea = new Rectangle(0, 0, pageSize.width * nbPages.width, pageSize.height
                    * nbPages.height);
        }

        fireDrawingAreaResized();
    } // end setDrawingArea()

    public final int getPageNoPos() {
        return pageNoPos;
    }

    public final Font getPageNoFont() {
        return pageNoFont;
    }

    public final void setPageNoParams(int pageNoPos, Font pageNoFont) {
        this.pageNoPos = pageNoPos;
        this.pageNoFont = pageNoFont;
        fireAreaDamaged(null);
    }

    public final void addDiagramListener(DiagramListener listener) {
        if (!diagramListeners.contains(listener))
            diagramListeners.addElement(listener);
    }

    public final void removeDiagramListener(DiagramListener listener) {
        diagramListeners.removeElement(listener);
    }

    public final void fireAreaDamaged(Rectangle rect) {
        for (int i = diagramListeners.size(); --i >= 0;) {
            ((DiagramListener) diagramListeners.elementAt(i)).areaDamaged(rect);
        }
    }

    public final void fireDrawingAreaResized() {
        for (int i = diagramListeners.size(); --i >= 0;) {
            ((DiagramListener) diagramListeners.elementAt(i)).drawingAreaResized();
        }
    }

    private final void firePaintSelHandles(GraphicComponent gc) {
        for (int i = diagramListeners.size(); --i >= 0;) {
            ((DiagramListener) diagramListeners.elementAt(i)).paintSelHandles(gc);
        }
    }

    public final void addDiagramSelectionListener(DiagramSelectionListener listener) {
        if (!diagramSelectionListeners.contains(listener))
            diagramSelectionListeners.addElement(listener);
    }

    public final void removeDiagramSelectionListener(DiagramSelectionListener listener) {
        diagramSelectionListeners.removeElement(listener);
    }

    // Postpone the firing of selection listeners in order to fire them only
    // once
    // when modifying the selection a number of times during a single run.
    // Overridden
    public void fireSelectionChanged() {
        if (selectionChangePosted)
            return;
        SwingUtilities.invokeLater(selectionChangeRunnable);
        selectionChangePosted = true;
    }

    public final Enumeration components() {
        return layerEnumeration(0xffffffff);
    }

    // Enumerate from top to bottom.
    public final Enumeration layerEnumeration(final int layerMask) {
        return new Enumeration() {
            private int layer = layers.length - 1;
            private int ind = ((layerMask & 1 << layer) == 0 ? 0 : layers[layer].size());

            public boolean hasMoreElements() {
                if (ind > 0)
                    return true;
                while (true) {
                    if (--layer < 0)
                        return false;
                    if ((layerMask & 1 << layer) == 0)
                        continue;
                    ind = layers[layer].size();
                    if (ind != 0)
                        return true;
                }
            }

            public Object nextElement() {
                ind--;
                return layers[layer].elementAt(ind);
            }
        };
    }

    // Return null if empty diagram.
    public final Rectangle getContentRect() {
        Rectangle contentRect = null;
        Enumeration enumeration = components();
        while (enumeration.hasMoreElements()) {
            GraphicComponent gc = (GraphicComponent) enumeration.nextElement();
            if (contentRect == null)
                contentRect = new Rectangle(gc.getRectangle());
            else
                contentRect.add(gc.getRectangle());
        }
        return contentRect;
    }

    // diagView is null for printing
    // paint all the graphic components whose rectangle intersects the clipping
    // area
    // (grow the graphic component rectangle to take into account bold lines).
    final void paint(Graphics g, DiagramView diagView, int renderingFlags, int drawingMode,
            int left, int right, int top, int bottom) {
        if (pageNoPos != PAGE_NO_NONE && drawingMode != GraphicComponent.DRAW_FRAME)
            paintPageNo(g, diagView, left, right, top, bottom);
        paintAux(g, diagView, drawingMode, renderingFlags, left, right, top, bottom, null);
    }

    public final void paintAux(Graphics g, DiagramView diagView, int drawingMode,
            int renderingFlags, int left, int right, int top, int bottom, HashSet selGcs) {
        int d = GraphicComponent.LINE_BOLD_WIDTH;
        if (diagView != null)
            d = (int) (d / diagView.getZoomFactor());
        left -= d;
        right += d;
        top -= d;
        bottom += d;

        for (int layer = 0; layer < layers.length; layer++) {
            SrVector gcs = layers[layer];
            int i, nb;
            for (i = 0, nb = gcs.size(); i < nb; i++) {
                GraphicComponent gc = (GraphicComponent) gcs.elementAt(i);
                Rectangle rect = gc.getRectangle();
                if (rect == null)
                    continue;
                if (rect.x > right || rect.x + rect.width < left || rect.y > bottom
                        || rect.y + rect.height < top)
                    continue;
                if (selGcs != null && !selGcs.contains(gc))
                    continue;
                gc.paint(g, diagView, drawingMode, renderingFlags);
            } // end for
        } // end for
    } // end paintAux()

    private class GraphicComponentComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            if (o1 == o2) {
                return 0;
            }
            int o1Layer = o1 == null ? -1 : getLayer((GraphicComponent) o1);
            int o2Layer = o2 == null ? -1 : getLayer((GraphicComponent) o2);
            if (o1Layer == -1 && o2Layer == -1)
                return 0;
            if (o1Layer < 0) {
                return Integer.MIN_VALUE;
            }
            if (o2Layer < 0) {
                return Integer.MAX_VALUE;
            }
            if (o1Layer == o2Layer) {
                SrVector gcs = layers[o1Layer];
                return gcs.indexOf(o1) < gcs.indexOf(o2) ? -1 : 1;
            }
            return (o1Layer < o2Layer ? -1 : 1);
        }

    }

    private int getLayer(GraphicComponent gc) {
        int result = -1;
        layerIteration: for (int layer = 0; layer < layers.length; layer++) {
            SrVector gcs = layers[layer];
            int i, nb;
            for (i = 0, nb = gcs.size(); i < nb; i++) {
                if (gcs.contains(gc)) {
                    result = layer;
                    break layerIteration;
                }
            }
        }
        return result;
    }

    public void sort(GraphicComponent[] components) {
        if (components == null || components.length == 1)
            return;
        Arrays.sort(components, new GraphicComponentComparator());
    }

    private void paintPageNo(Graphics g, DiagramView diagView, int left, int right, int top,
            int bottom) {
        int topRow = top / pageSize.height;
        int bottomRow = bottom / pageSize.height;
        int leftCol = left / pageSize.width;
        int rightCol = right / pageSize.width;
        for (int row = topRow; row <= bottomRow; row++) {
            for (int col = leftCol; col <= rightCol; col++) {
                String strPageNo = getStringPageNo(row, col, nbPages);
                Rectangle rect = getPageNoRect(g, strPageNo, row, col);
                if (rect.x > right || rect.x + rect.width < left || rect.y > bottom
                        || rect.y + rect.height < top)
                    continue;
                Font font = pageNoFont;

                Font sampleFont = new Font(font.getName(), font.getStyle(), font.getSize() + 2);
                Toolkit tk = Toolkit.getDefaultToolkit();
                int nScreenFontResolution = tk.getScreenResolution();
                double ratio = (double) nScreenFontResolution / (double) 72;
                double fsize = sampleFont.getSize();
                fsize = (fsize > 0 ? fsize : 1);
                final Font nf = new Font(sampleFont.getFontName(), sampleFont.getStyle(),
                        (int) (fsize * ratio) + 1);
                font = sampleFont;

                if (diagView != null) {
                    rect = diagView.zoom(rect);
                    font = diagView.zoom(g, font);
                    if (font == null)
                        continue;
                }
                g.setFont(font);
                g.setColor(Color.black);
                FontMetrics fm = g.getFontMetrics(font);
                g.drawString(strPageNo, rect.x, rect.y + fm.getAscent());
            }
        }
    }

    protected abstract String getStringPageNo(int row, int col, Dimension nbPages);

    private Rectangle getPageNoRect(Graphics g, String strPageNo, int row, int col) {
        FontMetrics fm = g.getFontMetrics(pageNoFont);
        int width = fm.stringWidth(strPageNo);
        int height = fm.getHeight();
        int x = PAGE_NO_MARGIN;
        if ((pageNoPos & PAGE_NO_MIDDLE) != 0)
            x = (pageSize.width - width) / 2;
        else if ((pageNoPos & PAGE_NO_RIGHT) != 0)
            x = pageSize.width - width - PAGE_NO_MARGIN;
        int y = PAGE_NO_MARGIN;
        if ((pageNoPos & PAGE_NO_BOTTOM) != 0)
            y = pageSize.height - height - PAGE_NO_MARGIN;
        return new Rectangle(x + col * pageSize.width, y + row * pageSize.height, width, height);
    }

    // paint the selection handles of the selected components whose grown
    // rectangle intersects the clipping area.
    // (the grown rectangle is the rectangle grown by a handle size in all
    // directions).
    final void paintSelHandles(Graphics g, DiagramView diagView, int left, int right, int top,
            int bottom) {
        if (selItems == null)
            return;
        int d = diagView.getHandleSize();
        left -= d;
        right += d;
        top -= d;
        bottom += d;
        Iterator iter = selItems.iterator();
        while (iter.hasNext()) {
            GraphicComponent gc = (GraphicComponent) iter.next();
            Rectangle rect = gc.getRectangle();
            if (rect.x > right || rect.x + rect.width < left || rect.y > bottom
                    || rect.y + rect.height < top)
                continue;
            gc.paintSelHandles(g, diagView);
        }
    }

    // Paint the text in the background
    private void paintTextInBackground(Graphics g, DiagramView diagView, int left, int top,
            int right, int bottom, String upperText, String lowerText) {
        // If printing (diagView==null), the graphics objects has already been
        // scaled .. any drawing made on it is automatically scaled
        float zoomFactor = diagView == null ? 1.0f : diagView.getZoomFactor();
        if (diagView != null && zoomFactor < 0.40f) // Too small on screen,
            // unreadable, do not paint
            // unless printing or saving
            // image
            return;
        Graphics2D g2 = (Graphics2D) g;

        Rectangle clipRect = null;

        if (diagView != null) // if painting a Jpanel..
            clipRect = diagView.getVisibleRect();
        else
            // else if painting for printed hardcopy..
            clipRect = new Rectangle(left, top, right - left, bottom - top);

        int textSize = (int) (280 * zoomFactor / upperText.length());
        int addrSize = (int) (13 * zoomFactor);
        int delta = (int) (150 * zoomFactor);
        int offset = (int) (20 * zoomFactor);
        String font = "Courier"; // NOT LOCALIZABLE, font name

        boolean antialias = false;
        Object hint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        if (hint != null && hint.equals(RenderingHints.VALUE_ANTIALIAS_ON))
            antialias = true;
        g2.setColor(Color.lightGray);

        Font font1 = new Font(font, Font.BOLD, textSize);
        FontRenderContext frc1 = new FontRenderContext(null, antialias, false);
        TextLayout textlayout1 = new TextLayout(upperText, font1, frc1);

        Font font2 = new Font(font, Font.PLAIN, addrSize);
        FontRenderContext frc2 = new FontRenderContext(null, antialias, false);
        TextLayout textlayout2 = new TextLayout(lowerText, font2, frc2);
        AffineTransform textAt = new AffineTransform();

        int ax = 0;
        int ay = 0;
        int aw = diagView == null ? right : diagView.getWidth();
        int ah = diagView == null ? bottom : diagView.getHeight();
        double theta = Math.toRadians(-30);

        // Create the pattern
        BufferedImage pattern = new BufferedImage(delta * 2, delta, BufferedImage.TYPE_INT_ARGB);
        Graphics2D patternGraphics = (Graphics2D) pattern.getGraphics();
        patternGraphics.setColor(new Color(0, 0, 0, 0)); // transparent
        patternGraphics.fillRect(0, 0, delta * 2, delta);
        patternGraphics.setColor(Color.lightGray);
        textAt.translate(30 * zoomFactor, delta - (30 * zoomFactor));
        textAt.rotate(theta);
        Shape text1Shape = textlayout1.getOutline(textAt);
        textAt.translate(0, offset);
        Shape text2Shape = textlayout2.getOutline(textAt);
        patternGraphics.draw(text1Shape);
        patternGraphics.draw(text2Shape);

        // Fill the visible rect with the pattern - we must always start at 0, 0
        // of the entire graphics
        // to avoid broken image when scrolling (the only part repainted is the
        // rect of the corrupted graphics -
        // for example the rect that become visible after scrolling). Instead,
        // we verify the intersection of each
        // pattern applied with the visible rectangle.
        // Rectangle intersection = null;
        for (int x = ax; x < aw; x += delta * 2) {
            for (int y = ay; y < ah; y += delta) {
                // evaluate intersection with the visible rectangle before
                // painting the pattern
                // I think that this opperation is performed by the Graphics
                // object because the clipping areas (device and visible)
                // are already set on the Graphics object (any rendering outside
                // the clipping area are certainly ignored).
                // That doesn't seem to impact on performance.

                // intersection = SwingUtilities.computeIntersection(x, y , 2 *
                // delta, delta, (Rectangle)clipRect.clone());
                // if (intersection.width != 0 && intersection.height != 0){
                g2.drawImage(pattern, x, y, delta * 2, delta, null);
                // }
            }
        }
        // Free pattern resources
        pattern.flush();

        /*
         * for (int x=ax; x<aw; x+=delta*2) { for (int y=ay; y<ah; y+=delta) { textAt.translate(x,
         * y); intersection = SwingUtilities.computeIntersection(x - delta, y - delta, 4 * delta, 2
         * * delta, (Rectangle)clipRect.clone()); if (intersection.width != 0 && intersection.height
         * != 0){ textAt.rotate(theta); g2.draw(textlayout1.getOutline(textAt)); textAt.translate(0,
         * offset); g2.draw(textlayout2.getOutline(textAt)); textAt.translate(0, -offset);
         * textAt.rotate(0-theta); } textAt.translate(-x, -y); } //end for y } //end for x
         */
    }

    // Create an image containing a selection of graphic components.
    public final Image createImage(int scale) {
        return createImage(selItems, scale, false);
    }

    // Create an image containing a selection of graphic components.
    public final Image createImage(int scale, boolean transparent) {
        return createImage(selItems, scale, transparent);
    }

    // Create an image containing a selection of graphic components.
    // If selGcs null, create an image for the whole diagram.
    public final Image createImage(HashSet selGcs, int scale) {
        return createImage(selGcs, scale, false);
    }

    // Create an image containing a selection of graphic components.
    // If selGcs null, create an image for the whole diagram.
    public final Image createImage(HashSet selGcs, int scale, boolean transparent) {
        Rectangle rect = null;
        if (selGcs == null)
            rect = getContentRect();
        else {
            Iterator iter = selGcs.iterator();
            while (iter.hasNext()) {
                GraphicComponent gc = (GraphicComponent) iter.next();
                if (rect == null)
                    rect = new Rectangle(gc.getRectangle());
                else
                    rect.add(gc.getRectangle());
            }
        }
        if (rect == null)
            return null;
        // TODO GP: We should not grow the rect, this cause continuous
        // copy-paste of an image to constantly grow.
        // This needs investigation. gc.getRectangle() should be fixed to return
        // the actual
        // real size with consideration for its stroke's line width. But this is
        // tricky to fix as
        // we may have added other weird bad fixes like this in other places in
        // the code.
        // Another quick fix would be to ask the graphics components to provide
        // an additional insets value
        // and grow only if provided. But the best fix is to change
        // getRectangle().
        rect.grow(GraphicComponent.LINE_BOLD_WIDTH, GraphicComponent.LINE_BOLD_WIDTH);
        return createImage(selGcs, rect, scale, transparent);
    }

    public final Image createImage(int pageIndex, int scale) {
        return createImage(pageIndex, scale, false);
    }

    public final Image createImage(int pageIndex, int scale, boolean transparent) {
        int x = pageSize.width * (pageIndex % nbPages.width);
        int y = pageSize.height * (pageIndex / nbPages.width);
        Rectangle rect = new Rectangle(x, y, pageSize.width, pageSize.height);
        return createImage(null, rect, scale, transparent);
    }

    public final Image createImage(HashSet selGcs, Rectangle rect, int scale, boolean transparent) {
        double zoom = scale / 100.0;
        BufferedImage bufImage = null;
        if (transparent) {
            bufImage = new BufferedImage((int) (rect.width * zoom), (int) (rect.height * zoom),
                    BufferedImage.TYPE_INT_ARGB);
        } else {
            bufImage = new BufferedImage((int) (rect.width * zoom), (int) (rect.height * zoom),
                    BufferedImage.TYPE_INT_RGB);
        }

        int renderingFlags = 0;

        Graphics2D g = bufImage.createGraphics();
        GraphicUtil.configureGraphicsForPrinting(g);
        if (transparent) {
            Composite composite = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
            g.fillRect(0, 0, bufImage.getWidth(), bufImage.getHeight());
            g.setComposite(composite);
            renderingFlags |= RenderingFlags.ERASE_BACKGROUND;
        } else {
            g.setColor(Color.white);
            g.fillRect(0, 0, bufImage.getWidth(), bufImage.getHeight());
        }

        ((Graphics2D) g).scale(zoom, zoom);
        g.translate(-rect.x, -rect.y);
        g.setClip(rect.x, rect.y, rect.width, rect.height);

        paintAux(g, null, GraphicComponent.DRAW_WHOLE, renderingFlags, rect.x, rect.x + rect.width,
                rect.y, rect.y + rect.height, selGcs);

        g.dispose();
        return bufImage;
    }

    // return the topmost graphic component at point <x,y> (return null if none)
    // first we look if the point <x,y> is on a selection handle
    // top to bottom order is: handles, graphics, line labels, selected lines,
    // unselected lines
    public final GraphicComponent graphicAt(DiagramView diagView, int x, int y, int layerMask,
            boolean withSel) {
        Line selLine = null;
        if (withSel && selItems != null) {
            Iterator iter = selItems.iterator();
            while (iter.hasNext()) {
                GraphicComponent gc = (GraphicComponent) iter.next();
                if (gc instanceof Line) {
                    int i = ((Line) gc).segmentAt(diagView, x, y);
                    if (i != -1) {
                        selLine = (Line) gc;
                        if (((Line) gc).pointAt(diagView, x, y, i) != -1)
                            return gc;
                    }
                } else if (gc.handleAt(diagView, x, y) != -1)
                    return gc;
            }
        }
        for (int layer = layers.length; --layer >= 0;) {
            if ((layerMask & 1 << layer) == 0)
                continue;
            if (layer == LAYER_LINE && selLine != null)
                return selLine;
            SrVector gcs = layers[layer];
            for (int i = gcs.size(); --i >= 0;) {
                GraphicComponent gc = (GraphicComponent) gcs.elementAt(i);
                if (gc.contains(diagView, x, y))
                    return gc;
            }
        }
        return null;
    }

    public final boolean isSelected(GraphicComponent gc) {
        return (selItems != null && selItems.contains(gc));
    }

    public final boolean setSelected(GraphicComponent gc, boolean state) {
        boolean oldState = isSelected(gc);
        if (oldState != state) {
            // XOR-draw the handles and the select frame of the selected cells.
            // Must be done before deselectAllCells().
            firePaintSelHandles(gc);
            if (state) {
                if (selItems == null)
                    selItems = new HashSet();
                selItems.add(gc);
            } else {
                if (gc instanceof ZoneBox)
                    ((ZoneBox) gc).deselectAllCells();
                selItems.remove(gc);
            }
            fireSelectionChanged();
        }
        return oldState;
    }

    public final void deselectAll() {
        GraphicComponent[] selComps = getSelectedComponents();
        for (int i = 0; i < selComps.length; i++)
            setSelected(selComps[i], false);
        selItems = null;
    }

    public final GraphicComponent[] getSelectedComponents() {
        if (selItems == null)
            return new GraphicComponent[0];
        return (GraphicComponent[]) selItems.toArray(new GraphicComponent[selItems.size()]);
    }

    public final void beginComputePos() {
        computePosGraphics = new HashSet();
        computePosLines = new HashSet();
    }

    public final void setComputePos(GraphicComponent gc) {
        if (computePosLines == null)
            return;

        if (gc instanceof Line || gc instanceof Attachment)
            computePosLines.add(gc);
        else if (gc instanceof LineLabel)
            computePosLines.add(((LineLabel) gc).getLine());
        else
            computePosGraphics.add(gc);
    } // end setComputePos()

    // Compute position data on non-lines first; computing position data on
    // nodes with autofit
    // causes lines and attachments to be added to the computePosLines set.
    // <g> = any screen graphics, only used to get font metrics
    public final void endComputePos(Graphics g) {
        if (computePosGraphics.size() != 0) {
            Iterator iter = computePosGraphics.iterator();
            computePosGraphics = null; // prevents adding of non-lines; adding
            // of lines and attachments is still
            // possible
            while (iter.hasNext()) {
                GraphicComponent gc = (GraphicComponent) iter.next();
                gc.computePositionData(g);
            } // end while
        } // end if
        computePosGraphics = null;

        if (computePosLines.size() != 0) {
            Iterator iter = computePosLines.iterator();
            computePosLines = null; // prevents adding of lines and attachments
            while (iter.hasNext())
                ((GraphicComponent) iter.next()).computePositionData(g);
        }
        computePosLines = null;
    }

    // Return true if between <beginComputePos> and <endComputePos>
    final boolean inComputePos() {
        return (computePosGraphics != null); // endComputePos sets
        // computePosGraphics to null
        // before anything else
    }

    // overridden
    public JPopupMenu getPopupMenu(Point ptClicked, GraphicComponent gcClicked) {
        return null;
    }

    public final void setEditor(DiagramView view, ZoneBox box, CellID cellID) {
        removeEditor(CellEditor.CANCEL); // should not happen
        
        //do not allow in-place editing for model viewer perspective
        if (! ScreenPerspective.isFullVersion()) {
            return;
        }

        Rectangle cellRect = box.getCellRect(cellID);
        if (cellRect == null) // cannot edit the cell if the zone is not
            // displayed
            return;
        cellRect = view.zoom(cellRect);
        CellEditor editor = box.getCellEditor(cellID);
        if (editor == null)
            return;
        editorComp = editor.getComponent(box, cellID, box.getCell(cellID), view, cellRect);
        if (editorComp == null)
            return;
        currentEditor = editor;
        editorCell = cellID;
        view.add(editorComp);
        Dimension size = editorComp.getPreferredSize();
        if (editorComp instanceof JScrollPane) {
            Rectangle visibleArea = view.getVisibleRect();
            // apply constraints to size to 65% of the visible area
            if (size.width > 200 && size.width > visibleArea.width * 0.65) {
                size.width = (int) (visibleArea.width * 0.65);
            }
            if (size.height > 100 && size.height > visibleArea.height * 0.65) {
                size.height = (int) (visibleArea.height * 0.65);
            }
            // try to ensure a minimum editing size
            if (size.width < 200) {
                size.width = 200;
            }
            if (size.height < 100) {
                size.height = 100;
            }

            // limit editor inside the visible area with a margin of 5(use
            // margins to allow click editing stop)
            if (size.width + 10 > visibleArea.width) {
                size.width = visibleArea.width - 10;
            }
            if (size.height + 10 > visibleArea.height) {
                size.height = visibleArea.height - 10;
            }
            // ensure the editor is visible in case the visible area is very
            // small.
            if (size.width < 30) {
                size.width = 30;
            }
            if (size.height < 10) {
                size.height = 10;
            }

            Rectangle editorBounds = new Rectangle(cellRect.x, cellRect.y, size.width, size.height);
            // ensure the editor is fully visible (inside the visible area) by
            // moving x and y equals to
            // the area outside visible area and with respect to margins.
            if (!visibleArea.contains(editorBounds)) {
                if (editorBounds.x < visibleArea.x)
                    editorBounds.x = visibleArea.x + 5;
                else if (editorBounds.x + editorBounds.width > visibleArea.x + visibleArea.width)
                    editorBounds.x = editorBounds.x
                            - (editorBounds.x + editorBounds.width - visibleArea.x - visibleArea.width)
                            - 5;
                if (editorBounds.y < visibleArea.y)
                    editorBounds.y = visibleArea.y + 5;
                else if (editorBounds.y + editorBounds.height > visibleArea.y + visibleArea.height)
                    editorBounds.y = editorBounds.y
                            - (editorBounds.y + editorBounds.height - visibleArea.y - visibleArea.height)
                            - 5;
            }
            editorComp.setBounds(editorBounds);
            editorComp.validate();
            ((JScrollPane) editorComp).getViewport().getView().requestFocus();
        } else {
            editorComp.setBounds(cellRect.x, cellRect.y + (cellRect.height - size.height) / 2,
                    size.width, size.height);
            if (editor instanceof ComboBoxCellEditor) {
                ComboBoxCellEditor dce = (ComboBoxCellEditor) editor;
                if (dce.isEmpty()) {
                    Toolkit.getDefaultToolkit().beep();
                    removeEditor(CellEditor.CANCEL); // should not happen
                } else {
                    editorComp.validate();
                    editorComp.requestFocus();
                    dce.showPopup();
                }
            } else {
                editorComp.validate();
                editorComp.requestFocus();
            }
        }
    }

    public final void removeEditor(int endCode) {
        if (currentEditor == null)
            return;
        DiagramView view = (DiagramView) editorComp.getParent();
        view.remove(editorComp);
        view.repaint(editorComp.getBounds());
        CellEditor editor = currentEditor;
        currentEditor = null;
        editorCell = null;
        // When stopEditing is called, the editor is already removed;
        // this allows stopEditing to start a new edition.
        editor.stopEditing(endCode);
    }

    public final CellID getEditorCell() {
        return editorCell;
    }

    // ////////////////////////////////
    // Printable SUPPORT
    //
    public final int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        int x = pageSize.width * (pageIndex % nbPages.width);
        int y = pageSize.height * (pageIndex / nbPages.width);
        double printZoom = printScale / 100.0;
        g.translate((int) (pageFormat.getImageableX() - x * printZoom), (int) (pageFormat
                .getImageableY() - y * printZoom));
        ((Graphics2D) g).scale(printZoom, printZoom);
        Rectangle clipRect = g.getClipBounds();
        paint(g, null, GraphicComponent.DRAW_WHOLE, 0, clipRect.x, clipRect.x + clipRect.width,
                clipRect.y, clipRect.y + clipRect.height);
        return Printable.PAGE_EXISTS;
    }

    //
    // End of Printable SUPPORT
    // ////////////////////////////////////

    // ////////////////////////////////////
    // Pageable SUPPORT
    //
    public final int getNumberOfPages() {
        return nbPages.width * nbPages.height;
    }

    public final PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return pageFormat;
    }

    public final Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return this;
    }
    //
    // End of Pageable SUPPORT
    // ////////////////////////////////////

}
