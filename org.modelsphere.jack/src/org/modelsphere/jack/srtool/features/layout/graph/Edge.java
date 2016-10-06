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

import java.awt.Polygon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;

/**
 * The Class Edge.
 */
public class Edge {

    /**
     * The Enum OrientationConstraint.
     */
    public static enum OrientationConstraint {

        /** Edge may be oriented upward or downward in a layered graph. */
        Free,
        /** Edge must be oriented upward in a layered graph. */
        Upward,
        /** The UNDIRECTED. */
        UNDIRECTED
    };

    /**
     * The Enum RelationType.
     */
    public static enum RelationType {

        /** The Association. */
        Association,
        /** The Aggregation. */
        Aggregation,
        /** The Composition. */
        Composition,
        /** The Generalization. */
        Generalization,
        /** The Realization. */
        Realization,
        /** The Dependency. */
        Dependency,
        /** The Multiplicity. */
        Multiplicity
    }

    /** The go. */
    private DbObject go;

    /** The so. */
    private DbObject so;

    /**
     * Holds the information about preferred user orientationConstraint.
     */
    private OrientationConstraint orientationConstraint;

    /**
     * Holds edge's kind of relation in a diagram.
     */
    private RelationType relationType;

    /**
     * Holds the information about whether the edge is right angled or not.
     */
    private boolean rightAngled;

    /**
     * Holds the intermediate points by which the edge should go through in its way from
     * <code>source</code> to <code>destination</code>. It does not include the starting and ending
     * points. If it is worth null, the edge does directly from <code>source</code> to
     * <code>destination</code>.
     */
    private Polygon intermediateCoordinates;

    /**
     * If contains true, the relation in the original OMS diagram was directed the other way around.
     * It is mainly the case for inheritance relations which OMS keeps with <i>parents</i> pointing
     * toward their <i>children</i> whether most algorithms represent inheritance the other way
     * around (with <i>children</i> pointing to their <i>parents</i>).
     */
    private boolean diagramReversed;

    /**
     * Instantiates a new edge.
     */
    public Edge() {
        this(OrientationConstraint.Free);
    }

    /**
     * Instantiates a new edge.
     * 
     * @param orientationConstraint
     *            the orientation constraint
     */
    public Edge(OrientationConstraint orientationConstraint) {
        this.orientationConstraint = orientationConstraint;
        rightAngled = false;
        intermediateCoordinates = null;
        diagramReversed = false;
        relationType = RelationType.Association;
    }

    /**
     * Builds the dummy edge of.
     * 
     * @param edge
     *            the edge
     * @return the edge
     */
    public static Edge buildDummyEdgeOf(Edge edge) {
        Edge dummy = new Edge();

        dummy.setOrientationConstraint(edge.getOrientationConstraint());
        dummy.setRelationType(edge.getRelationType());
        dummy.setRightAngled(edge.isRightAngled());
        dummy.setIntermediateCoordinates(edge.getIntermediateCoordinates());
        dummy.setDiagramReversed(edge.isDiagramReversed());

        return dummy;
    }

    /**
     * Translate all intermediate coordinates for this edge (if any).
     * 
     * @param dx
     *            Translation's delta x.
     * @param dy
     *            Translation's delta y.
     */
    public void translate(int dx, int dy) {
        if (intermediateCoordinates != null) {
            for (int i = 0; i < intermediateCoordinates.npoints; i++) {
                intermediateCoordinates.xpoints[i] += dx;
                intermediateCoordinates.ypoints[i] += dy;
            }
        }
    }

    /**
     * Gets the go.
     * 
     * @return the go
     */
    public DbObject getGo() {
        return go;
    }

    /**
     * Gets the so.
     * 
     * @return the so
     */
    public DbObject getSo() {
        return so;
    }

    /**
     * Gets the orientation constraint.
     * 
     * @return the orientation constraint
     */
    public OrientationConstraint getOrientationConstraint() {
        return orientationConstraint;
    }

    /**
     * Gets the relation type.
     * 
     * @return the relation type
     */
    public RelationType getRelationType() {
        return relationType;
    }

    /**
     * Checks if is right angled.
     * 
     * @return whether the edge should be right angled or not.
     */
    public boolean isRightAngled() {
        return rightAngled;
    }

    /**
     * Gets the intermediate coordinates.
     * 
     * @return A list of intermediate coordinates or null.
     */
    public Polygon getIntermediateCoordinates() {
        return intermediateCoordinates;
    }

    /**
     * Checks if is diagram reversed.
     * 
     * @return true, if is diagram reversed
     */
    public boolean isDiagramReversed() {
        return diagramReversed;
    }

    /**
     * Sets the go.
     * 
     * @param go
     *            the go
     * @param so
     *            the so
     * @throws DbException
     *             the db exception
     */
    public void setGo(DbObject go, DbObject so) throws DbException {
        this.go = go;
        this.so = so;
    }

    /**
     * Sets the orientation constraint.
     * 
     * @param constraint
     *            the new orientation constraint
     */
    public void setOrientationConstraint(OrientationConstraint constraint) {
        orientationConstraint = constraint;
    }

    /**
     * Sets the right angled.
     * 
     * @param rightAngle
     *            the new right angled
     */
    public void setRightAngled(boolean rightAngle) {
        rightAngled = rightAngle;
    }

    /**
     * Sets the relation type.
     * 
     * @param relation
     *            the new relation type
     */
    public void setRelationType(RelationType relation) {
        relationType = relation;
    }

    /**
     * Sets the intermediate coordinates.
     * 
     * @param intermediateCoordinates
     *            the new intermediate coordinates
     */
    public void setIntermediateCoordinates(Polygon intermediateCoordinates) {
        this.intermediateCoordinates = intermediateCoordinates;
    }

    /**
     * Sets the diagram reversed.
     * 
     * @param diagramReversed
     *            the new diagram reversed
     */
    public void setDiagramReversed(boolean diagramReversed) {
        this.diagramReversed = diagramReversed;
    }
}
