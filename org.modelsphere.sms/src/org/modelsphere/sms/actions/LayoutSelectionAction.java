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

package org.modelsphere.sms.actions;

import java.lang.reflect.Constructor;

import javax.swing.Action;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOODiagram;

public final class LayoutSelectionAction extends AbstractApplicationAction implements
        SelectionActionListener {

    public LayoutSelectionAction() {
        super(LocaleMgr.action.getString("layoutAll"));
    }

    protected final void doActionPerformed() {
        DbObject diagGO = null;
        ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
        if (diag != null) {
            diagGO = diag.getDiagramGO();
        } else if (diag == null) {
            DbObject dbo[] = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
            if (dbo.length > 0)
                if (dbo[0] instanceof DbSMSDiagram)
                    diagGO = dbo[0];
        }

        if (diag == null || diagGO == null)
            return;

        GraphicComponent[] selComps = diag.getSelectedComponents();
        if (selComps.length == 0)
            selComps = null;

        try {
            Class claz = null;
            String key = (selComps == null) ? "layoutAll" : "layoutSelection";
        	String tx = LocaleMgr.action.getString(key);
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, tx); 
            
            try {
                if (diagGO instanceof DbOODiagram)
                    claz = Class.forName("org.modelsphere.sms.oo.java.graphic.JVDiagramLayout");
                else if (diagGO instanceof DbBEDiagram)
                    claz = Class.forName("org.modelsphere.sms.be.graphic.BEDiagramLayout");
                else
                    //DbORDiagram
                    claz = Class.forName("org.modelsphere.sms.or.graphic.ORDiagramLayout");

                Constructor constr = claz.getConstructor(new Class[] { DbObject.class,
                        GraphicComponent[].class });
                constr.newInstance(new Object[] { diagGO, selComps });
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
            diagGO.getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public void performAction(GraphicComponent[] comps, DbSMSDiagram diagGO) {
        if (diagGO == null)
            return;

        if (comps.length == 0)
            comps = null;
        try {
            Class claz = null;
            String key = (comps == null) ? "layoutAll" : "layoutSelection";
            String tx = LocaleMgr.action.getString(key);
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, tx);
            
            try {
                if (diagGO instanceof DbOODiagram)
                    claz = Class.forName("org.modelsphere.sms.oo.java.graphic.JVDiagramLayout");
                else if (diagGO instanceof DbBEDiagram)
                    claz = Class.forName("org.modelsphere.sms.be.graphic.BEDiagramLayout");
                else
                    //DbORDiagram
                    claz = Class.forName("org.modelsphere.sms.or.graphic.ORDiagramLayout");

                Constructor constr = claz.getConstructor(new Class[] { DbObject.class,
                        GraphicComponent[].class });
                constr.newInstance(new Object[] { diagGO, comps });
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
            diagGO.getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public void performAction(GraphicComponent[] comps, ApplicationDiagram diag) {
        if (comps.length == 0)
            comps = null;
        DbObject diagGO = diag.getDiagramGO();
        try {
            Class claz = null;
            String key = (comps == null) ? "layoutAll" : "layoutSelection";
            String tx = LocaleMgr.action.getString(key);
            diagGO.getDb().beginTrans(Db.WRITE_TRANS, tx);
            
            try {
                if (diagGO instanceof DbOODiagram)
                    claz = Class.forName("org.modelsphere.sms.oo.java.graphic.JVDiagramLayout");
                else if (diagGO instanceof DbBEDiagram)
                    claz = Class.forName("org.modelsphere.sms.be.graphic.BEDiagramLayout");
                else
                    //DbORDiagram
                    claz = Class.forName("org.modelsphere.sms.or.graphic.ORDiagramLayout");

                Constructor constr = claz.getConstructor(new Class[] { DbObject.class,
                        GraphicComponent[].class });
                constr.newInstance(new Object[] { diagGO, comps });
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
            diagGO.getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        setEnabled(false);
        ApplicationDiagram diag = ApplicationContext.getFocusManager().getActiveDiagram();
        if (diag != null) {
            DbObject dbo = diag.getDiagramGO();
            if (dbo instanceof DbBEDiagram) {
                DbBEDiagram diagram = (DbBEDiagram) dbo;
                if (TerminologyUtil.getInstance().isUML(diagram)) {
                    setEnabled(false);
                    return;
                }
            }
        }

        setEnabled(true);
        String key = (diag.getSelectedComponents().length == 0) ? "layoutAll" : "layoutSelection";
    	String tx = LocaleMgr.action.getString(key);
    	putValue(Action.NAME, tx);
    }
}
