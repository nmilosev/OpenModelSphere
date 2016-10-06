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

import org.modelsphere.jack.baseDb.db.DbObject;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class AssociationRoles {
    private DbObject associationGo = null;
    private LineLabel LineLabel1 = null;
    private LineLabel LineLabel2 = null;

    public AssociationRoles() {
    }

    /**
     * @return Returns the associationGo.
     */
    public DbObject getAssociationGo() {
        return associationGo;
    }

    /**
     * @param associationGo
     *            The associationGo to set.
     */
    public void setAssociationGo(DbObject associationGo) {
        this.associationGo = associationGo;
    }

    /**
     * @return Returns the lineLabel1.
     */
    public LineLabel getLineLabel1() {
        return LineLabel1;
    }

    /**
     * @param lineLabel1
     *            The lineLabel1 to set.
     */
    public void setLineLabel1(LineLabel lineLabel1) {
        LineLabel1 = lineLabel1;
    }

    /**
     * @return Returns the lineLabel2.
     */
    public LineLabel getLineLabel2() {
        return LineLabel2;
    }

    /**
     * @param lineLabel2
     *            The lineLabel2 to set.
     */
    public void setLineLabel2(LineLabel lineLabel2) {
        LineLabel2 = lineLabel2;
    }
}
