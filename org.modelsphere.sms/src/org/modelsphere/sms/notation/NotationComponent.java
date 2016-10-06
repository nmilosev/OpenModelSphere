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

package org.modelsphere.sms.notation;

import java.awt.BorderLayout;
import java.lang.reflect.Constructor;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.sms.db.DbSMSNotation;

public class NotationComponent extends JPanel {

    private NotationFrame notationFrame;
    private DbObject notation;
    public JLabel nameTextField;
    private JPanel borderPanel;
    private OptionComponent[] optionComps;
    private String[] optionGroupHeaders;
    private MetaField builtInMetaField;

    public NotationComponent(NotationFrame notationFrame, MetaClass notationClass,
            MetaField builtInMetaField, String[] optionsName) {
        this.notationFrame = notationFrame;
        this.builtInMetaField = builtInMetaField;
        jbInit(notationClass, optionsName);
    }

    private void jbInit(MetaClass notationClass, String[] optionsName) {
        setLayout(new BorderLayout());
        MetaField[][] optionGroups;
        Object[][] optionValueGroups;
        String[] optionGroupComponents;

        optionGroups = NotationFrame.getOptionGroups(notationClass.getJClass(), optionsName[0]);
        optionValueGroups = NotationFrame.getOptionValueGroups(notationClass.getJClass(),
                optionsName[1]);
        optionGroupHeaders = NotationFrame.getOptionGroupHeaders(notationClass.getJClass(),
                optionsName[2]);
        optionGroupComponents = NotationFrame.getOptionGroupComponents(notationClass.getJClass(),
                optionsName[3]);
        optionComps = new OptionComponent[optionGroups.length];
        borderPanel = new JPanel(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        for (int i = 0; i < optionComps.length; i++) {
            optionComps[i] = createOptionPanel(optionGroups[i], optionGroupComponents[i]);
        }

        nameTextField = new JLabel();
        JPanel namePanel = new JPanel();
        namePanel.add(nameTextField);
        add(namePanel, BorderLayout.NORTH);
        add(borderPanel, BorderLayout.CENTER);
    }

    private final OptionComponent createOptionPanel(MetaField[] optionGroup,
            String optionComponentClassName) {
        try {
            Class componentClass = Class.forName(optionComponentClassName);
            Constructor componentConstructor = componentClass.getConstructor(new Class[] {
                    NotationComponent.class, MetaField[].class });
            OptionComponent component = (OptionComponent) componentConstructor
                    .newInstance(new Object[] { this, optionGroup });
            return component;
        } catch (Exception e) {
            if (Debug.isDebug())
                e.printStackTrace();
            throw new RuntimeException("Notation options panel not implemented.");
        }
    }

    public final void setNotation(DbObject newNotation) throws DbException {
        notation = newNotation;

        borderPanel.removeAll();
        borderPanel.validate();
        borderPanel.repaint();
        refreshName();
    }

    public final void setNotation(DbObject newNotation, NotationNode selectedNode, boolean refresh)
            throws DbException {
        notation = newNotation;

        int idx = selectedNode.getParent().getIndex(selectedNode);

        // show the selected panel
        borderPanel.removeAll();
        JComponent optionComponent = optionComps[idx].getOptionPanel();
        borderPanel.add(optionComponent, BorderLayout.CENTER);
        if (builtInMetaField != null && notation.get(builtInMetaField) != null
                && ((Boolean) notation.get(builtInMetaField)).booleanValue())
            optionComponent.setEnabled(false); // Prevents update of the built
        // in notations
        else
            optionComponent.setEnabled(true);
        optionComps[idx].setNotation(notation, refresh);
        borderPanel.validate();
        borderPanel.repaint();
        refreshName();
    }

    private final void refreshName() throws DbException {
        nameTextField.setText((String) notation.get(DbSMSNotation.fName));
        nameTextField.getParent().validate();
    }

    public final void setApply(boolean state) {
        notationFrame.setApply(state);
    }

    public final void applyChanges() throws DbException {
        for (int i = 0; i < optionComps.length; i++)
            optionComps[i].applyChanges();
    }

    public final void resetChanges() {
        for (int i = 0; i < optionComps.length; i++)
            optionComps[i].resetChanges();
    }
}
