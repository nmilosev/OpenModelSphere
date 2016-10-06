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

import org.modelsphere.jack.util.SrVector;

public final class GraphLayoutNode {

    Rectangle rect;
    int bigWidth;
    int bigHeight;
    double radius;
    double distance;
    double startAngle;
    double arcAngle;
    double childrenArcAngle;
    Object userNode;
    SrVector links = new SrVector();
    SrVector constraintLinks;
    GraphLayoutNode parent;
    SrVector children = new SrVector();
    int level;
    boolean movedCloser = false;

    GraphLayoutNode() {
    }

    GraphLayoutNode(Rectangle rect, Object userNode) {
        this.rect = rect;
        this.userNode = userNode;
    }

    public final Rectangle getRect() {
        return rect;
    }

    public final Object getUserNode() {
        return userNode;
    }

    final void setParent(GraphLayoutNode parent) {
        this.parent = parent;
        parent.children.addElement(this);
    }
}
