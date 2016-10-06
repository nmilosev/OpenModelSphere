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
package org.modelsphere.sms.be.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SelectionChangedEvent;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorQualifier;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowQualifier;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreQualifier;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseQualifier;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.international.LocaleMgr;

/**
 * @author Nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class LinkQualifiersAndResourcesAction extends AbstractApplicationAction {

    public static String LINK_QUALIFIER_OR_RESOURCE = LocaleMgr.action.getString("LinkQualOrRsrc");
    public static String LINK_ALREADY_EXISTS_MESSAGE = LocaleMgr.action.getString("LinkExist");

    LinkQualifiersAndResourcesAction() {
        super(LINK_QUALIFIER_OR_RESOURCE);
        // this.setVisibilityMode(visibilityMode)
    }

    public void performAction(DbObject[] dragObjects, DbObject processOrActorOrFlows, Point location)
            throws DbException {

        // //
        // validate pre-condition

        if (!(processOrActorOrFlows instanceof DbBEUseCase
                || processOrActorOrFlows instanceof DbBEActor
                || processOrActorOrFlows instanceof DbBEStore || processOrActorOrFlows instanceof DbBEFlow))
            return;

        // //
        // acquire target object

        // //
        // take objects one by one and attempt linking to the
        // processOrActorOrFlow

        processOrActorOrFlows.getDb().beginWriteTrans(LINK_QUALIFIER_OR_RESOURCE);
        for (int i = 0; i < dragObjects.length; i++) {
            if (dragObjects[i] instanceof DbBEResource) {
                DbBEResource resource = (DbBEResource) dragObjects[i];
                DbEnumeration dbEnum = processOrActorOrFlows.getComponents().elements(
                        DbBEUseCaseResource.metaClass);
                boolean found = false;
                while (dbEnum.hasMoreElements()) {
                    DbBEUseCaseResource assignRess = (DbBEUseCaseResource) dbEnum.nextElement();
                    if (assignRess.getResource() == resource) {
                        found = true;
                        break;
                    }
                }
                dbEnum.close();
                if (!found) {
                    if (processOrActorOrFlows instanceof DbBEUseCase)
                        new DbBEUseCaseResource(processOrActorOrFlows,
                                (DbBEResource) dragObjects[i]);
                } else {
                    JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                            LINK_ALREADY_EXISTS_MESSAGE, ApplicationContext.getApplicationName(),
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (dragObjects[i] instanceof DbBEQualifier) {
                if (processOrActorOrFlows instanceof DbBEUseCase) {
                    DbBEQualifier qualifier = (DbBEQualifier) dragObjects[i];
                    DbEnumeration dbEnum = processOrActorOrFlows.getComponents().elements(
                            DbBEUseCaseQualifier.metaClass);
                    boolean found = false;
                    while (dbEnum.hasMoreElements()) {
                        DbBEUseCaseQualifier assignRess = (DbBEUseCaseQualifier) dbEnum
                                .nextElement();
                        if (assignRess.getQualifier() == qualifier) {
                            found = true;
                            break;
                        }
                    }
                    dbEnum.close();
                    if (!found) {
                        new DbBEUseCaseQualifier(processOrActorOrFlows,
                                (DbBEQualifier) dragObjects[i]);
                    } else
                        JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                                LINK_ALREADY_EXISTS_MESSAGE, ApplicationContext.getApplicationName(),
                                JOptionPane.INFORMATION_MESSAGE);
                } else if (processOrActorOrFlows instanceof DbBEFlow) {
                    DbBEQualifier qualifier = (DbBEQualifier) dragObjects[i];
                    DbEnumeration dbEnum = processOrActorOrFlows.getComponents().elements(
                            DbBEFlowQualifier.metaClass);
                    boolean found = false;
                    while (dbEnum.hasMoreElements()) {
                        DbBEFlowQualifier assignRess = (DbBEFlowQualifier) dbEnum.nextElement();
                        if (assignRess.getQualifier() == qualifier) {
                            found = true;
                            break;
                        }
                    }
                    dbEnum.close();
                    if (!found)
                        new DbBEFlowQualifier(processOrActorOrFlows, (DbBEQualifier) dragObjects[i]);
                    else
                        JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                                LINK_ALREADY_EXISTS_MESSAGE, ApplicationContext.getApplicationName(),
                                JOptionPane.INFORMATION_MESSAGE);
                } else if (processOrActorOrFlows instanceof DbBEActor) {
                    DbBEQualifier qualifier = (DbBEQualifier) dragObjects[i];
                    DbEnumeration dbEnum = processOrActorOrFlows.getComponents().elements(
                            DbBEActorQualifier.metaClass);
                    boolean found = false;
                    while (dbEnum.hasMoreElements()) {
                        DbBEActorQualifier assignRess = (DbBEActorQualifier) dbEnum.nextElement();
                        if (assignRess.getQualifier() == qualifier) {
                            found = true;
                            break;
                        }
                    }
                    dbEnum.close();
                    if (!found)
                        new DbBEActorQualifier(processOrActorOrFlows,
                                (DbBEQualifier) dragObjects[i]);
                    else
                        JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                                LINK_ALREADY_EXISTS_MESSAGE, ApplicationContext.getApplicationName(),
                                JOptionPane.INFORMATION_MESSAGE);
                } else if (processOrActorOrFlows instanceof DbBEStore) {
                    DbBEQualifier qualifier = (DbBEQualifier) dragObjects[i];
                    DbEnumeration dbEnum = processOrActorOrFlows.getComponents().elements(
                            DbBEStoreQualifier.metaClass);
                    boolean found = false;
                    while (dbEnum.hasMoreElements()) {
                        DbBEStoreQualifier assignRess = (DbBEStoreQualifier) dbEnum.nextElement();
                        if (assignRess.getQualifier() == qualifier) {
                            found = true;
                            break;
                        }
                    }
                    dbEnum.close();
                    if (!found)
                        new DbBEStoreQualifier(processOrActorOrFlows,
                                (DbBEQualifier) dragObjects[i]);
                    else
                        JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                                LINK_ALREADY_EXISTS_MESSAGE, ApplicationContext.getApplicationName(),
                                JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        processOrActorOrFlows.getDb().commitTrans();
    }

    protected void doActionPerformed() {
        super.doActionPerformed();
    }

    protected void doActionPerformed(ActionEvent e) {
        super.doActionPerformed(e);
    }

    protected String getPreferenceID() {
        return super.getPreferenceID();
    }

    public String getText(Object[] selObjects) {
        return super.getText(selObjects);
    }

    public boolean isEnabled(Object[] selObjects) {
        return super.isEnabled(selObjects);
    }

    public void putValue(String key, Object newValue) {
        super.putValue(key, newValue);
    }

    public void selectionChanged(SelectionChangedEvent e) throws DbException {
        super.selectionChanged(e);
    }

    public void setEnabled(boolean newValue) {
        super.setEnabled(newValue);
    }

    protected void setPreferenceID(String id) {
        super.setPreferenceID(id);
    }
}
