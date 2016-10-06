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
package org.modelsphere.sms.preference;

import java.awt.BorderLayout; //import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.awt.choosers.PageTitlePanel;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.MainFrame;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class DiagramPageTitleOptionGroup extends OptionGroup implements ActionListener,
        ChangeListener {
    private static final String PAGE_TITLE = LocaleMgr.screen.getString("PageNumberAndTitle");

    public DiagramPageTitleOptionGroup() {
        super(PAGE_TITLE);
    }

    protected OptionPanel createOptionPanel() {
        return new PageTitleOptionPanel();
    }

    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
    }

    //
    // inner class
    //
    private static class PageTitleOptionPanel extends OptionPanel {

        public PageTitleOptionPanel() {
        }

        public void init() {
            PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
            String patn = options.getPropertyString(ApplicationDiagram.class,
                    ApplicationDiagram.PAGE_TITLE, ApplicationDiagram.PAGE_TITLE_DEFAULT);

            PageTitlePanel.TitleOptions titleOptions = new PageTitlePanel.TitleOptions(patn);
            JPanel panel = new PageTitlePanel(titleOptions, this);
            this.setLayout(new BorderLayout());
            this.add(panel, BorderLayout.CENTER);
        } // end init()

        public void terminate() {
            // refresh ApplicationDiagram ?
            DefaultMainFrame mf = MainFrame.getSingleton();
            mf.refreshAllDiagrams();
        } // end terminate()

    } // end DiagramGridOptionPanel

} // end DiagramGridOptionGroup()
