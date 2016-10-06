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

package org.modelsphere.sms.be.notation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.srtypes.BEZoneStereotype;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.notation.OptionComponent;

public final class FlowComponentPanel extends JPanel implements DocumentListener {
    private OptionComponent optionComponent;

    private JLabel formatLabel = new JLabel(LocaleMgr.screen.getString("format"));
    private JTextField format = new JTextField();

    private ZoneEditor zoneEditor = new ZoneEditor();

    private boolean init = false;

    public FlowComponentPanel(OptionComponent optionComponent) {
        this.optionComponent = optionComponent;
        initGUI();
    }

    private void initGUI() {
        setLayout(new GridBagLayout());

        add(formatLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(12, 12, 6, 12), 0, 0));
        add(format, new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(12, 0, 6, 12), 20, 0));
        add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 2, 0.7, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        add(zoneEditor, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(6, 12, 12, 12), 0, 0));

        format.getDocument().addDocumentListener(this);
    }

    void initFields(DbBENotation notation, Object[] values) throws DbException {
        init = true; // avoid setText to trigger an value changed
        format.setText((String) values[0]);
        zoneEditor.initValues(notation, BEZoneStereotype.getInstance(BEZoneStereotype.FLOW));
        format.setEnabled(!notation.isBuiltIn());
        init = false;
    }

    public void insertUpdate(DocumentEvent e) {
        updatePrefix();
    }

    public void removeUpdate(DocumentEvent e) {
        updatePrefix();
    }

    public void changedUpdate(DocumentEvent e) {
    }

    private void updatePrefix() {
        if (init)
            return;
        optionComponent.setValue(format.getText(), 0);
    }

}
