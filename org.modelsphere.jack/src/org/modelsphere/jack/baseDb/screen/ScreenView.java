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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.international.LocaleChangeEvent;
import org.modelsphere.jack.international.LocaleChangeListener;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;

public abstract class ScreenView extends JPanel implements LocaleChangeListener, ScreenTabPanel {
    public static final int ADD_BTN = 0x0001;
    public static final int DELETE_BTN = 0x0002;
    public static final int LINK_BTN = 0x0004;
    public static final int UNLINK_BTN = 0x0008;
    public static final int PROPERTIES_BTN = 0x0010;
    public static final int EXTRA_BTN = 0x40000000;
    public static final int ALL_BUTTONS = ADD_BTN + DELETE_BTN + LINK_BTN + UNLINK_BTN
            + PROPERTIES_BTN + EXTRA_BTN;

    public static final int APPLY_ACTION = 0x0020;
    public static final int REINSERT_ACTION = 0x0040;
    public static final int SORTED_LIST = 0x0080;
    public static final int DISPLAY_CLASS = 0x0100;
    public static final int NAME_ONLY = 0x0200;
    public static final int FULL_NAME = 0x0400;
    public static final int DISPLAY_PARENT_NAME = 0x0800;

    public static final int ADD_DEL_ACTIONS = ADD_BTN + DELETE_BTN + PROPERTIES_BTN + APPLY_ACTION;
    public static final int LINK_UNL_ACTIONS = LINK_BTN + UNLINK_BTN + PROPERTIES_BTN;

    protected ScreenContext screenContext;
    protected int actionMode;
    private String tabName = "";
    private PropertiesFrame containerFrame = null;

    private JPanel contentPanel = null;
    private JPanel controlPanel = null;
    private JButton addBtn = null;
    protected JButton deleteBtn = null;
    private JButton linkBtn = null;
    protected JButton unlinkBtn = null;
    protected JButton propertiesBtn = null;
    protected JButton moveUpBtn = null, moveDownBtn = null;
    private JButton extraBtn = null;

    public ScreenView(ScreenContext screenContext) {
        this.screenContext = screenContext;
    }

    public ScreenView(ScreenContext screenContext, int actionMode) {
        this.screenContext = screenContext;
        this.actionMode = actionMode;
    }

