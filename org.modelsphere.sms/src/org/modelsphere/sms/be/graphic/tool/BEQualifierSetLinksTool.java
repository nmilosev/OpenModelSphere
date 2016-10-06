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

package org.modelsphere.sms.be.graphic.tool;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.tool.LinkUnlinkTool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.be.QualifierToolBar;
import org.modelsphere.sms.be.db.DbBEActor;
import org.modelsphere.sms.be.db.DbBEActorQualifier;
import org.modelsphere.sms.be.db.DbBEFlow;
import org.modelsphere.sms.be.db.DbBEFlowQualifier;
import org.modelsphere.sms.be.db.DbBEQualifier;
import org.modelsphere.sms.be.db.DbBEStore;
import org.modelsphere.sms.be.db.DbBEStoreQualifier;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseQualifier;
import org.modelsphere.sms.be.graphic.BEActorBox;
import org.modelsphere.sms.be.graphic.BEFlow;
import org.modelsphere.sms.be.graphic.BEFlowLabel;
import org.modelsphere.sms.be.graphic.BEFlowQualifier;
import org.modelsphere.sms.be.graphic.BEStoreBox;
import org.modelsphere.sms.be.graphic.BEUseCaseBox;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSSemanticalObject;

public final class BEQualifierSetLinksTool extends LinkUnlinkTool {
    private static final String kTransName = LocaleMgr.misc.getString("LinkQualifier");

    public BEQualifierSetLinksTool(String text, Image image) {
        super(0, text, image);
        setToolBar(QualifierToolBar.KEY);
        setAlwaysVisible(true);
    }

    protected Cursor loadDefaultCursor() {
        return AwtUtil.loadCursor(BEModule.class, "international/resources/setlink.gif", new Point(
                1, 1), "box"); // NOT
        // LOCALIZABLE,
        // not
        // yet
    }

    protected void setlink(GraphicComponent gc, Object[] linkObjs) {
        if (gc == null || linkObjs == null || linkObjs.length == 0)
            return;
        try {
            // We must preserve actual assignment values for selected elements
            if (gc instanceof BEUseCaseBox) {
                BEUseCaseBox box = (BEUseCaseBox) gc;
                DbBEUseCase usecase = (DbBEUseCase) box.getSemanticalObject();
                usecase.getDb().beginWriteTrans(kTransName);
                setLinksFor(usecase, DbBEUseCaseQualifier.metaClass,
                        DbBEUseCaseQualifier.fQualifier, linkObjs);
                usecase.getDb().commitTrans();
            } else if (gc instanceof BEActorBox) {
                BEActorBox box = (BEActorBox) gc;
                DbBEActor actor = (DbBEActor) box.getSemanticalObject();
                actor.getDb().beginWriteTrans(kTransName);
                setLinksFor(actor, DbBEActorQualifier.metaClass, DbBEActorQualifier.fQualifier,
                        linkObjs);
                actor.getDb().commitTrans();
            } else if (gc instanceof BEStoreBox) {
                BEStoreBox box = (BEStoreBox) gc;
                DbBEStore store = (DbBEStore) box.getSemanticalObject();
                store.getDb().beginWriteTrans(kTransName);
                setLinksFor(store, DbBEStoreQualifier.metaClass, DbBEStoreQualifier.fQualifier,
                        linkObjs);
                store.getDb().commitTrans();
            } else if (gc instanceof BEFlow || gc instanceof BEFlowLabel
                    || gc instanceof BEFlowQualifier) {
                BEFlow line = null;
                if (gc instanceof BEFlow)
                    line = (BEFlow) gc;
                else if (gc instanceof BEFlowLabel) {
                    BEFlowLabel label = (BEFlowLabel) gc;
                    line = (BEFlow) label.getLine();
                } else if (gc instanceof BEFlowQualifier) {
                    BEFlowQualifier qualifier = (BEFlowQualifier) gc;
                    line = (BEFlow) qualifier.getLine();
                }
                if (line == null)
                    return;
                DbBEFlow flow = (DbBEFlow) line.getSemanticalObject();
                flow.getDb().beginWriteTrans(kTransName);
                setLinksFor(flow, DbBEFlowQualifier.metaClass, DbBEFlowQualifier.fQualifier,
                        linkObjs);
                flow.getDb().commitTrans();
            } else
                Toolkit.getDefaultToolkit().beep();
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

    private void setLinksFor(DbObject selDbo, MetaClass assignMetaClass, MetaField linkMetaField,
            Object[] linkObjs) throws DbException {
        // first link missing resources
        for (int i = 0; i < linkObjs.length; i++) {
            if (!(linkObjs[i] instanceof DefaultComparableElement))
                continue;
            DbBEQualifier qualifier = (DbBEQualifier) ((DefaultComparableElement) linkObjs[i]).object;
            // check if already linked
            DbEnumeration dbEnum = selDbo.getComponents().elements(assignMetaClass);
            boolean found = false;
            while (dbEnum.hasMoreElements()) {
                DbObject assignQualifier = dbEnum.nextElement();
                if (assignQualifier.get(linkMetaField) == qualifier) {
                    found = true;
                    break;
                }
            }
            dbEnum.close();
            if (!found) {
                selDbo.createComponent(assignMetaClass, new Object[] { qualifier },
                        new Class[] { DbSMSSemanticalObject.class });
            }
        }
        // remove invalid resources
        DbEnumeration dbEnum = selDbo.getComponents().elements(assignMetaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject assignQualifier = dbEnum.nextElement();
            DbObject qualifier = (DbObject) assignQualifier.get(linkMetaField);
            boolean found = false;
            for (int i = 0; !found && i < linkObjs.length; i++) {
                if (!(linkObjs[i] instanceof DefaultComparableElement))
                    continue;
                if (((DefaultComparableElement) linkObjs[i]).object != qualifier)
                    continue;
                found = true;
            }
            if (!found)
                assignQualifier.remove();
        }
        dbEnum.close();
    }

}
