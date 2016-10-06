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

package com.neosapiens.plugins.reverse.java;

import java.awt.event.ActionEvent;

import javax.swing.*;

import org.modelsphere.jack.awt.JackMenu;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

import com.neosapiens.plugins.reverse.java.international.LocaleMgr;
import com.neosapiens.plugins.reverse.java.ui.ReverseJavaBytecodeWizard;
import com.neosapiens.plugins.reverse.java.ui.WizardParameters;

public class ReverseJavaBytecodePlugin2 implements Plugin2 {

    @Override
    public PluginSignature getSignature() {
        return null;
    }

    @Override
    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        JMenu importFromMenu = getJavaToolMenu(menuManager);
        PluginAction action = getPluginAction();
        importFromMenu.add(action);
        return null;
    }

    @Override
    public Class<?>[] getSupportedClasses() {
        return null;
    }

    @Override
    public OptionGroup getOptionGroup() {
        return null;
    }

    @Override
    public PluginAction getPluginAction() {
        String name = LocaleMgr.misc.getString("ReverseEngineerJavaBytecodeAction");
        ImageIcon icon = LocaleMgr.misc.getImageIcon("ReverseEngineerJavaBytecodeAction");
        PluginAction action = new PluginAction(this, name);
        action.setIcon(icon);
        return action;
    }

    @Override
    public void execute(ActionEvent ev) throws Exception {

        //get context
        ReverseJavaBytecodeParameters params = new ReverseJavaBytecodeParameters();

        //show wizard
        JFrame frame = ApplicationContext.getDefaultMainFrame();
        ReverseJavaBytecodeWizard wizard = new ReverseJavaBytecodeWizard(params);
        String title = LocaleMgr.misc.getString("ImportJavaBytecode");
        wizard.showOpenDialog(frame, title);

        //get parameters set by user
        params = (ReverseJavaBytecodeParameters) wizard.getParameters();
        WizardParameters.Status status = params.getStatus();

        if (status == WizardParameters.Status.FINISHED) {
            params.saveProperties();

            //get import parameters
            title = LocaleMgr.misc.getString("ImportingJavaBytecodeFiles");
            boolean useProgressBar = true;

            Controller controller = new DefaultController(title, useProgressBar,
                    "bytecodeImportLogFile.txt");
            Worker worker = new ReverseJavaBytecodeWorker(params);
            controller.start(worker);
        } //end if
    } //end execute()

    private JackMenu getJavaToolMenu(MainFrameMenu menuManager) {
        JMenu toolMenu = menuManager.getMenuForKey(MainFrameMenu.MENU_TOOLS);
        JackMenu javaToolMenu = null;

        int nbItems = toolMenu.getItemCount();
        for (int i = 0; i < nbItems; i++) {
            JMenuItem item = toolMenu.getItem(i);
            if (item instanceof JackMenu) {
                JackMenu menu = (JackMenu) item;
                String text = menu.getText();
                if ("Java".equals(text)) {
                    javaToolMenu = menu;
                    break;
                }
            } //end if
        } //end for

        return javaToolMenu;
    } //end getJavaToolMenu()
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }

} //end ImportPlugin2
