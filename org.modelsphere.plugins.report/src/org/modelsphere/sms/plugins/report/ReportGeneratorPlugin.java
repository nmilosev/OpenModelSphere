/*************************************************************************

Copyright (C) 2010 Grandite

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

package org.modelsphere.sms.plugins.report;

// JDK
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.forward.GenerateInFileInfo;
import org.modelsphere.sms.plugins.report.model.ReportModel;
import org.modelsphere.sms.plugins.report.screen.ReportPropertiesFrame;

public class ReportGeneratorPlugin implements Plugin2 {

    public ReportGeneratorPlugin() {
    }

    // ***************************************************************************
    // Plugin methods

    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    // for debug only
    private GenerateInFileInfo m_generateInFileInfo = null;

    public GenerateInFileInfo getGenerateInFileInfo() {
        if (m_generateInFileInfo == null) {
            m_generateInFileInfo = new GenerateInFileInfo();
        }

        return m_generateInFileInfo;
    }

    public Class[] getSupportedClasses() {
        return null;
        //return new Class[]{DbObject.class/*DbORDataModel.class, DbSMSProject.class, DbOOClassModel.class*/};
    }

    public PluginSignature getSignature() {
        return null;
    }

    public void execute(ActionEvent actEvent) throws Exception {
        DbObject[] selectedObjects = ApplicationContext.getFocusManager()
                .getSelectedSemanticalObjects();
        DbObject[] newSelectedObjects;

        ArrayList list = new ArrayList();
        if (selectedObjects != null && selectedObjects.length == 0) {
            JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(), LocaleMgr.misc
                    .getString("noSelection"), LocaleMgr.misc.getString("GenerateReport"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // BEGIN TRANS
        selectedObjects[0].getProject().getDb().beginTrans(Db.READ_TRANS);

        // REMOVE UNNECESSARY SEMANTICAL OBJECTS
        // fill ArrayList from SelectedSemanticalObjects (Explorer)
        for (int i = 0; i < selectedObjects.length; i++) {
            list.add(selectedObjects[i]);
        }
        // remove unncessary objects from the ArrayList
        removeUnnecessaryObjects(list);
        newSelectedObjects = new DbObject[list.size()];
        // copy ArrayList content into new Array
        System.arraycopy(list.toArray(), 0, newSelectedObjects, 0, list.size());

        // COMMIT TRANS
        selectedObjects[0].getProject().getDb().commitTrans();

        ReportModel model = new ReportModel(newSelectedObjects);

        ReportPropertiesFrame frame = new ReportPropertiesFrame(ApplicationContext
                .getDefaultMainFrame(), true, model);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(ApplicationContext.getDefaultMainFrame());
        frame.setVisible(true);
        if (frame.returnCode == ReportPropertiesFrame.GENERATE_REPORT) {
            DefaultController controller = new DefaultController(LocaleMgr.misc
                    .getString("GenerateReport"), false, null);
            controller.start(new ReportGeneratorWorker(frame.getReportModel()));
        }
    }

    // Plugin methods - End
    // ***************************************************************************

    private void removeUnnecessaryObjects(ArrayList dbObjects) throws DbException {
        boolean changed = false;

        if (dbObjects.size() < 2)
            return;

        for (int i = 1; i < dbObjects.size(); i++) {
            if (isCompositeOf((DbObject) dbObjects.get(0), (DbObject) dbObjects.get(i))) {
                dbObjects.remove(i);
                changed = true;
            } else if (isCompositeOf((DbObject) dbObjects.get(i), (DbObject) dbObjects.get(0))) {
                dbObjects.remove(0);
                changed = true;
            }
        }

        if (changed)
            removeUnnecessaryObjects(dbObjects);
    }

    private boolean isCompositeOf(DbObject dbo1, DbObject dbo2) throws DbException {
        while (dbo2 != null) {
            if (dbo1 == dbo2)
                return true;

            dbo2 = dbo2.getComposite();
        }

        return false;
    }
    
    @Override
    public boolean doListenSelection() {
        return false;
    }

	@Override
	public OptionGroup getOptionGroup() {
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		return null;
	}
}
