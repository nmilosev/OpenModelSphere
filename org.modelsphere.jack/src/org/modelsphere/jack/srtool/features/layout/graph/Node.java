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
import java.awt.Rectangle;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.graphic.DbGraphic;

/**
 * The Class Node.
 */
public class Node extends Vertex {

    /** The so. */
    private DbObject so;

    /** The go. */
    private DbObject go;

    /** The weight. */
    private double weight = 0.0;

    /** The bounds. */
    private Rectangle bounds = new Rectangle();

    /** The fit mode. */
    private int fitMode = GraphicComponent.NO_FIT;

    /** The name. */
    private String name;

    /**
     * Instantiates a new node.
     */
    public Node() {
    }

    /**
     * Gets the bounds.
     * 
     * @return the bounds
     */
    public Rectangle getBounds() {
        return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * Gets the fit mode.
     * 
     * @return the fit mode
     */
    public int getFitMode() {
        return fitMode;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Don't use this to change the name in the real application.
     * 
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the gO.
     * 
     * @return the gO
     */
    public DbObject getGO() {
        return go;
    }

    /**
     * Gets the height.
     * 
     * @return the height
     */
    public int getHeight() {
        return bounds.height;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.graph.Vertex#getLocation()
     */
    @Override
    public Point getLocation() {
        return new Point(bounds.x, bounds.y);
    }

    /**
     * Gets the sO.
     * 
     * @return the sO
     */
    public DbObject getSO() {
        return so;
    }

    /**
     * Gets the weight.
     * 
     * @return the weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Gets the width.
     * 
     * @return the width
     */
    public int getWidth() {
        return bounds.width;
    }

    /**
     * Gets the x.
     * 
     * @return the x
     */
    public int getX() {
        return bounds.x;
    }

    /**
     * Gets the y.
     * 
     * @return the y
     */
    public int getY() {
        return bounds.y;
    }

    /**
     * Sets the bounds.
     * 
     * @param bounds
     *            the new bounds
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.graph.Vertex#setDimension
     * (java.awt.Dimension)
     */
    @Override
    public void setDimension(Dimension dimension) {
        bounds.setRect(bounds.x, bounds.y, dimension.width, dimension.height);
    }

    /**
     * Sets the dimension.
     * 
     * @param w
     *            the w
     * @param h
     *            the h
     */
    public void setDimension(int w, int h) {
        bounds.setRect(bounds.x, bounds.y, w, h);
    }

    /**
     * Sets the fit mode.
     * 
     * @param fitMode
     *            the new fit mode
     */
    public void setFitMode(int fitMode) {
        this.fitMode = fitMode;
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
    public void setGO(DbObject go, DbObject so) throws DbException {
        this.go = go;
        this.so = so;
        name = so == null ? null : so.getName();

        if (go != null) {
            Rectangle rect = (Rectangle) go.get(DbGraphic.fGraphicalObjectRectangle);
            bounds.setRect(rect);
        }
    }

    /**
     * Sets the height.
     * 
     * @param h
     *            the new height
     */
    public void setHeight(int h) {
        bounds.setRect(bounds.x, bounds.y, bounds.width, h);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.graph.Vertex#setLocation( java.awt.Point)
     */
    @Override
    public void setLocation(Point point) {
        bounds.setRect(point.x, point.y, bounds.width, bounds.height);
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
        bounds.setRect(x, y, bounds.width, bounds.height);
    }

    /**
     * Sets the weight.
     * 
     * @param weight
     *            the new weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Sets the width.
     * 
     * @param w
     *            the new width
     */
    public void setWidth(int w) {
        bounds.setRect(bounds.x, bounds.y, w, bounds.height);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (name == null) {
            return super.toString();
        }
        return "[Node] " + name;
    }

    /**
     * Translate.
     * 
     * @param dx
     *            the dx
     * @param dy
     *            the dy
     */
    public void translate(int dx, int dy) {
        bounds.setRect(bounds.x + dx, bounds.y + dy, bounds.width, bounds.height);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.srtool.features.layout.graph.Vertex#getDimension()
     */
    @Override
    public Dimension getDimension() {
        return new Dimension(bounds.width, bounds.height);
    }
}
