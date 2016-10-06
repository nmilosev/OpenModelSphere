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
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.tool.LinkUnlinkTool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.be.BEModule;
import org.modelsphere.sms.be.ResourceToolBar;
import org.modelsphere.sms.be.db.DbBEResource;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.DbBEUseCaseResource;
import org.modelsphere.sms.be.graphic.BEUseCaseBox;
import org.modelsphere.sms.be.international.LocaleMgr;

public final class BEResourceSetLinksTool extends LinkUnlinkTool {
    private static final String kTransName = LocaleMgr.misc.getString("LinkResource");

    public BEResourceSetLinksTool(String text, Image image) {
        super(0, text, image);
        setToolBar(ResourceToolBar.KEY);
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
                // first link missing resources
                for (int i = 0; i < linkObjs.length; i++) {
                    if (!(linkObjs[i] instanceof DefaultComparableElement))
                        continue;
                    DbBEResource resource = (DbBEResource) ((DefaultComparableElement) linkObjs[i]).object;
                    // check if already linked
                    DbEnumeration dbEnum = usecase.getComponents().elements(
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
                    if (!found)
                        new DbBEUseCaseResource(usecase, resource);
                }
                // remove invalid resources
                DbEnumeration dbEnum = usecase.getComponents().elements(
                        DbBEUseCaseResource.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbBEUseCaseResource assignRess = (DbBEUseCaseResource) dbEnum.nextElement();
                    DbObject resource = assignRess.getResource();
                    boolean found = false;
                    for (int i = 0; !found && i < linkObjs.length; i++) {
                        if (!(linkObjs[i] instanceof DefaultComparableElement))
                            continue;
                        if (((DefaultComparableElement) linkObjs[i]).object != resource)
                            continue;
                        found = true;
                    }
                    if (!found)
                        assignRess.remove();
                }
                dbEnum.close();
                usecase.getDb().commitTrans();
            } else
                Toolkit.getDefaultToolkit().beep();
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

}
