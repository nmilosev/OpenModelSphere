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
package org.modelsphere.sms.oo.java.features;

//Java
import java.awt.Rectangle;
import java.util.Vector;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.LookupDialog;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOOAdt;
import org.modelsphere.sms.oo.db.DbOOAdtGo;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOOAssociationGo;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.DbJVPrimitiveType;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public class JVCreateAssociation {

    private DbSMSClassifierGo dmAdtGo;
    private DbJVDataMember dm;
    private DbJVClass dmAdt;
    private DbJVClass type;
    private DbJVDataMember typeDm;
    private DbJVAssociation assoc = null;
    private String message = null;

    /*
     * IMPORTANT: Do not open a transaction before executing this method.
     */
    public static void doIt(DbSMSClassifierGo dmAdtGo, DbJVDataMember dm) {
        try {
            dm.getDb()
                    .beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("AssociationCreation"));
            JVCreateAssociation o = new JVCreateAssociation(dmAdtGo, dm);
            dm.getDb().commitTrans();
            if (o.message != null)
                ExceptionHandler.showErrorMessage(ApplicationContext.getDefaultMainFrame(),
                        o.message);
        } catch (Exception ex) {
            ExceptionHandler
                    .processUncatchedException(ApplicationContext.getDefaultMainFrame(), ex);
        }
    }

    public JVCreateAssociation(DbSMSClassifierGo dmAdtGo, DbJVDataMember dm) throws DbException {
        this.dmAdtGo = dmAdtGo;
        this.dm = dm;
        dmAdt = (DbJVClass) dm.getComposite();
        if (dmAdt != dmAdtGo.getClassifier())
            return; // should not occur
        if (dm.getAssociationEnd() != null) {
            DbOOAssociationEnd end = dm.getAssociationEnd();
            assoc = (DbJVAssociation) end.getComposite();
            DbOOAssociationEnd oppEnd = end.getOppositeEnd();
            typeDm = (DbJVDataMember) oppEnd.getAssociationMember();
            type = (DbJVClass) typeDm.getComposite();
        } else {
            DbObject dbo = dm.getType();
            if (dbo == null) {
                message = LocaleMgr.message.getString("FieldTypCannotUndefined");
                return;
            }
            if (dbo instanceof DbJVPrimitiveType) {
                message = LocaleMgr.message.getString("FieldTypeCannotPrimitive");
                return;
            }
            if (((DbJVClass) dbo).isCollection()) {
                dbo = dm.getElementType();
                if (dbo == null) {
                    message = LocaleMgr.message.getString("FieldElemTypeCannotUndefined");
                    return;
                }
                if (dbo instanceof DbJVPrimitiveType) {
                    message = LocaleMgr.message.getString("FieldElementTypeCannotPrimitive");
                    return;
                }
            }
            type = (DbJVClass) dbo;

            Vector dms = getMatchingDataMember(type, dmAdt, dm);
            if (dms.size() == 0)
                typeDm = null;
            else if (dms.size() == 1)
                typeDm = (DbJVDataMember) ((DefaultComparableElement) dms.elementAt(0)).object;
            else {
                String title = MessageFormat.format(LocaleMgr.screen
                        .getString("CreateRelationBetween01"), new Object[] { dmAdt.getName(),
                        type.getName() });
                String message = MessageFormat.format(LocaleMgr.screen
                        .getString("MoreThan1FieldFoundChoose1"), new Object[] { dmAdt.getName() });

                // Close the transaction before showing the lookup dialog, and
                // reopen it after.
                // We can do that, until now, we did not begin to modify the
                // database.

                String transName = dm.getDb().getTransName();
                dm.getDb().commitTrans();
                int index = LookupDialog.selectOne(ApplicationContext.getDefaultMainFrame(), title,
                        message, dms.toArray(), -1, null);
                dm.getDb().beginTrans(Db.WRITE_TRANS, transName);
                if (index == -1)
                    return;
                typeDm = (DbJVDataMember) ((DefaultComparableElement) dms.elementAt(index)).object;
            }
        }

        // All the checks are done, we are now ready to modify the database.
        doTrans();
    }

    private void doTrans() throws DbException {
        if (assoc == null) {
            boolean disablenavigability = false;
            if (typeDm == null) {
                typeDm = new DbJVDataMember(type);
                typeDm.setRoleDefaultInitialValues(dm, SMSMultiplicity
                        .getInstance(SMSMultiplicity.OPTIONAL));
                disablenavigability = true;
            }
            assoc = new DbJVAssociation(dm, getMultiplicity(dm), typeDm, getMultiplicity(typeDm));
            if (disablenavigability)
                typeDm.getAssociationEnd().setNavigable(Boolean.FALSE);
        }
        if (assoc.getAssociationGos().size() == 0) {
            DbSMSDiagram diag = (DbSMSDiagram) dmAdtGo.getComposite();
            DbSMSClassifierGo typeGo = (DbSMSClassifierGo) DbGraphic.getFirstGraphicalObject(diag,
                    type);
            if (typeGo == null) {
                typeGo = new DbOOAdtGo(diag, type);
                Rectangle pos = DbGraphic.getRectangle(dmAdtGo);
                pos.y -= pos.height + 20;
                pos.y = Math.max(pos.y, -pos.height / 2);
                typeGo.setRectangle(pos);
            }
            new DbOOAssociationGo(diag, dmAdtGo, typeGo, assoc);
        }
    }

    private SMSMultiplicity getMultiplicity(DbJVDataMember dm) throws DbException {
        if (((DbJVClass) dm.getType()).isCollection()
                || DbJVDataMember.ARRAY_TYPE_USE.equals(dm.getTypeUse()))
            return SMSMultiplicity.getInstance(SMSMultiplicity.MANY);
        return SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL);
    }

    private static Vector getMatchingDataMember(DbJVClass adt, DbJVClass dmOfThisType,
            DbJVDataMember currentDm) throws DbException {
        Vector dms = new Vector();
        DbEnumeration dbEnum = adt.getComponents().elements(DbJVDataMember.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbJVDataMember dm = (DbJVDataMember) dbEnum.nextElement();
            if (currentDm == dm || dm.getAssociationEnd() != null)
                continue;
            DbOOAdt type = dm.getType();
            if (type == null || type instanceof DbJVPrimitiveType)
                continue;
            if (((DbJVClass) type).isCollection())
                type = dm.getElementType();
            if (type == dmOfThisType)
                dms.addElement(new DefaultComparableElement(dm, dm.getName()));
        }
        dbEnum.close();
        return dms;
    }
}
