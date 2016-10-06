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

package org.modelsphere.sms.oo.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.awt.JackPopupMenu;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.db.DbSMSUmlConstraint;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.db.util.Extensibility;
import org.modelsphere.sms.oo.db.DbOOAssociationEnd;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.graphic.RoleLabel;
import org.modelsphere.sms.oo.international.LocaleMgr;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.util.AnyAdt;

public final class LinkConstraintAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String kLinkUmlConstraint = LocaleMgr.action
            .getString("LinkUMLConstraint");

    LinkConstraintAction() {
        super(kLinkUmlConstraint);
    }

    public JMenuItem createItem(JackPopupMenu jackPopupMenu) {
        JMenuItem item = new JMenu(kLinkUmlConstraint);
        jackPopupMenu.add(item);
        return item;
    }

    public void init(JMenuItem item, Object[] selObjects) {
        boolean enabled = false;

        if (selObjects.length > 0) {
            try {
                if (selObjects[0] instanceof DbObject) {
                    DbObject dbo = (DbObject) selObjects[0];
                    Db db = dbo.getDb();
                    db.beginReadTrans();
                    DbSMSProject proj = (DbSMSProject) dbo
                            .getCompositeOfType(DbSMSProject.metaClass);
                    MetaClass mc = dbo.getMetaClass();
                    enabled = addUMLConstraints(item, proj, mc);
                    db.commitTrans();
                } else if (selObjects[0] instanceof RoleLabel) {
                    RoleLabel label = (RoleLabel) selObjects[0];
                    DbObject dbo = label.getSemanticalObject();
                    Db db = dbo.getDb();
                    db.beginReadTrans();

                    if (dbo instanceof DbOODataMember) {
                        DbOOAssociationEnd end = ((DbOODataMember) dbo).getAssociationEnd();
                        if (end != null) {
                            dbo = end;
                        }
                    } //end if

                    DbSMSProject proj = (DbSMSProject) dbo
                            .getCompositeOfType(DbSMSProject.metaClass);
                    MetaClass mc = dbo.getMetaClass();
                    enabled = addUMLConstraints(item, proj, mc);
                    db.commitTrans();
                } //end if
            } catch (DbException ex) {
                enabled = false;
            } //end try
        } //end if

        item.setEnabled(enabled);
    } //end init()

    private boolean addUMLConstraints(JMenuItem item, DbSMSProject proj, MetaClass mc)
            throws DbException {
        boolean enabled = false;
        List<DbSMSUmlConstraint> umlConstraints = getConstraints(proj, mc);

        for (DbSMSUmlConstraint constr : umlConstraints) {
            String name = constr.getName();
            JMenuItem subItem = new JMenuItem(this);
            subItem.setText(name);
            item.add(subItem);
            enabled = true;
        } //end for

        return enabled;
    }

    // Check that all objects in the selection are DataMember/Method/Parameter, and belong to a single project.
    public final void updateSelectionAction() throws DbException {
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        boolean state = (semObjs.length > 0 && ApplicationContext.getFocusManager()
                .getCurrentProject() != null);
        for (int i = 0; i < semObjs.length && state; i++) {
            state = (AnyAdt.getTypeField(semObjs[i]) != null);
        }
        setEnabled(state);
    }

    protected final void doActionPerformed(ActionEvent e) {

        String constraint = e.getActionCommand();
        DbObject[] dbos = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        String txName = kLinkUmlConstraint;

        try {
            PluginServices.multiDbBeginTrans(Db.WRITE_TRANS, txName);

            for (DbObject dbo : dbos) {
                if (dbo instanceof DbOODataMember) {
                    DbOODataMember field = (DbOODataMember) dbo;
                    DbSMSProject proj = (DbSMSProject) dbo
                            .getCompositeOfType(DbSMSProject.metaClass);
                    DbRelationN relN = proj.getComponents();
                    DbEnumeration enu = relN.elements(DbSMSUmlExtensibility.metaClass);
                    while (enu.hasMoreElements()) {
                        DbSMSUmlExtensibility umlExtensibility = (DbSMSUmlExtensibility) enu
                                .nextElement();
                        DbSMSUmlConstraint constr = Extensibility.findConstraintByName(
                                umlExtensibility, constraint);
                        link(field, constr);
                    } //end while
                    enu.close();

                } //end if
            } //end for

            PluginServices.multiDbCommitTrans();
        } catch (DbException ex) {
            JFrame frame = ApplicationContext.getDefaultMainFrame();
            ExceptionHandler.processUncatchedException(frame, ex);
        }

    } //end doActionPerformed()

    private void link(DbOODataMember field, DbSMSUmlConstraint constr) throws DbException {
        field.addToUmlConstraints(constr);

    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }

    //
    // private methods
    //

    private List<DbSMSUmlConstraint> getConstraints(DbSMSProject proj, MetaClass metaclass)
            throws DbException {
        List<DbSMSUmlConstraint> umlConstraints = new ArrayList<DbSMSUmlConstraint>();

        DbRelationN relN = proj.getComponents();
        DbEnumeration enu = relN.elements(DbSMSUmlExtensibility.metaClass);
        while (enu.hasMoreElements()) {
            DbSMSUmlExtensibility ext = (DbSMSUmlExtensibility) enu.nextElement();
            DbRelationN relN2 = ext.getComponents();
            DbEnumeration enu2 = relN2.elements(DbSMSUmlConstraint.metaClass);
            while (enu2.hasMoreElements()) {
                DbSMSUmlConstraint constr = (DbSMSUmlConstraint) enu2.nextElement();
                MetaClass mc = constr.getUmlConstraintMetaClass();
                boolean assignable = mc.isAssignableFrom(metaclass);

                if (assignable) {
                    umlConstraints.add(constr);
                }

            } //end while
            enu2.close();
        } //end while
        enu.close();

        Collections.sort(umlConstraints, new Comparator<DbSMSUmlConstraint>() {

            @Override
            public int compare(DbSMSUmlConstraint o1, DbSMSUmlConstraint o2) {
                int comparison;

                try {
                    comparison = o1.getName().compareTo(o2.getName());
                } catch (DbException ex) {
                    comparison = 0;
                }
                return comparison;
            }

        });
        return umlConstraints;
    }

}
