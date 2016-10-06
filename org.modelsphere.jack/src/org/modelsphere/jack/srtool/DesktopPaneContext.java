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

package org.modelsphere.jack.srtool;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JViewport;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.preference.context.ContextComponent;
import org.modelsphere.jack.preference.context.ContextUtils;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.w3c.dom.*;

class DesktopPaneContext implements ContextComponent {
    private static final String INTERNAL_FRAME_TAG = "internalFrame";
    private static final String BOUNDS_TAG = "bounds";
    private static final String LOCX = "x";
    private static final String LOCY = "y";
    private static final String LOCW = "w";
    private static final String LOCH = "h";

    private static final String STATE_TAG = "state";
    private static final String STATE_VALUE = "value";

    private static final String VIEW_TAG = "view";
    private static final String VIEW_ZOOM = "zoom";
    private static final String VIEW_X = "x";
    private static final String VIEW_Y = "y";

    private static final int STATE_MAXIMIZED = 0x01;
    private static final int STATE_ICONIFIED = 0x02;

    private DefaultMainFrame frame;
    private JDesktopPane desktopPane = null;

    DesktopPaneContext(DefaultMainFrame frame) {
        this.frame = frame;
    }

    @Override
    public String getID() {
        return "DesktopPaneDiagram";
    }

