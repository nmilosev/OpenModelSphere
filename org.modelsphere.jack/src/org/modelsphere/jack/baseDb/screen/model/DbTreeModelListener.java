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

package org.modelsphere.jack.baseDb.screen.model;

import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.Explorer;

public class DbTreeModelListener {

    private TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    // If false is returned, the node is not inserted in the tree.
    // We already checked that <dbo> is of the wanted classes or may have the wanted classes in its descendents.
    // Called in a read transaction.
    public boolean filterNode(DbObject dbo) throws DbException {
        return true;
    }

    public String getDisplayText(DbObject dbo, Object callingObject) throws DbException {
        if (terminologyUtil.isObjectLine(dbo))
            return ApplicationContext.getSemanticalModel().getDisplayText(dbo, Explorer.class);
        else
            return dbo.getName();
    }

    public Icon getIcon(DbObject dbo) throws DbException {
        return dbo.getSemanticalIcon(DbObject.SHORT_FORM);
    }

    // We already checked that <dbo> is of the wanted classes.
    // Called in a read transaction.
    public boolean isSelectable(DbObject dbo) throws DbException {
        return true;
    }
}
