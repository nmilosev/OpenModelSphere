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

package org.modelsphere.jack.srtool.graphic;

import java.awt.Point;
import java.io.Serializable;

/**
 * 
 * A PointCascade implements a stepable position in an two-dimensional offset wrapping-space. The
 * instance computes a new Point location and increments it on demand. The four logical Point
 * parameters which control the instance are
 * <UL>
 * <LI>the <I>offset</I> from the origin,
 * <LI>the current <I>position</I> relative to the offset,
 * <LI>the <I>step</I> which is added to the position during an increment, and
 * <LI>the <I>wrap</I> which bounds the position relative to the offset.
 * </UL>
 * The instance wraps access to two Cascade objects, one for the x-dimension and the other for the
 * y.
 * 
 */
public class PointCascade implements Cloneable, Serializable {
    private Cascade xCascade;
    private Cascade yCascade;

    /**
     * Creates a new PointCascade with default logical parameters.
     **/
    public PointCascade() {
        setXCascade(new Cascade(0, 0, 25, 200));
        setYCascade(new Cascade(0, 0, 25, 100));
    }

    /**
     * Creates a new PointCascade with the given Cascades.
     * 
     * @param xCascade
     *            the Cascade for the x dimension
     * @param yCascade
     *            the Cascade for the y dimension
     **/
    public PointCascade(Cascade xCascade, Cascade yCascade) {
        setXCascade(xCascade);
        setYCascade(yCascade);
    }

    /**
     * Creates a new Cascade with the given logical parameters.
     * 
     * @param offset
     *            the offset from 0,0
     * @param position
     *            the position from the offset
     * @param step
     *            the distance the position is incremented
     * @param wrap
     *            the basis of the position modulus
     **/
    public PointCascade(Point offset, Point position, Point step, Point wrap) {
        this();
        setOffset(offset);
        setPosition(position);
        setStep(step);
        setWrap(wrap);
    }

    /**
     * Sets the instance's x dimension Cascade
     * 
     * @param xCascade
     *            the Cascade for the x dimension
     **/
    public void setXCascade(Cascade xCascade) {
        this.xCascade = xCascade;
    }

    /**
     * Gets the instance's x dimension Cascade
     * 
     * @return the Cascade for the x dimension
     **/
    public Cascade getXCascade() {
        return xCascade;
    }

    /**
     * Sets the instance's y dimension Cascade
     * 
     * @param xCascade
     *            the Cascade for the y dimension
     **/
    public void setYCascade(Cascade yCascade) {
        this.yCascade = yCascade;
    }

    /**
     * Gets the instance's y dimension Cascade
     * 
     * @return the Cascade for the y dimension
     **/
    public Cascade getYCascade() {
        return yCascade;
    }

    /**
     * Sets the instance's logical offset
     * 
     * @param offset
     *            the offset from 0,0
     **/
    public void setOffset(Point offset) {
        xCascade.setOffset(offset.x);
        yCascade.setOffset(offset.y);
    }

    /**
     * Gets the instance's logical offset
     * 
     * @return the offset from 0,0
     **/
    public Point getOffset() {
        return new Point(xCascade.getOffset(), yCascade.getOffset());
    }

    /**
     * Sets the instance's logical position
     * 
     * @param position
     *            the position from the offset
     **/
    public void setPosition(Point position) {
        xCascade.setPosition(position.x);
        yCascade.setPosition(position.y);
    }

    /**
     * Gets the instance's logical position
     * 
     * @return the position from the offset
     **/
    public Point getPosition() {
        return new Point(xCascade.getPosition(), yCascade.getPosition());
    }

    /**
     * Sets the instance's logical step
     * 
     * @param step
     *            the distance the position is incremented
     **/
    public void setStep(Point step) {
        xCascade.setStep(step.x);
        yCascade.setStep(step.y);
    }

    /**
     * Gets the instance's logical step
     * 
     * @return the distance the position is incremented
     **/
    public Point getStep() {
        return new Point(xCascade.getStep(), yCascade.getStep());
    }

    /**
     * Sets the instance's logical wrap
     * 
     * @param wrap
     *            the basis of the position modulus
     **/
    public void setWrap(Point wrap) {
        xCascade.setWrap(wrap.x);
        yCascade.setWrap(wrap.y);
    }

    /**
     * Gets the instance's logical wrap
     * 
     * @return the basis of the position modulus
     **/
    public Point getWrap() {
        return new Point(xCascade.getWrap(), yCascade.getWrap());
    }

    /**
     * Computes the instance's current location.
     * 
     * @return the location
     **/
    public Point location() {
        return new Point(xCascade.location(), yCascade.location());
    }

    /**
     * Increments the instance's location, keeping it within the parameters.
     **/
    public void increment() {
        xCascade.increment();
        yCascade.increment();
    }

    /**
     * Compares the contents of the instance with the given PointCascade's contents.
     * 
     * @return true if the contents of the instance and the given pointCascade are the same
     **/
    public boolean equals(PointCascade pointCascade) {
        return ((pointCascade != null) && (getOffset().equals(pointCascade.getOffset()))
                && (getPosition().equals(pointCascade.getPosition()))
                && (getStep().equals(pointCascade.getStep())) && (getWrap().equals(pointCascade
                .getWrap())));
    }

    /**
     * Clones the instance.
     * 
     * @return the cloned instance
     **/
    public Object clone() {
        PointCascade clone = null;
        try {
            clone = (PointCascade) (super.clone());
        } catch (CloneNotSupportedException exception) {
            throw new InternalError(exception.toString());
        }
        clone.xCascade = (Cascade) (xCascade.clone());
        clone.yCascade = (Cascade) (yCascade.clone());
        return clone();
    }
}
