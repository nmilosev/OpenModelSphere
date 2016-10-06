/*************************************************************************

Copyright (C) 2012 Grandite

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

package org.modelsphere.jack.srtool.features.layout.graph;

import java.awt.Point;

/**
 * <p>
 * Represents a <i>dummy-vertex</i> (also referred to as, <i>null-node</i>) that can stand as a
 * vertex in a graph.
 * <p>
 * <i>Dummy-vertex</i> are mostly inserted to replace some edges by a path of new edges. The object
 * can store the reference of the replaced edge (or edges) that it at the origin of its insertion.
 * As they are not supposed to change over the life time of a <tt>DummyVertex</tt>, the replaced
 * edge(s) can only be set at creation time and not later.
 * <p>
 * It also can be applied a location (a coordinate in Z^2).
 * 
 * @author Gabriel
 * 
 */
public class DummyVertex extends CrossingVertex {

    /**
     * Instantiates a new dummy vertex.
     */
    public DummyVertex() {
        this(new Point(0, 0), new Edge[0]);
    }

    /**
     * Instantiates a new dummy vertex.
     * 
     * @param position
     *            An initial position.
     */
    public DummyVertex(Point position) {
        this(position, new Edge[0]);
    }

    /**
     * Instantiates a new dummy vertex.
     * 
     * @param replacingEdge
     *            An <tt>Edge</tt> replaced by the this <i>dummy-vertex</i>.
     */
    public DummyVertex(Edge replacingEdge) {
        this(new Edge[] { replacingEdge });
    }

    /**
     * Instantiates a new dummy vertex.
     * 
     * @param replacingEdges
     *            An array of each <tt>Edge</tt> that is replaced by this <i>dummy-vertex</i>.
     */
    public DummyVertex(Edge[] replacingEdges) {
        this(new Point(0, 0), replacingEdges);
    }

    /**
     * Instantiates a new dummy vertex.
     * 
     * @param position
     *            An initial position.
     * @param replacingEdges
     *            An array of each <tt>Edge</tt> that is replaced by this <i>dummy-vertex</i>.
     */
    public DummyVertex(Point position, Edge[] replacingEdges) {
        super(position, replacingEdges);
    }

    /**
     * Sets the location.
     * 
     * @param x
     *            the x
     * @param y
     *            the y
     */
    public void setLocation(int x, int y) {
        setLocation(new Point(x, y));
    }

    /**
     * Gets the x.
     * 
     * @return the x
     */
    public int getX() {
        return location.x;
    }

    /**
     * Gets the y.
     * 
     * @return the y
     */
    public int getY() {
        return location.y;
    }

}