    @Override
    public void loadContext(Element configurationElement) throws DbException {
        desktopPane = frame.getJDesktopPane();
        if (desktopPane == null) {
            return;
        }

        NodeList nodes = configurationElement.getChildNodes();
        int count = nodes.getLength();
        for (int i = 0; i < count; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equals(INTERNAL_FRAME_TAG)) {
                loadInternalFrame(node);
            }
        }
    }

    private void loadInternalFrame(Node internalFrameNode) throws DbException {
        DbObject dbo = ContextUtils.readDOMElement((Element) internalFrameNode);

        if (dbo == null)
            return;

        Rectangle bounds = null;
        int state = 0;
        NodeList nodes = internalFrameNode.getChildNodes();
        int count = nodes.getLength();

        String viewX = null;
        String viewY = null;
        String viewZoom = null;

        for (int i = 0; i < count; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(BOUNDS_TAG)) {
                bounds = findBounds(node);
            } else if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equals(STATE_TAG)) {
                state = findState(node);
            } else if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equals(VIEW_TAG)) {
                NamedNodeMap attributes = node.getAttributes();
                if (attributes != null) {
                    Node xNode = attributes.getNamedItem(VIEW_X);
                    Node yNode = attributes.getNamedItem(VIEW_Y);
                    Node zoomNode = attributes.getNamedItem(VIEW_ZOOM);
                    if (xNode != null) {
                        viewX = xNode.getNodeValue();
                    }
                    if (yNode != null) {
                        viewY = yNode.getNodeValue();
                    }
                    if (zoomNode != null) {
                        viewZoom = zoomNode.getNodeValue();
                    }
                }
            }
        }
        if (bounds == null || bounds.width < 10 || bounds.height < 10) {
            return;
        }

        DiagramInternalFrame internalFrame = frame.addDiagramInternalFrame(dbo);

        if (internalFrame == null)
            return;
        try {
            internalFrame.setSelected(true);
            internalFrame.setNormalBounds(bounds);
            internalFrame.setMaximum((state & STATE_MAXIMIZED) != 0);
            internalFrame.setIcon((state & STATE_ICONIFIED) != 0);
            if ((state & STATE_ICONIFIED) == 0)
                internalFrame.toFront();
        } catch (Exception e) {
        }

        if (viewX != null && viewY != null && viewZoom != null) {
            try {
                int x = Integer.parseInt(viewX);
                int y = Integer.parseInt(viewY);
                float zoom = Float.parseFloat(viewZoom);
                if (x > -1 && y > -1 && zoom >= 0 && zoom <= 5) {
                    internalFrame.getDiagram().getMainView().setZoomFactor(zoom);
                    internalFrame.getDiagram().getMainView().validate();
                    ((JViewport) internalFrame.getDiagram().getMainView().getParent())
                            .setViewPosition(new Point(x, y));
                }
            } catch (Exception e) {
            }
        }

    }

    private Rectangle findBounds(Node boundsNode) {
        NamedNodeMap attributes = boundsNode.getAttributes();
        if (attributes == null)
            return null;

        Node xNode = attributes.getNamedItem(LOCX);
        Node yNode = attributes.getNamedItem(LOCY);
        Node wNode = attributes.getNamedItem(LOCW);
        Node hNode = attributes.getNamedItem(LOCH);
        if (xNode == null || yNode == null || wNode == null || hNode == null)
            return null;

        String xValue = xNode.getNodeValue();
        String yValue = yNode.getNodeValue();
        String wValue = wNode.getNodeValue();
        String hValue = hNode.getNodeValue();
        if (xValue == null || yValue == null || wValue == null || hValue == null)
            return null;

        double x = Double.MIN_VALUE;
        double y = Double.MIN_VALUE;
        double w = -1;
        double h = -1;
        try {
            x = Double.parseDouble(xValue);
            y = Double.parseDouble(yValue);
            w = Double.parseDouble(wValue);
            h = Double.parseDouble(hValue);
        } catch (Exception e) {
            return null;
        }

        if (x == Double.MIN_VALUE || y == Double.MIN_VALUE || w == -1 || h == -1)
            return null;

        Rectangle desktopBounds = desktopPane.getBounds();
        Rectangle bounds = new Rectangle((int) (x * desktopBounds.width),
                (int) (y * desktopBounds.height), (int) (w * desktopBounds.width),
                (int) (h * desktopBounds.height));
        return bounds;
    }

    private int findState(Node stateNode) {
        NamedNodeMap attributes = stateNode.getAttributes();
        if (attributes == null)
            return 0;

        Node valueNode = attributes.getNamedItem(STATE_VALUE);
        if (valueNode == null) {
            return 0;
        }

        String stateValue = valueNode.getNodeValue();
        if (stateValue == null)
            return 0;

        int state = 0;
        try {
            state = Integer.parseInt(stateValue);
        } catch (Exception e) {
            return 0;
        }

        return state;
    }

    @Override
    public boolean saveContext(Document document, Element componentConfigurationElement)
            throws DbException {
        desktopPane = frame.getJDesktopPane();
        if (desktopPane == null) {
            return true;
        }

        Component[] components = desktopPane.getComponents();
        for (int i = components.length - 1; i >= 0; i--) {
            if (components[i] instanceof DiagramInternalFrame) {
                if (!((DiagramInternalFrame) components[i]).isClosed())
                    saveContext(document, componentConfigurationElement,
                            (DiagramInternalFrame) components[i]);
            } else if (components[i] instanceof JInternalFrame.JDesktopIcon) {
                JInternalFrame internalFrame = ((JInternalFrame.JDesktopIcon) components[i])
                        .getInternalFrame();
                if (internalFrame instanceof DiagramInternalFrame) {
                    if (!internalFrame.isClosed())
                        saveContext(document, componentConfigurationElement,
                                (DiagramInternalFrame) internalFrame);
                }
            }
        }
        return true;
    }

    private void saveContext(Document document, Element componentConfigurationElement,
            DiagramInternalFrame internalFrame) {
        ApplicationDiagram applicationDiagram = internalFrame.getDiagram();
        if (applicationDiagram == null)
            return;
        DbObject diagramGO = applicationDiagram.getDiagramGO();
        if (diagramGO == null)
            return;

        Element internalFrameElement = document.createElement(INTERNAL_FRAME_TAG);
        if (!ContextUtils.writeDOMElement(document, internalFrameElement, diagramGO)) {
            return;
        }
        Rectangle desktopbounds = desktopPane.getBounds();
        Rectangle bounds = internalFrame.getNormalBounds();
        if (bounds == null) {
            return;
        }
        Element boundsElement = document.createElement(BOUNDS_TAG);
        double x = (double) bounds.x / (double) desktopbounds.width;
        double y = (double) bounds.y / (double) desktopbounds.height;
        double w = (double) bounds.width / (double) desktopbounds.width;
        double h = (double) bounds.height / (double) desktopbounds.height;
        boundsElement.setAttribute(LOCX, Double.toString(x));
        boundsElement.setAttribute(LOCY, Double.toString(y));
        boundsElement.setAttribute(LOCW, Double.toString(w));
        boundsElement.setAttribute(LOCH, Double.toString(h));

        internalFrameElement.appendChild(boundsElement);

        Element stateElement = document.createElement(STATE_TAG);
        int state = 0;
        if (internalFrame.isMaximum()) {
            state |= STATE_MAXIMIZED;
        }
        if (internalFrame.isIcon()) {
            state |= STATE_ICONIFIED;
        }
        stateElement.setAttribute(STATE_VALUE, Integer.toString(state));

        internalFrameElement.appendChild(stateElement);

        float zoom = applicationDiagram.getMainView().getZoomFactor();
        Rectangle viewBounds = applicationDiagram.getMainView().getVisibleRect();
        Element viewElement = document.createElement(VIEW_TAG);
        viewElement.setAttribute(VIEW_ZOOM, Float.toString(zoom));
        viewElement.setAttribute(VIEW_X, Integer.toString(viewBounds.x));
        viewElement.setAttribute(VIEW_Y, Integer.toString(viewBounds.y));

        internalFrameElement.appendChild(viewElement);

        componentConfigurationElement.appendChild(internalFrameElement);
    }

}
