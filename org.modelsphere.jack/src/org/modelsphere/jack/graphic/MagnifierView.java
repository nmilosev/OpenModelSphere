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
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MagnifierView extends DiagramView {
    public static final String ZOOM_FACTOR_PROPERTY = "ZoomFactor"; // NOT LOCALIZABLE - property
    public static final float ZOOM_FACTOR_PROPERTY_DEFAULT = 1.0f;

    private static final int HALF_CROSS = (int) (Diagram.PIXELS_PER_MM * 2);

    private DiagramView srcView;
    private Point mousePos = null;

    private MouseMotionListener srcViewListener = new MouseMotionListener() {
        public void mouseDragged(MouseEvent e) {
            scrollToMousePos(e);
        }

        public void mouseMoved(MouseEvent e) {
            scrollToMousePos(e);
        }
    };

    public MagnifierView(DiagramView srcView) {
        super(srcView.getModel());
        setProcessRightMouseButton(false);
        this.srcView = srcView;
        srcView.addMouseMotionListener(srcViewListener);
    }

    public final void delete() {
        srcView.removeMouseMotionListener(srcViewListener);
        super.delete();
    }

    public final void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mousePos != null) {
            g.setColor(Color.red);
            Point zoomPoint = new Point((int) (mousePos.x * getZoomFactor()),
                    (int) (mousePos.y * getZoomFactor()));
            g
                    .drawLine(zoomPoint.x - HALF_CROSS, zoomPoint.y, zoomPoint.x + HALF_CROSS,
                            zoomPoint.y);
            g
                    .drawLine(zoomPoint.x, zoomPoint.y - HALF_CROSS, zoomPoint.x, zoomPoint.y
                            + HALF_CROSS);
        }
    }

    private void scrollToMousePos(MouseEvent e) {
        float zoom = srcView.getZoomFactor();
        mousePos = new Point((int) (e.getX() / zoom), (int) (e.getY() / zoom));
        scrollRectToCenter(new Rectangle(mousePos.x, mousePos.y, 1, 1));
    }

}
