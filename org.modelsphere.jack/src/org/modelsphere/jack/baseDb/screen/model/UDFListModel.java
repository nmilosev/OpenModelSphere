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

import java.lang.reflect.Modifier;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.SrVector;

public class UDFListModel extends DbListModel {

    public UDFListModel(ScreenView screenView, DbProject project) throws DbException {
        super(screenView, project, DbObject.fComponents, DbUDF.metaClass);
    }

    protected final void loadPartitions() throws DbException {
        SrVector vecParts = new SrVector();
        loadParts(vecParts, DbObject.metaClass);
        vecParts.sort();
        partitions = new Partition[vecParts.size()];
        vecParts.getElements(0, partitions.length, partitions, 0);
    }

    private void loadParts(SrVector vecParts, MetaClass metaClass) {
        if (!Modifier.isAbstract(metaClass.getJClass().getModifiers())
                && (metaClass.getFlags() & MetaClass.NO_UDF) == 0) {
            String guiName = ApplicationContext.getSemanticalModel()
                    .getDisplayTextForUDF(metaClass);
            vecParts.addElement(new Partition(metaClass, guiName));
        }
        MetaClass[] subClasses = metaClass.getSubMetaClasses();
        for (int i = 0; i < subClasses.length; i++)
            loadParts(vecParts, subClasses[i]);
    }

    protected final Object getPartId(DbObject dbObj) throws DbException {
        return ((DbUDF) dbObj).getUDFMetaClass();
    }
}
