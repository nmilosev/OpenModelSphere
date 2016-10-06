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

package org.modelsphere.jack.baseDb.db.srtypes;

import java.awt.Polygon;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;

public final class SrPolygon extends SrType {

    static final long serialVersionUID = -396362582543416750L;

    int[] xs;
    int[] ys;

    // Parameterless constructor
    public SrPolygon() {
    }

    public SrPolygon(Polygon poly) {
        xs = new int[poly.npoints];
        ys = new int[poly.npoints];
        System.arraycopy(poly.xpoints, 0, xs, 0, poly.npoints);
        System.arraycopy(poly.ypoints, 0, ys, 0, poly.npoints);
    }

    public final Object toApplType() {
        return new Polygon(xs, ys, xs.length);
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SrPolygon))
            return false;
        SrPolygon poly = (SrPolygon) obj;
        if (xs.length != poly.xs.length)
            return false;
        for (int i = 0; i < xs.length; i++) {
            if (xs[i] != poly.xs[i] || ys[i] != poly.ys[i])
                return false;
        }
        return true;
    }

    public String toString() {
        String str = "";
        for (int i = 0; i < xs.length; i++) {
            if (i != 0)
                str = str + ", ";
            str = str + "[" + xs[i] + "," + ys[i] + "]"; // NOT LOCALIZABLE
        }
        return str;
    }

    public final void dbFetch(Db db) throws DbException {
        db.fetch(this);
        db.fetch(xs);
        db.fetch(ys);
    }

    public final void dbCluster(Db db, Object parent) throws DbException {
        db.cluster(this, parent);
        db.cluster(xs, this);
        db.cluster(ys, this);
    }
}