    protected final void initPanel() {
        if (contentPanel != null)
            contentPanel.removeAll();
        else {
            setLayout(new BorderLayout());
            contentPanel = new JPanel();
            contentPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 5, 5));
            add(contentPanel, BorderLayout.CENTER);
            controlPanel = createControlPanel();
            if (controlPanel != null) {
                add(controlPanel, BorderLayout.SOUTH);
                controlPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 6, 1));
            }
        }
    }

    public final int getActionMode() {
        return actionMode;
    }

    public final boolean isApplyPanel() {
        return (actionMode & APPLY_ACTION) == APPLY_ACTION;
    }

    public final void setPropertiesFrame(PropertiesFrame frame) {
        containerFrame = frame;
    }

    public final PropertiesFrame getPropertiesFrame() {
        return containerFrame;
    }

    public final ScreenContext getScreenContext() {
        return screenContext;
    }

    protected final JPanel getContentPanel() {
        return contentPanel;
    }

    public final String getTabName() {
        return tabName;
    }

    public final void setTabName(String newTabName) {
        tabName = newTabName;
    }

    /**
     * you can tell the screenview that the data display in the screenview has changed so the
     * screenview will adapt the standard buttons and will managed the windowColosing accordingly
     */
    public final void setHasChanged() {
        if (isApplyPanel())
            containerFrame.setHasChanged();
    }

    public abstract ScreenModel getModel();

    public DbObject[] getSelection() {
        return new DbObject[0];
    }

    // called by PropertiesFrame.commit
    public void commit() throws DbException {
    }

    public void resetHasChanged() {
    }

    public void addAction() {
    }

    public void deleteAction() {
    }

    public void linkAction() {
    }

    public void unlinkAction() {
    }

    public void propertiesAction() {
    }

    public void moveUpAction() {
    }

    public void moveDownAction() {
    }

    public void extraAction() {
    }

    public void activateTab() {
    }

    public void refresh() throws DbException {
    }

    public void stopEditing() {
    }

    // Called when the model is changed or when the screen is closed
    // overriding methods must call super.deinstallPanel
    public void deinstallPanel() {
        ScreenModel model = getModel();
        if (model != null)
            model.dispose();
    }

    /*
     * private void adjustControlPanel(){ DbObject[] objects =
     * ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
     * propertiesBtn.setEnabled(isSelectionValid(objects)); }
     */

    public abstract Color getSelectionBackground();

    public abstract Color getSelectionForeground();

    public abstract boolean isCellEditable(int row, int column);

    private JPanel createControlPanel() {
        if ((actionMode & ALL_BUTTONS) == 0)
            return null;
        Vector vecButton = new Vector();
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ActionListener buttonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == addBtn)
                    addAction();
                else if (e.getSource() == deleteBtn)
                    deleteAction();
                else if (e.getSource() == linkBtn)
                    linkAction();
                else if (e.getSource() == unlinkBtn)
                    unlinkAction();
                else if (e.getSource() == propertiesBtn)
                    propertiesAction();
                else if (e.getSource() == moveUpBtn)
                    moveUpAction();
                else if (e.getSource() == moveDownBtn)
                    moveDownAction();
                else if (e.getSource() == extraBtn)
                    extraAction();
            }
        };

        if ((actionMode & ADD_BTN) == ADD_BTN) {
            addBtn = new JButton();
            initAddButton(addBtn);
            vecButton.add(addBtn);
        }
        if ((actionMode & DELETE_BTN) == DELETE_BTN) {
            deleteBtn = new JButton();
            initDeleteButton(deleteBtn);
            vecButton.add(deleteBtn);
        }
        if ((actionMode & LINK_BTN) == LINK_BTN) {
            linkBtn = new JButton();
            initLinkButton(linkBtn);
            vecButton.add(linkBtn);
        }
        if ((actionMode & UNLINK_BTN) == UNLINK_BTN) {
            unlinkBtn = new JButton();
            initUnlinkButton(unlinkBtn);
            vecButton.add(unlinkBtn);
        }
        if ((actionMode & PROPERTIES_BTN) == PROPERTIES_BTN) {
            propertiesBtn = new JButton();
            initPropertiesButton(propertiesBtn);
            vecButton.add(propertiesBtn);
        }
        if ((actionMode & REINSERT_ACTION) == REINSERT_ACTION) {
            moveUpBtn = new JButton();
            initMoveUpButton(moveUpBtn);
            vecButton.add(moveUpBtn);

            moveDownBtn = new JButton();
            initMoveDownButton(moveDownBtn);
            vecButton.add(moveDownBtn);
        }

        if ((actionMode & EXTRA_BTN) == EXTRA_BTN) {
            extraBtn = new JButton();
            initExtraButton(extraBtn);
            vecButton.add(extraBtn);
        }

        JButton[] buttons = new JButton[vecButton.size()];
        vecButton.copyInto(buttons);
        AwtUtil.normalizeComponentDimension(buttons);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(buttonListener);
            panel.add(buttons[i]);
        }
        return panel;
    }

    protected void initAddButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "Add"); // NOT LOCALIZABLE
        button.setVisible(ScreenPerspective.isFullVersion()); 
    }

    protected void initDeleteButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "Delete"); // NOT LOCALIZABLE
        button.setVisible(ScreenPerspective.isFullVersion()); 
    }

    protected void initLinkButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "Link"); // NOT LOCALIZABLE
        button.setVisible(ScreenPerspective.isFullVersion()); 
    }

    protected void initUnlinkButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "Unlink"); // NOT LOCALIZABLE
        button.setVisible(ScreenPerspective.isFullVersion()); 
    }

    protected void initPropertiesButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "Properties"); // NOT
        // LOCALIZABLE
    }

    protected void initMoveUpButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "MoveUp"); // NOT LOCALIZABLE
        button.setVisible(ScreenPerspective.isFullVersion()); 
    }

    protected void initMoveDownButton(JButton button) {
        LocaleMgr.screen.initAbstractButton(button, "MoveDown"); // NOT LOCALIZABLE
        button.setVisible(ScreenPerspective.isFullVersion()); 
    }

    protected void initExtraButton(JButton button) {
    }

    // //////////////////////////////
    // LocaleChangeListener SUPPORT
    //
    public void localeChanged(LocaleChangeEvent e) {
        if (addBtn != null)
            initAddButton(addBtn);
        if (deleteBtn != null)
            initDeleteButton(deleteBtn);
        if (linkBtn != null)
            initLinkButton(linkBtn);
        if (unlinkBtn != null)
            initUnlinkButton(unlinkBtn);
        if (propertiesBtn != null)
            initPropertiesButton(propertiesBtn);
        if (extraBtn != null)
            initExtraButton(extraBtn);
    }

    //
    // End of LocaleChangeListener SUPPORT
    // ///////////////////////////////

    public void updateUI() {
        super.updateUI();
        setBackground(UIManager.getColor("Table.background")); // NOT
        // LOCALIZABLE
        setForeground(UIManager.getColor("Table.foreground")); // NOT
        // LOCALIZABLE
    }

}
