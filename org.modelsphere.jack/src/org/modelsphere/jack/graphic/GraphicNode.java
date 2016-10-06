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

import java.awt.Rectangle;

import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.util.SrVector;

// A graphic component that may have lines connected to it.
public class GraphicNode extends ZoneBox {

    private SrVector lines1 = null; // lines whose first point is connected to this node.
    private SrVector lines2 = null; // lines whose last point is connected to this node.
    private SrVector attachments = null;

    public GraphicNode(Diagram diagram, GraphicShape shape) {
        super(diagram, shape);
    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        if (!all) {
            int i;
            if (lines1 != null) {
                for (i = lines1.size(); --i >= 0;) {
                    Line line = ((Line) lines1.get(i));
                    if (line.isStandAloneSupported() && !all)
                        line.node1 = null;
                    else
                        line.delete(all);
                }
            }
            if (lines2 != null) {
                for (i = lines2.size(); --i >= 0;) {
                    Line line = ((Line) lines2.get(i));
                    if (line.isStandAloneSupported() && !all)
                        line.node2 = null;
                    else
                        line.delete(all);
                }
            }
            if (attachments != null) {
                for (i = attachments.size(); --i >= 0;)
                    ((Attachment) attachments.get(i)).delete(all);
            }
        }
        lines1 = lines2 = attachments = null;
        super.delete(all);
    }

    final void addLine1(Line line) {
        if (lines1 == null)
            lines1 = new SrVector();
        lines1.add(line);
    }

    final void addLine2(Line line) {
        if (lines2 == null)
            lines2 = new SrVector();
        lines2.add(line);
    }

    final void removeLine1(Line line) {
        if (lines1 != null)
            lines1.remove(line);
    }

    final void removeLine2(Line line) {
        if (lines2 != null)
            lines2.remove(line);
    }

    public final Line[] getLines() {
        int n1 = (lines1 == null ? 0 : lines1.size());
        int n2 = (lines2 == null ? 0 : lines2.size());
        Line[] lines = new Line[n1 + n2];
        if (lines1 != null)
            lines1.getElements(0, n1, lines, 0);
        if (lines2 != null)
            lines2.getElements(0, n2, lines, n1);
        return lines;
    }

    final void addAttachment(Attachment attachment) {
        if (attachments == null)
            attachments = new SrVector();
        attachments.add(attachment);
    }

    final void removeAttachment(Attachment attachment) {
        if (attachments != null)
            attachments.remove(attachment);
    }

    public final Attachment getAttachment() {
        return (attachments != null && attachments.size() != 0 ? (Attachment) attachments.get(0)
                : null);
    }

    public final Attachment[] getAttachments() {
        if (attachments == null)
            return new Attachment[0];
        Attachment[] array = new Attachment[attachments.size()];
        attachments.getElements(0, attachments.size(), array, 0);
        return array;
    }

    // Mark all lines as needing position recomputation (necessary if node is fitted
    // with its central point fixed, in which case the line polygons will not be changed)
    public final void setRectangle(Rectangle rect) {
        super.setRectangle(rect);
        int i;
        if (lines1 != null) {
            for (i = lines1.size(); --i >= 0;)
                diagram.setComputePos((Line) lines1.get(i));
        }
        if (lines2 != null) {
            for (i = lines2.size(); --i >= 0;)
                diagram.setComputePos((Line) lines2.get(i));
        }
        if (attachments != null) {
            for (i = attachments.size(); --i >= 0;)
                diagram.setComputePos((Attachment) attachments.get(i));
        }
    }
}
