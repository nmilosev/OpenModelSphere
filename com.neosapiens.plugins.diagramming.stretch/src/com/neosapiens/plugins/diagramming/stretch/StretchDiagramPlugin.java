/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/

package com.neosapiens.plugins.diagramming.stretch;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.modelsphere.jack.awt.JackMenu;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.*;
import org.modelsphere.jack.srtool.actions.PluginDefaultAction;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.actions.SMSActionsStore;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSGraphicalObject;

/**
 * 
 * This is the entry point of the Stretch Diagram plugin
 * 
 * @author Marco Savard
 * 
 */
public class StretchDiagramPlugin implements Plugin2 {
	
	public StretchDiagramPlugin() {
	}

    public PluginSignature getSignature() {
        return null;
    }

    public OptionGroup getOptionGroup() {
        return null;
    }

    public PluginAction getPluginAction() {
        return new PluginDefaultAction(this);
    }

    public Class[] getSupportedClasses() {
        return new Class[] { DbSMSDiagram.class };
    }

    public String installAction(DefaultMainFrame mf, MainFrameMenu menu) {
        JackMenu diagramMenu = getDiagramMenu(menu);
        PluginAction action = getPluginAction();
        diagramMenu.insertAfter(action, SMSActionsStore.SET_DRAWING_AREA);
        return null;
    }
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }

    public void execute(ActionEvent ev) throws Exception {
        //list selected diagrams or focused diagram
        List<DbSMSDiagram> diagramList = new ArrayList<DbSMSDiagram>();

        //add selected diagrams
        FocusManager focus = FocusManager.getSingleton();
        final DbObject[] selectedobjs = focus.getSelectedSemanticalObjects();

        if (selectedobjs != null) {
            for (DbObject o : selectedobjs) {
                if (o instanceof DbSMSDiagram) {
                    diagramList.add((DbSMSDiagram) o);
                }
            }
        } //end if

        //add focused diagram
        ApplicationDiagram diag = focus.getActiveDiagram();
        DbObject go = (diag == null) ? null : diag.getDiagramGO();
        if (go instanceof DbSMSDiagram) {
            diagramList.add((DbSMSDiagram) go);
        }

        if (!diagramList.isEmpty()) {
            stretchDiagrams(diagramList);
        }

    } //end execute()

    //
    // private methods
    //
    private void stretchDiagrams(List<DbSMSDiagram> diagramList) {
        try {
            DbObject dbo = diagramList.get(0);
            String transaction = LocaleMgr.misc.getString("StretchDiagram");
            dbo.getDb().beginTrans(Db.WRITE_TRANS, transaction);

            for (DbSMSDiagram diagram : diagramList) {
                spreadDiagram(diagram);
            } //end for

            dbo.getDb().commitTrans();
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
        }
    }

    private static final String DIAGRAM = LocaleMgr.misc.getString("diagram");

    private JackMenu getDiagramMenu(MainFrameMenu menuManager) {
        JMenu toolMenu = menuManager.getMenuForKey(MainFrameMenu.MENU_EDIT);
        JackMenu diagramMenu = null;

        int nbItems = toolMenu.getItemCount();
        for (int i = 0; i < nbItems; i++) {
            JMenuItem item = toolMenu.getItem(i);
            if (item instanceof JackMenu) {
                JackMenu menu = (JackMenu) item;
                String text = menu.getText();
                if (DIAGRAM.equals(text)) {
                    diagramMenu = menu;
                    break;
                }
            } //end if
        } //end for

        return diagramMenu;
    } //end getDiagramMenu()

    private void spreadDiagram(DbSMSDiagram diagGO) throws DbException {
        final double SPREAD_FACTOR = 1.2;

        //change page's print scale
        double scale = diagGO.getPrintScale();
        scale /= SPREAD_FACTOR;
        diagGO.setPrintScale((int) Math.round(scale));

        //re-position each diagram components
        DbRelationN relN = diagGO.getComponents();
        DbEnumeration enu = relN.elements();

        while (enu.hasMoreElements()) {
            DbObject dbo = enu.nextElement();
            if (dbo instanceof DbSMSGraphicalObject) {
                DbSMSGraphicalObject go = (DbSMSGraphicalObject) dbo;
                Rectangle r0 = go.getRectangle();
                if (r0 != null) {
                    int x = (int) (r0.x * SPREAD_FACTOR);
                    int y = (int) (r0.y * SPREAD_FACTOR);
                    int w = (int) (r0.width * SPREAD_FACTOR);
                    int h = (int) (r0.height * SPREAD_FACTOR);
                    Rectangle r1 = new Rectangle(x, y, w, h);
                    go.setRectangle(r1);
                }
            } //end if
        } //end while
        enu.close();

    } //end spreadDiagram()
}
