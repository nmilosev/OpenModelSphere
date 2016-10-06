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

package org.modelsphere.sms.style;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.srtypes.DbtPrefix;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.sms.db.DbSMSStyle;
import org.modelsphere.sms.db.srtypes.SMSDisplayDescriptor;
import org.modelsphere.sms.or.db.srtypes.ORDescriptorFormat;

public class StyleComponent extends JPanel {

    private StyleFrame styleFrame;
    private DbObject style;
    public JLabel nameTextField;
    private JPanel borderPanel;
    private OptionComponent[] optionComps;
    private String[] optionGroupHeaders;

    public StyleComponent(StyleFrame styleFrame, MetaClass styleClass, String[] optionsName) {
        this.styleFrame = styleFrame;
        jbInit(styleClass, optionsName);
    }

    private void jbInit(MetaClass styleClass, String[] optionsName) {
        setLayout(new BorderLayout());
        MetaField[][] optionGroups;
        Object[][] optionValueGroups;

        optionGroups = StyleFrame.getOptionGroups(styleClass.getJClass(), optionsName[0]);
        optionValueGroups = StyleFrame.getOptionValueGroups(styleClass.getJClass(), optionsName[1]);
        optionGroupHeaders = StyleFrame.getOptionGroupHeaders(styleClass.getJClass(),
                optionsName[2]);
        optionComps = new OptionComponent[optionGroups.length];
        borderPanel = new JPanel(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        for (int i = 0; i < optionComps.length; i++) {
            optionComps[i] = createOptionPanel(optionGroups[i], optionValueGroups[i]);
        }

        nameTextField = new JLabel();
        JPanel namePanel = new JPanel();
        namePanel.add(nameTextField);
        add(namePanel, BorderLayout.NORTH);
        add(borderPanel, BorderLayout.CENTER);
    }

    private final OptionComponent createOptionPanel(MetaField[] optionGroup,
            Object[] optionValueGroup) {
        if (optionValueGroup instanceof Boolean[])
            return new BooleanOptionComponent(this, optionGroup);
        else if (optionValueGroup instanceof Font[])
            return new FontOptionComponent(this, optionGroup);
        else if (optionValueGroup instanceof DbtPrefix[])
            return new PrefixOptionComponent(this, optionGroup);
        else if (optionValueGroup instanceof Color[])
            return new ColorOptionComponent(this, optionGroup);
        else if (optionValueGroup instanceof SMSDisplayDescriptor[])
            return new DisplayOptionComponent(this, optionGroup);
        else if (optionValueGroup instanceof ORDescriptorFormat[])
            return new DescriptorFormatOptionComponent(this, optionGroup);
        else
            throw new RuntimeException("Unimplemented panel type for DbSMSStyle");
    }

    public final void setStyle(DbObject newStyle) throws DbException {
        style = newStyle;

        borderPanel.removeAll();
        borderPanel.validate();
        borderPanel.repaint();
        refreshName();
    }

    public final void setStyle(DbObject newStyle, StyleNode selectedNode, boolean refresh)
            throws DbException {
        style = newStyle;

        // show the selected panel
        for (int i = 0; i < optionComps.length; i++) {
            if (selectedNode.toString().equals(optionGroupHeaders[i])) {
                optionComps[i].setStyle(style, refresh);
                borderPanel.removeAll();
                borderPanel.add(optionComps[i].getOptionPanel(), BorderLayout.CENTER);
                borderPanel.validate();
                borderPanel.repaint();
                break;
            }
        }
        refreshName();
    }

    private final void refreshName() throws DbException {
        nameTextField.setText((String) style.get(DbSMSStyle.fName));
        nameTextField.getParent().validate();
    }

    public final void setApply(boolean state) {
        styleFrame.setApply(state);
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
