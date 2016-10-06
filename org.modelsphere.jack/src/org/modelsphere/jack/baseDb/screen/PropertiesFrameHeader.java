/*************************************************************************

Copyright (C) 2009 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.baseDb.screen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.SemanticalModel;
import org.modelsphere.jack.srtool.explorer.Explorer;
import org.modelsphere.jack.util.ExceptionHandler;

public class PropertiesFrameHeader extends JPanel implements MouseListener {
    protected DbObject dbo;
    protected DbObject parentDbo;
    protected JLabel parentTitle = new JLabel();
    protected JLabel parentValue = new JLabel();
    protected JLabel childTitle = new JLabel();
    protected JLabel childValue = new JLabel();
    //protected Border border = BorderFactory.createLineBorder(UIManager.getColor("Tree.selectionBackground"), 1);
    protected Border border = BorderFactory.createLineBorder(
            UIManager.getColor("Panel.background"), 4);

    TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();

    protected PropertiesFrameHeader(DbObject dbo, DbObject parentDbo) {
        super(new GridBagLayout());
        this.dbo = dbo;
        this.parentDbo = parentDbo;
        add(parentTitle, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 4, 4, 6), 0, 0));
        add(parentValue, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 0, 4, 4), 0, 0));
        add(childTitle, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 4, 2, 6), 0, 0));
        add(childValue, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(2, 0, 2, 4), 0, 0));

        setBorder(border);
    }

    public void refresh() throws DbException {
        boolean supportedParent = ApplicationContext.getDefaultMainFrame().supportsPropertiesFrame(
                parentDbo);
        supportedParent = supportedParent
                && ApplicationContext.getSemanticalModel().isVisibleOnScreen(
                        parentDbo.getComposite(), parentDbo, PropertiesFrameHeader.class);
        if (supportedParent) {
            parentValue.addMouseListener(this);
        } else {
            parentValue.removeMouseListener(this);
        }

        if (supportedParent) {
            String pTitle = ApplicationContext.getSemanticalModel().getDisplayText(
                    parentDbo.getMetaClass(), null, parentDbo, PropertiesFrameHeader.class);
            String pName = "";
            if (terminologyUtil.isObjectLine(parentDbo))
                pName = ApplicationContext.getSemanticalModel().getDisplayText(parentDbo,
                        Explorer.class);
            else
                pName = ApplicationContext.getSemanticalModel().getDisplayText(parentDbo,
                        PropertiesFrameHeader.class);

            parentTitle.setText("<html><body><b>" + pTitle + ":</b><body><html>"); // NOT LOCALIZABLE
            parentValue.setText("<html><body>" + (supportedParent ? "<u>" : "") + pName
                    + (supportedParent ? "</u>" : "") + "<body><html>"); // NOT LOCALIZABLE
        }
        parentTitle.setVisible(supportedParent);
        parentValue.setVisible(supportedParent);

        String cTitle = ApplicationContext.getSemanticalModel().getDisplayText(dbo.getMetaClass(),
                null, dbo, PropertiesFrameHeader.class);
        String cName = "";
        if (terminologyUtil.isObjectLine(dbo))
            cName = ApplicationContext.getSemanticalModel().getDisplayText(dbo, Explorer.class);
        else
            cName = ApplicationContext.getSemanticalModel().getDisplayText(dbo,
                    SemanticalModel.NAME_SHORT_FORM, null, PropertiesFrameHeader.class);

        childTitle.setText("<html><body><b>" + cTitle + ":</b><body><html>"); // NOT LOCALIZABLE
        childValue.setText("<html><body>" + cName + "<body><html>"); // NOT LOCALIZABLE
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == parentValue) {
            try {
                parentDbo.getDb().beginReadTrans();
                ApplicationContext.getDefaultMainFrame().addPropertyInternalFrame(parentDbo);
                parentDbo.getDb().commitTrans();
            } catch (Exception ex) {
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
            }
        } else { // childValue
            try {
                dbo.getDb().beginReadTrans();
                ApplicationContext.getDefaultMainFrame().addPropertyInternalFrame(dbo);
                dbo.getDb().commitTrans();
            } catch (Exception ex) {
                ExceptionHandler.processUncatchedException(
                        ApplicationContext.getDefaultMainFrame(), ex);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        JInternalFrame parent = (JInternalFrame) SwingUtilities.getAncestorOfClass(
                JInternalFrame.class, this);
        if (parent != null) {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void mouseExited(MouseEvent e) {
        JInternalFrame parent = (JInternalFrame) SwingUtilities.getAncestorOfClass(
                JInternalFrame.class, this);
        if (parent != null) {
            parent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public void updateUI() {
        super.updateUI();
        //Color background = UIManager.getColor("Tree.textBackground");
        //Color foreground = UIManager.getColor("Tree.textForeground");
        //setBackground(background);
        //setForeground(foreground);
        //Color borderColor = UIManager.getColor("Tree.selectionBackground");
        Color borderColor = UIManager.getColor("Panel.background");
        border = BorderFactory.createLineBorder(borderColor, 4);
        setBorder(border);
    }

}
