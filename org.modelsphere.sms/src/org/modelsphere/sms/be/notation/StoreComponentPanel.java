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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.modelsphere.jack.awt.NumericTextField;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.srtypes.BEZoneStereotype;
import org.modelsphere.sms.be.international.LocaleMgr;
import org.modelsphere.sms.db.srtypes.SMSNotationShape;
import org.modelsphere.sms.notation.OptionComponent;

public final class StoreComponentPanel extends JPanel implements ActionListener {
    private OptionComponent optionComponent;

    private JLabel formatLabel = new JLabel(LocaleMgr.screen.getString("format"));
    private JTextField format = new JTextField();
    private JLabel widthLabel = new JLabel(LocaleMgr.screen.getString("defaultWidth"));
    private NumericTextField width = new NumericTextField(NumericTextField.INTEGER);
    private JLabel heightLabel = new JLabel(LocaleMgr.screen.getString("defaultHeight"));
    private NumericTextField height = new NumericTextField(NumericTextField.INTEGER);
    private JLabel shapeLabel = new JLabel(LocaleMgr.screen.getString("shape"));
    private JComboBox shape = new JComboBox() {
        // This is a patch for 1.3 (ok for 1.4) Bad evaluation of combo height
        public Dimension getPreferredSize() {
            Dimension dim = super.getPreferredSize();
            if (dim == null)
                return null;
            int height = SMSNotationShape.imagePossibleValues[0].getHeight(null);
            return new Dimension(dim.width + 16, Math.max(dim.height, height + 2));
        }

        public Dimension getMinimumSize() {
            Dimension dim = super.getPreferredSize();
            if (dim == null)
                return null;
            int height = SMSNotationShape.imagePossibleValues[0].getHeight(null);
            return new Dimension(dim.width + 16, Math.max(dim.height, height + 2));
        }
    };

    private ZoneEditor zoneEditor = new ZoneEditor();

    private boolean init = false;

    public StoreComponentPanel(OptionComponent optionComponent) {
        this.optionComponent = optionComponent;
        initGUI();
    }

    private void initGUI() {
        setLayout(new GridBagLayout());

        add(formatLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, new Insets(12, 12, 6, 6), 0, 0));
        add(format, new GridBagConstraints(1, 0, 1, 1, 0.4, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(12, 0, 6, 6), 0, 0));
        add(shapeLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, new Insets(0, 12, 6, 6), 0, 0));
        add(shape, new GridBagConstraints(1, 1, 1, 1, 0.4, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 2, 0.2, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(widthLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, new Insets(12, 6, 6, 6), 0, 0));
        add(width, new GridBagConstraints(4, 0, 1, 1, 0.4, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(12, 0, 6, 12), 0, 0));
        add(heightLabel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, new Insets(0, 6, 6, 6), 0, 0));
        add(height, new GridBagConstraints(4, 1, 1, 1, 0.4, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 12), 0, 0));

        add(zoneEditor, new GridBagConstraints(0, 2, 5, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(6, 12, 12, 12), 0, 0));

        format.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updatePrefix();
            }

            public void removeUpdate(DocumentEvent e) {
                updatePrefix();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        width.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateWidth();
            }

            public void removeUpdate(DocumentEvent e) {
                updateWidth();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });
        height.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateHeight();
            }

            public void removeUpdate(DocumentEvent e) {
                updateHeight();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });

        ImageIcon values[] = new ImageIcon[SMSNotationShape.imagePossibleValues.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = SMSNotationShape.imagePossibleValues[i] == null ? null : new ImageIcon(
                    SMSNotationShape.imagePossibleValues[i]);
        }
        shape.setEditable(false);
        shape.setModel(new DefaultComboBoxModel(values));
        shape.addActionListener(this);
    }

    void initFields(DbBENotation notation, Object[] values) throws DbException {
        init = true; // avoid setText to trigger an value changed
        format.setText((String) values[2]);
        width.setText(values[0] == null ? "" : values[0].toString()); // NOT
        // LOCALIZABLE
        height.setText(values[1] == null ? "" : values[1].toString()); // NOT
        // LOCALIZABLE
        zoneEditor.initValues(notation, BEZoneStereotype.getInstance(BEZoneStereotype.STORE));
        if (values[3] != null) {
            Object[] shapes = SMSNotationShape.objectPossibleValues;
            for (int i = 0; i < shapes.length; i++) {
                if (shapes[i].equals(values[3])) {
                    shape.setSelectedIndex(i);
                }
            }
        }
        boolean builtin = notation.isBuiltIn();
        shape.setEnabled(!builtin);
        format.setEnabled(!builtin);
        init = false;
    }

    private void updatePrefix() {
        if (init)
            return;
        optionComponent.setValue(format.getText(), 2);
    }

    private void updateWidth() {
        if (init)
            return;
        try {
            if (width.getText() == null || width.getText().length() == 0)
                return;
            optionComponent.setValue(new Integer(width.getText()), 0);
        } catch (NumberFormatException e) {
        }
    }

    private void updateHeight() {
        if (init)
            return;
        try {
            if (height.getText() == null || height.getText().length() == 0)
                return;
            optionComponent.setValue(new Integer(height.getText()), 1);
        } catch (NumberFormatException e) {
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (init)
            return;
        if (e.getSource() == shape)
            optionComponent.setValue(
                    SMSNotationShape.objectPossibleValues[shape.getSelectedIndex()], 3);
    }

}
