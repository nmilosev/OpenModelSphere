/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.oo.java.actions;

import java.util.ArrayList;

import org.modelsphere.jack.actions.AbstractDomainAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrSort;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.util.AnyAdt;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public final class SetCollectionDataTypeAction extends AbstractDomainAction implements
        SelectionActionListener {

    private static final String kCollType = LocaleMgr.action.getString("collectionDataType");
    private static final String kSetCollType = LocaleMgr.action.getString("setCollectionType");
    private static final String kArray = LocaleMgr.action.getString("array");

    SetCollectionDataTypeAction() {
        super(kCollType);
    }

    protected final void doActionPerformed() {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        DbOOAdt collType = (DbOOAdt) ((DefaultComparableElement) getSelectedObject()).object;
        try {
            objects[0].getDb().beginTrans(Db.WRITE_TRANS, kSetCollType);
            for (int i = 0; i < objects.length; i++) {
                DbOODataMember dm = (DbOODataMember) objects[i];
                DbOOAdt type = (DbOOAdt) (dm.getAssociationEnd().getOppositeEnd()
                        .getAssociationMember().getComposite());
                if (collType != null) {
                    dm.setElementType(type);
                    dm.setType(collType);
                    dm.setTypeUse(null);
                } else {
                    dm.setType(type);
                    dm.setElementType(null);
                    if (dm.getTypeUse() == null)
                        dm.setTypeUse(DbJVDataMember.ARRAY_TYPE_USE);
                }
            }
            objects[0].getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    // Selection only from diagram; so we are sure all selected objects belong
    // to the same project.
    public final void updateSelectionAction() throws DbException {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        int i;
        for (i = 0; i < objects.length; i++) {
            DbOODataMember dm = (DbOODataMember) objects[i];
            SMSMultiplicity multi = dm.getAssociationEnd().getMultiplicity();
            if (multi.getValue() == SMSMultiplicity.EXACTLY_ONE
                    || multi.getValue() == SMSMultiplicity.OPTIONAL) {
                setEnabled(false);
                return;
            }
        }

        ArrayList collTypes = (ArrayList) AnyAdt.getCollectionAdtsAux(new ArrayList(), objects[0]
                .getProject());
        int nbTypes = collTypes.size();
        DefaultComparableElement[] values = new DefaultComparableElement[nbTypes > 0 ? nbTypes + 2
                : 1];
        for (i = 0; i < nbTypes; i++) {
            DbOOAdt adt = (DbOOAdt) collTypes.get(i);
            String name = adt.getSemanticalName(DbObject.LONG_FORM);
            int iname = name.lastIndexOf('.');
            if (iname != -1)
                name = name.substring(iname + 1) + "  (" + name.substring(0, iname) + ")"; // NOT LOCALIZABLE
            values[i] = new DefaultComparableElement(adt, name);
        }
        SrSort.sortArray(values, nbTypes, new CollationComparator());
        if (nbTypes > 0)
            values[i++] = null; // separator
        values[i] = new DefaultComparableElement(null, kArray);
        setDomainValues(values);
        setEnabled(true);

        Object selType = null;
        for (i = 0; i < objects.length; i++) {
            DbOODataMember dm = (DbOODataMember) objects[i];
            Object type = dm.getType();
            if (!(type instanceof DbJVClass && ((DbJVClass) type).isCollection())) {
                type = kArray;
                if (DbObject.valuesAreEqual(dm.getTypeUse(), null)) {
                    selType = null;
                    break;
                }
            }
            if (selType == null)
                selType = type;
            else if (selType != type) {
                selType = null;
                break;
            }
        }
        if (selType == null)
            setSelectedIndex(-1);
        else if (selType == kArray)
            setSelectedIndex(values.length - 1);
        else {
            for (i = 0; i < nbTypes; i++) {
                if (values[i].object == selType) {
                    setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }
}
