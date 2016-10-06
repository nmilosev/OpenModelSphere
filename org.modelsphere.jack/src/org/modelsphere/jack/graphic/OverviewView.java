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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.graphic.tool.Tool;

public class OverviewView extends DiagramView implements ChangeListener {

    private DiagramView srcView;

    public OverviewView(DiagramView srcView) {
        super(srcView.getModel());
        setProcessRightMouseButton(false);
        setDrawingMode(GraphicComponent.DRAW_FRAME);
        this.srcView = srcView;
        setCurrentTool(new OverviewTool(this, srcView));
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                refresh();
            }
        });
        ((JViewport) srcView.getParent()).addChangeListener(this);
    }

    public final void delete() {
        ((JViewport) srcView.getParent()).removeChangeListener(this);
        super.delete();
    }

    // ///////////////////////////////////
    // ChangeListener SUPPORT
    //
    public final void stateChanged(ChangeEvent e) {
        refresh();
    }

    //
    // End of ChangeListener SUPPORT
    // //////////////////////////////////////

    private void refresh() {
        setZoomFactor(getPanoramaZoomFactor());
        ((OverviewTool) getCurrentTool()).refresh();
    }

    private static class OverviewTool extends Tool {

        private DiagramView srcView;
        private Rectangle rect = null;

        public OverviewTool(OverviewView overView, DiagramView srcView) {
            super(0);
            setDiagramView(overView);
            this.srcView = srcView;
        }

        public final void refresh() {
            if (rect != null) {
                // expand for the stroke
                getDiagramView().repaint(rect.x - 1, rect.y - 1, rect.width + 2, rect.height + 2);
            }
            rect = view.zoom(srcView.unzoom(((JViewport) srcView.getParent()).getViewRect()));
            if (rect != null) {
                // expand for the stroke
                getDiagramView().repaint(rect.x - 1, rect.y - 1, rect.width + 2, rect.height + 2);
            }
        }

        public final void paint(Graphics g) {
            if (rect == null)
                return;

            g.setColor(Color.red);
            ((Graphics2D) g).setStroke(new BasicStroke(2));
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }

        public final void mousePressed(MouseEvent e) {
            mouseDragged(e);
        }

        public final void mouseDragged(MouseEvent e) {
            srcView.scrollRectToCenter(new Rectangle(e.getX(), e.getY(), 1, 1));
        }
    }
}
