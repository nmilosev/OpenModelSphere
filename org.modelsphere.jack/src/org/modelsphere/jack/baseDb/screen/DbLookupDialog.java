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

package org.modelsphere.jack.baseDb.screen;

import java.awt.Component;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrSort;

public abstract class DbLookupDialog {

    /*
     * Displays a lookup dialog filled with the DbObjects in <dbos>, and asks the user to select
     * one. Returns null if user cancels; otherwise returns a DefaultComparableElement whose field
     * <object> is the DbObject selected; this DbObject is null if user selects 'unspecified'.
     * 
     * BEWARE: A read transaction must already be opened before calling this method; this method
     * closes the transaction before displaying the dialog.
     */
    public static DefaultComparableElement selectDbo(Component comp, String title, Point center,
            Db db, Collection dbos, DbObject currentDbo, String nullStr, boolean fullName)
            throws DbException {
        int nb = dbos.size();
        ArrayList arrayDefaultComparableElements = new ArrayList();

        if (nullStr != null) {
            arrayDefaultComparableElements.add(new DefaultComparableElement(null, nullStr));
        }

        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

        boolean bObjectIsEntityOrRelationship = terminologyUtil
                .isObjectEntityOrAssociation(currentDbo);
        boolean bObjectIsRelationship = terminologyUtil.isObjectAssociation(currentDbo);

        Iterator iter = dbos.iterator();
        while (iter.hasNext()) {
            DbObject dbo = (DbObject) iter.next();
            String name;
            if (fullName) {
                name = ApplicationContext.getSemanticalModel().getDisplayText(dbo,
                        DbObject.LONG_FORM, null, null);
                int iname = name.lastIndexOf('.');
                if (iname != -1)
                    name = name.substring(iname + 1) + "  (" + name.substring(0, iname) + ")"; //NOT LOCALIZABLE
            } else
                name = ApplicationContext.getSemanticalModel().getDisplayText(dbo,
                        DbObject.SHORT_FORM, null, null);

            if (bObjectIsEntityOrRelationship) {
                if (bObjectIsRelationship == terminologyUtil.isObjectAssociation(dbo))
                    arrayDefaultComparableElements.add(new DefaultComparableElement(dbo, name));
            } else
                arrayDefaultComparableElements.add(new DefaultComparableElement(dbo, name));
        }
        db.commitTrans();

        CollationComparator comparator = new CollationComparator();

        DefaultComparableElement items[] = new DefaultComparableElement[arrayDefaultComparableElements
                .size()];
        arrayDefaultComparableElements.toArray(items);

        SrSort.sortArray(items, items.length, comparator);
        int index = -1;
        for (int i = 0; i < items.length; i++) {
            if (currentDbo == items[i].object) {
                index = i;
                break;
            }
        }
        LookupDialog lookup = new LookupDialog(comp, title, null, items, index,
                LookupDialog.SELECT_ONE, comparator);
        if (center != null) {
            if (center.x + lookup.getWidth() / 2 > Toolkit.getDefaultToolkit().getScreenSize()
                    .getWidth()) {
                center.x = Math.max(center.x - lookup.getWidth() / 2, lookup.getWidth() / 2);
            }
            if (center.y + lookup.getHeight() / 2 > Toolkit.getDefaultToolkit().getScreenSize()
                    .getHeight()) {
                center.y = Math.max(center.y - lookup.getHeight() / 2, lookup.getHeight() / 2);
            }
            lookup.setLocation(center.x - lookup.getWidth() / 2, center.y - lookup.getHeight() / 2);
        }

        lookup.setVisible(true);
        index = lookup.getSelIndex();
        return (index == -1 ? null : items[index]);
    }
}
