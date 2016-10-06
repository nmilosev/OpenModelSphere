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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.international.LocaleMgr;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.*;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class PUKLookupDialog {

    private static final String kLookupDlgTitle = LocaleMgr.misc.getString("PUKLookupDialogTitle");
    private static final String kTransactionID = LocaleMgr.misc
            .getString("LinkKeyTransactionIdentifier");

    private DbObject semObj = null;
    protected MetaRelationN[] listRelations;
    protected MetaClass listClass;
    protected MetaRelationship parentRel = null; // relation to 2nd parent for an intersection object.

    public PUKLookupDialog(DbObject dbObject, MetaRelationN[] listRelations, MetaClass listClass,
            MetaRelationship parentRel) {
        semObj = dbObject;
        this.listRelations = listRelations;
        this.listClass = listClass;
        this.parentRel = parentRel;
    }

    public DbObject[] linkAction(boolean[] outValCancel) {
        DbObject[] selObjs = showLinkDialog(outValCancel);
        if (selObjs == null || selObjs.length == 0)
            return selObjs;
        try {
            Db db = semObj.getDb();
            db.beginTrans(Db.WRITE_TRANS, MessageFormat.format(kTransactionID, new Object[] {
                    semObj.getMetaClass().getGUIName(), listRelations[0].getGUIName() }));
            MetaRelationship oppositeRel = listRelations[0].getOppositeRel(null);
            for (int i = 0; i < selObjs.length; i++) {
                DbObject selObj = selObjs[i];
                if (parentRel != null)
                    createLinkComponent(selObj);
                else
                    selObj.set(oppositeRel, semObj);
            }
            db.commitTrans();
        } catch (DbException ex) {
            ExceptionHandler
                    .processUncatchedException(ApplicationContext.getDefaultMainFrame(), ex);
        }
        return selObjs;

    }

    protected void createLinkComponent(DbObject parent) throws DbException {
    }

    protected DbObject[] showLinkDialog(boolean[] outValCancel) {
        SrVector linkObjs = getLinkableObjects();
        if (linkObjs.size() == 0)
            return null;

        CollationComparator comparator = new CollationComparator();
        linkObjs.sort(comparator);
        String title = null;
        if (!(parentRel instanceof MetaChoice)) {
            MetaClass linkClass = (parentRel != null ? parentRel.getOppositeRel(null)
                    .getMetaClass() : listClass);
            title = getLinkDialogTitle(linkClass);
        }
        int[] indices = LookupDialog.selectMany(ApplicationContext.getDefaultMainFrame(), title,
                null, linkObjs.toArray(), -1, comparator);
        if (indices.length == 0)
            outValCancel[0] = true;
        DbObject[] selObjs = new DbObject[indices.length];
        for (int i = 0; i < selObjs.length; i++)
            selObjs[i] = (DbObject) ((DefaultComparableElement) linkObjs.elementAt(indices[i])).object;
        return selObjs;
    }

    protected SrVector getLinkableObjects() {
        SrVector linkObjs = new SrVector(100);
        try {
            semObj.getDb().beginTrans(Db.READ_TRANS);
            // Get in <listObjs> all the objects already in the ListVIew
            DbRelationN dbRelN = (DbRelationN) semObj.get(listRelations[0]);
            int nb = dbRelN.size();
            SrVector listObjs = new SrVector(nb);
            for (int i = 0; i < nb; i++) {
                DbObject dbo = dbRelN.elementAt(i);
                MetaClass dboMc = dbo.getMetaClass();
                if (listClass.isAssignableFrom(dboMc)) {
                    if (parentRel != null) {
                        dbo = (DbObject) dbo.get(parentRel);
                    }
                    listObjs.addElement(dbo);
                }
            }
            // Get in <linkSet> all the linkable objects
            Collection linkSet = getLinkableSet();
            if (linkSet == null) {
                linkSet = new ArrayList();
                DbEnumeration dbEnum = getLinkableEnum();
                if (dbEnum != null) {
                    while (dbEnum.hasMoreElements())
                        linkSet.add(dbEnum.nextElement());
                    dbEnum.close();
                }
            }
            // Now <linkObjs> = <linkSet> - <listObjs>, i.e eliminate the objects already linked
            MetaRelationship oppositeRel = listRelations[0].getOppositeRel(null);
            Iterator iter = linkSet.iterator();
            while (iter.hasNext()) {
                DbObject dbo = (DbObject) iter.next();
                if (dbo == semObj)
                    continue;
                if (parentRel != null || oppositeRel instanceof MetaRelationN) {
                    if (listObjs.contains(dbo)) // fast: SrVector does not use the method equals
                        continue;
                } else {
                    if (dbo.get(oppositeRel) != null)
                        continue;
                }
                String name = getLinkableObjName(dbo);
                if (name != null)
                    linkObjs.addElement(new DefaultComparableElement(dbo, name));
            }
            semObj.getDb().commitTrans();
        } catch (DbException ex) {
            ExceptionHandler
                    .processUncatchedException(ApplicationContext.getDefaultMainFrame(), ex);
        }
        return linkObjs;
    }

    // Overridden
    protected Collection getLinkableSet() throws DbException {
        return null;
    }

    // Overridden
    protected DbEnumeration getLinkableEnum() throws DbException {
        return semObj.getComposite().getComponents().elements(listClass);
    }

    // Last chance filter: return null if object is not linkable
    // Overridden
    protected String getLinkableObjName(DbObject dbo) throws DbException {
        return ApplicationContext.getSemanticalModel().getDisplayText(dbo, PUKLookupDialog.class);
    }

    // If you want to change the dialog list dialog title i.e. in case we have DbObject
    // Overridden
    protected String getLinkDialogTitle(MetaClass linkClass) {
        String sTitle = kLookupDlgTitle;
        String sTerm = "";
        try {
            semObj.getDb().beginReadTrans();
            DbObject obj = semObj.getComposite().getComposite();
            semObj.getDb().commitTrans();
            TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
            obj.getDb().beginReadTrans();
            Terminology terminology = terminologyUtil.findModelTerminology(obj);
            obj.getDb().commitTrans();
            if (terminology != null)
                sTerm = terminology.getTerm(linkClass, true);
            else
                sTerm = linkClass.getGUIName(true, false);
        } catch (DbException dbe) {
            sTerm = linkClass.getGUIName(true, false);
        }

        return MessageFormat.format(kLookupDlgTitle, new Object[] { sTerm.toLowerCase() });
    }

}
