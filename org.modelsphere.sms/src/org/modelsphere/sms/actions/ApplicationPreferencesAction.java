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

package org.modelsphere.sms.actions;

import javax.swing.JToolBar;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.ActionsOptionGroup;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ToolBarManager;
import org.modelsphere.jack.preference.OptionDialog;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.context.ContextOptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.preference.DisplayToolTipsOptionGroup;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.preference.AdvancedOptionGroup;
import org.modelsphere.sms.preference.ConceptualModelingOptionGroup;
import org.modelsphere.sms.preference.DiagramAutomaticCreationOptionGroup;
import org.modelsphere.sms.preference.DiagramGridOptionGroup;
import org.modelsphere.sms.preference.DiagramMagnifierOptionGroup;
import org.modelsphere.sms.preference.DiagramPageTitleOptionGroup;
import org.modelsphere.sms.preference.DiagramStampOptionGroup;
import org.modelsphere.sms.preference.DirectoryOptionGroup;
import org.modelsphere.sms.preference.DisplayGUIOptionGroup;
import org.modelsphere.sms.preference.DisplayLFOptionGroup;
import org.modelsphere.sms.preference.DisplayLanguageOptionGroup;
import org.modelsphere.sms.preference.DisplayRecentModifsOptionGroup;
import org.modelsphere.sms.preference.DisplayWindowsOptionGroup;
import org.modelsphere.sms.preference.GeneralOptionGroup;

final class ApplicationPreferencesAction extends AbstractApplicationAction {
    private static final String kPrefDialogTitle = LocaleMgr.action.getString("applPrefTitle");

    public ApplicationPreferencesAction() {
        super(LocaleMgr.action.getString("applicationPreferences"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("applicationPreferences"));
    }

    public void performAction(int rowSelection) {
        OptionDialog prefDialog = initiateAction();

        // TODO: preselect the right page in the dialog

        prefDialog.showOptions(rowSelection);
    }

    protected final void doActionPerformed() {
        OptionDialog prefDialog = initiateAction();
        prefDialog.showOptions();
    }

    private OptionDialog initiateAction() {
        OptionDialog prefDialog = new OptionDialog(ApplicationContext.getDefaultMainFrame(),
                kPrefDialogTitle);
        OptionGroup display = new OptionGroup(LocaleMgr.screen.getString("Display"));
        display.addOptionGroup(new DisplayGUIOptionGroup());
        // display.addOptionGroup(new DisplayLFOptionGroup());
        display.addOptionGroup(new DisplayWindowsOptionGroup());
        display.addOptionGroup(new DisplayToolTipsOptionGroup());
        display.addOptionGroup(new DisplayLanguageOptionGroup());
        
        //Do not show for Model Viewer
        if (ScreenPerspective.isFullVersion()) {
        	display.addOptionGroup(new ConceptualModelingOptionGroup());
        	display.addOptionGroup(new DisplayRecentModifsOptionGroup());
        }

        OptionGroup diagram = new OptionGroup(LocaleMgr.screen.getString("DiagPanel"));
        diagram.addOptionGroup(new DiagramAutomaticCreationOptionGroup());
        diagram.addOptionGroup(new DiagramStampOptionGroup());
        diagram.addOptionGroup(new DiagramMagnifierOptionGroup());
        diagram.addOptionGroup(new DiagramGridOptionGroup());
        diagram.addOptionGroup(new DiagramPageTitleOptionGroup());

        OptionGroup toobars = new OptionGroup(LocaleMgr.action.getString("ToolBars"));
        addToolBarGroup(toobars, DefaultMainFrame.FILE_TOOLBAR);
        addToolBarGroup(toobars, DefaultMainFrame.EDIT_TOOLBAR);
        addToolBarGroup(toobars, DefaultMainFrame.DISPLAY_TOOLBAR);
        addToolBarGroup(toobars, DefaultMainFrame.FORMAT_TOOLBAR);
        addToolBarGroup(toobars, DefaultMainFrame.NAVIGATION_TOOLBAR);
        
        //Do not show for Model Viewer
        if (ScreenPerspective.isFullVersion()) {
        	addToolBarGroup(toobars, DefaultMainFrame.TOOLS_TOOLBAR);
        	addToolBarGroup(toobars, DefaultMainFrame.MODIFIER_TOOLBAR);
        }

        prefDialog.addOptionGroup(new GeneralOptionGroup());
        prefDialog.addOptionGroup(new DirectoryOptionGroup());
        prefDialog.addOptionGroup(display);
        prefDialog.addOptionGroup(diagram);
        prefDialog.addOptionGroup(new ContextOptionGroup());
        prefDialog.addOptionGroup(toobars);
        prefDialog.addOptionGroup(new AdvancedOptionGroup());

        prefDialog.setLocationRelativeTo(ApplicationContext.getDefaultMainFrame());

        return prefDialog;
    }

    private void addToolBarGroup(OptionGroup parent, String key) {
        JToolBar tb = ToolBarManager.getInstallToolBar(key);
        if (tb != null)
            parent.addOptionGroup(new ActionsOptionGroup(tb));
    }

}
