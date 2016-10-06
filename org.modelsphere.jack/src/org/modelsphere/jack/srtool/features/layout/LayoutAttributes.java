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

package org.modelsphere.jack.srtool.features.layout;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;

/**
 * The Class LayoutAttributes.
 */
public final class LayoutAttributes {

    /** The Constant CANVAS_HORIZONTAL_RESIZE_INCREMENT. */
    public static final LayoutAttribute<Double> CANVAS_HORIZONTAL_RESIZE_INCREMENT = new LayoutAttribute<Double>(
            "CANVAS_HORIZONTAL_RESIZE", 0D);

    /** The Constant CANVAS_VERTICAL_RESIZE_INCREMENT. */
    public static final LayoutAttribute<Double> CANVAS_VERTICAL_RESIZE_INCREMENT = new LayoutAttribute<Double>(
            "CANVAS_VERTICAL_RESIZE", 0D);

    /** The Constant CANVAS_DIMENSION. */
    public static final LayoutAttribute<Dimension2D> CANVAS_DIMENSION = new LayoutAttribute<Dimension2D>(
            "CANVAS_DIMENSION", new Dimension());

    /** The Constant RANDOMIZE. */
    public static final LayoutAttribute<Boolean> RANDOMIZE = new LayoutAttribute<Boolean>(
            "RANDOMIZE", Boolean.FALSE);

    /**
     * Instantiates a new layout attributes.
     */
    private LayoutAttributes() {
    }
}
