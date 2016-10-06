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

import java.awt.Dimension;
import java.awt.Point;

/**
 * The Class CrossingVertex.
 * 
 * @author David
 */
public abstract class CrossingVertex extends Vertex {

    /** The location. */
    protected Point location;

    /** The replaced edges. */
    protected Edge[] replacedEdges;

    /**
     * Instantiates a new crossing vertex.
     * 
     * @param location
     *            the location
     * @param crossingEdges
     *            the crossing edges
     */
    protected CrossingVertex(Point location, Edge[] crossingEdges) {
        this.location = location;
        replacedEdges = crossingEdges;
    }

    /**
     * Gets the location.
     * 
     * @return the location
     * @see org.modelsphere.jack.srtool.features.layout.graph.Vertex#getLocation()
     */
    @Override
    public Point getLocation() {
        return location;
    }

    /**
     * Sets the location.
     * 
     * @param point
     *            the new location
     * @see org.modelsphere.jack.srtool.features.layout.graph.Vertex#setLocation(java.awt.Point)
     */
    @Override
    public void setLocation(Point point) {
        location = point;
    }

    /**
     * Gets the dimension.
     * 
     * @return the dimension
     * @see org.modelsphere.jack.srtool.features.layout.graph.Vertex#getDimension()
     */
    @Override
    public Dimension getDimension() {
        return new Dimension(0, 0);
    }

    /**
     * Sets the dimension.
     * 
     * @param size
     *            the new dimension
     * @see org.modelsphere.jack.srtool.features.layout.graph.Vertex#setDimension(java.awt.Dimension)
     */
    @Override
    public void setDimension(Dimension size) {
        return;
    }

    /**
     * Gets the replaced edges.
     * 
     * @return the replaced edges
     */
    public Edge[] getReplacedEdges() {
        return replacedEdges;
    }

}
