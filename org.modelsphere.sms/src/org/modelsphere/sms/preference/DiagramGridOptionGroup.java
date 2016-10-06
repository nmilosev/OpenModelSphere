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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.graphic.Grid;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.preference.GridOptionPanel;
import org.modelsphere.sms.MainFrame;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class DiagramGridOptionGroup extends OptionGroup implements ActionListener,
        ChangeListener {

    public DiagramGridOptionGroup() {
        super(GridOptionGroup.GRID_OPTIONS);
    }

    protected OptionPanel createOptionPanel() {
        return new DiagramGridOptionPanel();
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
    private static class DiagramGridOptionPanel extends OptionPanel {

        public DiagramGridOptionPanel() {
        }

        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;

        public void init() {

            Boolean b = options.getPropertyBoolean(Grid.class, Grid.PROPERTY_HIDE_GRID,
                    new Boolean(Grid.PROPERTY_HIDE_GRID_DEFAULT));
            Boolean c = options.getPropertyBoolean(Grid.class, Grid.PROPERTY_GRID_ACTIVE,
                    new Boolean(Grid.PROPERTY_GRID_ACTIVE_DEFAULT));
            Integer x = options.getPropertyInteger(Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_X,
                    new Integer(5));
            Integer y = options.getPropertyInteger(Grid.class, Grid.PROPERTY_NB_OF_CELLS_IN_Y,
                    new Integer(5));
            Integer perc = options.getPropertyInteger(Grid.class, Grid.PROPERTY_PERCENT_OF_CELL,
                    new Integer(50));
            Integer ic = options.getPropertyInteger(Grid.class, Grid.PROPERTY_GRID_COLOR,
                    new Integer(0));
            Color color = new Color(ic.intValue());

            GridOptionPanel.GridOptions gridOptions = new GridOptionPanel.GridOptions(!b
                    .booleanValue(), c.booleanValue(), x.intValue(), y.intValue(), perc.intValue(),
                    color);
            JPanel panel = new GridOptionPanel(gridOptions, this);
            this.setLayout(new BorderLayout());
            this.add(panel, BorderLayout.CENTER);
        } // end init()

        public void terminate() {
            DefaultMainFrame mf = MainFrame.getSingleton();
            mf.refreshAllDiagrams();
        } // end terminate()

    } // end DiagramGridOptionPanel

} // end DiagramGridOptionGroup()
