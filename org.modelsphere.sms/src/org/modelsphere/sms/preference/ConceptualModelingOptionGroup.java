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

/*
 * Created on Apr 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.sms.preference;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.sms.international.LocaleMgr;

import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class ConceptualModelingOptionGroup extends OptionGroup {
    /**
     * @author nicolask
     * 
     *         TODO To change the template for this generated type comment go to Window -
     *         Preferences - Java - Code Style - Code Templates
     * 
     * 
     */

    protected OptionPanel createOptionPanel() {
        return new ConceptualPanel();
    }

    public static class ConceptualPanel extends OptionPanel implements ActionListener,
            ChangeListener {

        private JCheckBox checkbox = new JCheckBox();

        public ConceptualPanel() {
            setLayout(new GridBagLayout());
            add(checkbox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.BOTH, new Insets(18, 3, 11, 5), 0, 0));
            add(Box.createVerticalGlue(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            checkbox.setText(LocaleMgr.screen.getString("DisplayRelationalElements"));
            checkbox.addActionListener(this);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.modelsphere.jack.preference.OptionPanel#init()
         */
        public void init() {
            PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
            boolean displayRelationalAttributes = applOptions.getPropertyBoolean(
                    TerminologyUtil.class,
                    TerminologyUtil.DISPLAY_RELATIONAL_ATTRIBUTES_FOR_ER_MODELS_KEY,
                    TerminologyUtil.DISPLAY_RELATIONAL_ATTRIBUTES_FOR_ER_MODELS_DEFAULT)
                    .booleanValue();
            checkbox.setSelected(displayRelationalAttributes);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent )
         */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == checkbox) {
                boolean displayRelationalAttributes = checkbox.isSelected();
                fireOptionChanged(PropertiesManager.APPLICATION_PROPERTIES_SET,
                        TerminologyUtil.class,
                        TerminologyUtil.DISPLAY_RELATIONAL_ATTRIBUTES_FOR_ER_MODELS_KEY, Boolean
                                .valueOf(displayRelationalAttributes));
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @seejavax.swing.event.ChangeListener#stateChanged(javax.swing.event. ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
        }
    }

    public ConceptualModelingOptionGroup() {
        super(LocaleMgr.screen.getString("ConceptualModeling"));
    }

}
