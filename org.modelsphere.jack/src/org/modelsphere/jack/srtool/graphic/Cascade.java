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

import java.io.Serializable;

/**
 * 
 * A Cascade implements a stepable position in a one-dimensional offset wrapping-space. The instance
 * computes a new int location and increments it on demand. The four int parameters which control
 * the instance are
 * 
 * the offset from the origin, the current position relative to the offset, the step which is added
 * to the position during an increment, and the wrap which bounds the position relative to the
 * offset.
 * 
 */
public class Cascade implements Cloneable, Serializable {
    private int offset;
    private int position;
    private int step;
    private int wrap;

    /**
     * Creates a new Cascade with default parameters.
     **/
    public Cascade() {
        this(0, 0, 25, 150);
    }

    /**
     * Creates a new Cascade with the given parameters.
     * 
     * @param offset
     *            the offset from 0
     * @param position
     *            the position from the offset
     * @param step
     *            the distance the position is incremented
     * @param wrap
     *            the basis of the position modulus
     **/
    public Cascade(int offset, int position, int step, int wrap) {
        setOffset(offset);
        setPosition(position);
        setStep(step);
        setWrap(wrap);
    }

    /**
     * Sets the instance's offset.
     * 
     * @param offset
     *            the offset from 0
     **/
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Gets the instance's offset.
     * 
     * @return the offset from 0
     **/
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the instance's position.
     * 
     * @param position
     *            the position from the offset
     **/
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Gets the instance's position.
     * 
     * @return the position from the offset
     **/
    public int getPosition() {
        return position;
    }

    /**
     * Sets the instance's step.
     * 
     * @param step
     *            the distance the position is incremented
     **/
    public void setStep(int step) {
        this.step = step;
    }

    /**
     * Gets the instance's step.
     * 
     * @return the distance the position is incremented
     **/
    public int getStep() {
        return step;
    }

    /**
     * Sets the instance's wrap.
     * 
     * @param wrap
     *            the basis of the position modulus
     **/
    public void setWrap(int wrap) {
        this.wrap = wrap;
    }

    /**
     * Gets the instance's wrap.
     * 
     * @return the basis of the position modulus
     **/
    public int getWrap() {
        return wrap;
    }

    /**
     * Computes the instance's current location.
     * 
     * @return the location
     **/
    public int location() {
        return offset + position;
    }

    /**
     * Increments the instance's location, keeping it within the parameters.
     **/
    public void increment() {
        position += step;
        while (position < 0)
            position += wrap;
        while (position > wrap)
            position -= wrap;
        if (position < 0)
            position = 0;
    }

    /**
     * Compares the contents of the instance with the given Cascade's contents.
     * 
     * @return true if the contents of the instance and the given Cascade are the same
     **/
    public boolean equals(Cascade cascade) {
        return ((cascade != null) && (getOffset() == cascade.getOffset())
                && (getPosition() == cascade.getPosition()) && (getStep() == cascade.getStep()) && (getWrap() == cascade
                .getWrap()));
    }

    /**
     * Clones the instance.
     * 
     * @return the cloned instance
     **/
    public Object clone() {
        Cascade clone = null;
        try {
            clone = (Cascade) (super.clone());
        } catch (CloneNotSupportedException exception) {
            throw new InternalError(exception.toString());
        }
        return clone();
    }
}
