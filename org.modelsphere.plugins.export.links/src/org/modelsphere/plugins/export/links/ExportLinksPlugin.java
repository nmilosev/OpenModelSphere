/*************************************************************************

Copyright (C) 2009 neosapiens inc./MSSS

This file is part of Link Report plug-in.

Link Report plug-in is free software; you can redistribute it and/or modify
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

Link Report plug-in has been developed by neosapiens inc. for
the purposes of the Ministère de la Santé et des Services Sociaux
du Québec (MSSS).
 
You can reach neosapiens inc. at: 
  http://www.neosapiens.com

 **********************************************************************/
package org.modelsphere.plugins.export.links;

import java.awt.event.ActionEvent;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.plugins.export.links.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.or.db.DbORDataModel;

public class ExportLinksPlugin implements Plugin2 {

    @Override
    public PluginSignature getSignature() {
        return null;
    }

    @Override
    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        Class<?>[] classes = new Class[] { DbSMSProject.class, DbORDataModel.class,
                DbSMSLinkModel.class };
        return classes;
    }

    @Override
    public OptionGroup getOptionGroup() {
        return null;
    }

    @Override
    public PluginAction getPluginAction() {
        String actionName = LocaleMgr.misc.getString("GenerateLinkReports") + "...";
        PluginAction action = new PluginAction(this, actionName);
        return action;
    }

    @Override
    public void execute(ActionEvent ev) throws Exception {

        //open the controller
        String actionName = LocaleMgr.misc.getString("GenerateLinkReports");
        DefaultController controller = new DefaultController(actionName, false, null);
        PluginServices.multiDbBeginTrans(Db.READ_TRANS, null);

        //create and start the worker
        DbObject[] dbos = PluginServices.getSelectedSemanticalObjects();
        Worker worker = new ExportLinksWorker(dbos);
        controller.start(worker);

        PluginServices.multiDbCommitTrans();
    } //end execute()
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }
}
