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
import java.awt.Point;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;

public final class BEFinalStateTool extends BEUseCaseTool {

    private Cursor cursor;

    public BEFinalStateTool(String text) {
        super(text, GraphicUtil.loadImage(DbSMSStereotype.class, "resources/end-small.gif"));
        cursor = loadDefaultCursor();
    }

    public Cursor getCursor() {
        return cursor;
    }

    protected Cursor loadDefaultCursor() {
        return AwtUtil.loadCursor(DbSMSStereotype.class, "resources/end-small.gif",
                new Point(8, 8), "end", true); // NOT
        // LOCALIZABLE,
        // not
        // yet
    }

    public String getText(DbObject notation) {
        return LocaleMgr.action.getString("FinalStateCreation");
    }

    protected final void createBox(int x, int y) {
        super.createBox(x, y);
        if (semObj == null || !(semObj instanceof DbBEUseCase))
            return;

        DbSMSStereotype start = null;
        DbBEUseCase useCase = (DbBEUseCase) semObj;
        try {
            useCase.getDb().beginWriteTrans("");
            DbEnumeration dbEnum = useCase.getProject().getComponents().elements(
                    DbSMSUmlExtensibility.metaClass);
            while (dbEnum.hasMoreElements()) {
                DbObject dbo = dbEnum.nextElement();
                DbEnumeration enum2 = dbo.getComponents().elements(DbSMSStereotype.metaClass);
                while (enum2.hasMoreElements()) {
                    DbSMSStereotype stereotype = (DbSMSStereotype) enum2.nextElement();
                    if (stereotype.getStereotypeMetaClass()
                            .isAssignableFrom(useCase.getMetaClass())) {
                        if (stereotype.isBuiltIn())
                            if (stereotype.getName().equals("end")) {
                                useCase.setUmlStereotype(stereotype);
                                useCase.setName("End");
                            }
                    }
                }
                enum2.close();
            }
            dbEnum.close();
            useCase.getDb().commitTrans();
        } catch (DbException e) {
        }
    }

}
