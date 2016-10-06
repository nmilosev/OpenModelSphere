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

public final class BEQualifierLinkTool extends LinkUnlinkTool {
    private static final String kTransName = LocaleMgr.misc.getString("LinkQualifier");

    public BEQualifierLinkTool(String text, Image image) {
        super(0, text, image);
        setToolBar(QualifierToolBar.KEY);
        setAlwaysVisible(true);
    }

    protected Cursor loadDefaultCursor() {
        return AwtUtil.loadCursor(BEModule.class, "international/resources/addlink.gif", new Point(
                1, 1), "box"); // NOT
        // LOCALIZABLE,
        // not
        // yet
    }

    protected void addLink(GraphicComponent gc, Object[] linkObjs) {
        if (gc == null || linkObjs == null || linkObjs.length == 0)
            return;
        try {
            if (gc instanceof BEUseCaseBox) {
                BEUseCaseBox box = (BEUseCaseBox) gc;
                DbBEUseCase usecase = (DbBEUseCase) box.getSemanticalObject();
                usecase.getDb().beginWriteTrans(kTransName);
                for (int i = 0; i < linkObjs.length; i++) {
                    if (linkObjs[i] instanceof DefaultComparableElement) {
                        DbBEQualifier qualifier = (DbBEQualifier) ((DefaultComparableElement) linkObjs[i]).object;
                        // check if already linked
                        DbEnumeration dbEnum = usecase.getComponents().elements(
                                DbBEUseCaseQualifier.metaClass);
                        boolean found = false;
                        while (dbEnum.hasMoreElements()) {
                            DbBEUseCaseQualifier assignQualifier = (DbBEUseCaseQualifier) dbEnum
                                    .nextElement();
                            if (assignQualifier.getQualifier() == qualifier) {
                                found = true;
                                break;
                            }
                        }
                        dbEnum.close();
                        if (!found)
                            new DbBEUseCaseQualifier(usecase, qualifier);
                    }
                }
                usecase.getDb().commitTrans();
            } else if (gc instanceof BEActorBox) {
                BEActorBox box = (BEActorBox) gc;
                DbBEActor actor = (DbBEActor) box.getSemanticalObject();
                actor.getDb().beginWriteTrans(kTransName);
                for (int i = 0; i < linkObjs.length; i++) {
                    if (linkObjs[i] instanceof DefaultComparableElement) {
                        DbBEQualifier qualifier = (DbBEQualifier) ((DefaultComparableElement) linkObjs[i]).object;
                        // check if already linked
                        DbEnumeration dbEnum = actor.getComponents().elements(
                                DbBEActorQualifier.metaClass);
                        boolean found = false;
                        while (dbEnum.hasMoreElements()) {
                            DbBEActorQualifier assignQualifier = (DbBEActorQualifier) dbEnum
                                    .nextElement();
                            if (assignQualifier.getQualifier() == qualifier) {
                                found = true;
                                break;
                            }
                        }
                        dbEnum.close();
                        if (!found)
                            new DbBEActorQualifier(actor, qualifier);
                    }
                }
                actor.getDb().commitTrans();
            } else if (gc instanceof BEStoreBox) {
                BEStoreBox box = (BEStoreBox) gc;
                DbBEStore store = (DbBEStore) box.getSemanticalObject();
                store.getDb().beginWriteTrans(kTransName);
                for (int i = 0; i < linkObjs.length; i++) {
                    if (linkObjs[i] instanceof DefaultComparableElement) {
                        DbBEQualifier qualifier = (DbBEQualifier) ((DefaultComparableElement) linkObjs[i]).object;
                        // check if already linked
                        DbEnumeration dbEnum = store.getComponents().elements(
                                DbBEStoreQualifier.metaClass);
                        boolean found = false;
                        while (dbEnum.hasMoreElements()) {
                            DbBEStoreQualifier assignQualifier = (DbBEStoreQualifier) dbEnum
                                    .nextElement();
                            if (assignQualifier.getQualifier() == qualifier) {
                                found = true;
                                break;
                            }
                        }
                        dbEnum.close();
                        if (!found)
                            new DbBEStoreQualifier(store, qualifier);
                    }
                }
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
                for (int i = 0; i < linkObjs.length; i++) {
                    if (linkObjs[i] instanceof DefaultComparableElement) {
                        DbBEQualifier qualifier = (DbBEQualifier) ((DefaultComparableElement) linkObjs[i]).object;
                        // check if already linked
                        DbEnumeration dbEnum = flow.getComponents().elements(
                                DbBEFlowQualifier.metaClass);
                        boolean found = false;
                        while (dbEnum.hasMoreElements()) {
                            DbBEFlowQualifier assignQualifier = (DbBEFlowQualifier) dbEnum
                                    .nextElement();
                            if (assignQualifier.getQualifier() == qualifier) {
                                found = true;
                                break;
                            }
                        }
                        dbEnum.close();
                        if (!found)
                            new DbBEFlowQualifier(flow, qualifier);
                    }
                }
                flow.getDb().commitTrans();
            } else
                Toolkit.getDefaultToolkit().beep();
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

}
