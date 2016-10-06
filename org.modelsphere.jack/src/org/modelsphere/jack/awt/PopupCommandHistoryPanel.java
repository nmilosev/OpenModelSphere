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

package org.modelsphere.jack.awt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.UndoRedoAbstractAction;
import org.modelsphere.jack.international.LocaleMgr;

public class PopupCommandHistoryPanel extends PopupButton {
    private static final String kActionCount = LocaleMgr.misc.getString("Actions0");

    private JLabel actionCountLabel;
    private JList list;

    // This field is used to control events when the action properties are
    // changed.
    protected boolean preventEvent = false;

    public PopupCommandHistoryPanel() {
        this(null);
    }

    public PopupCommandHistoryPanel(UndoRedoAbstractAction action) {
        super(action);
    }

    private PopupPanel popup;

    @Override
    protected PopupPanel createPopupPanel() {
        if (popup == null) {
            // In this case, do not recreate the popup each time values change
            popup = super.createPopupPanel();
            popup.setLayout(new BorderLayout());

            JScrollPane scrollPane = new JScrollPane(getList(),
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            actionCountLabel = new JLabel();

            scrollPane.setBorder(null);
            scrollPane.setAutoscrolls(true);
            popup.add(scrollPane, BorderLayout.CENTER);
            Border actionCountBorder = BorderFactory.createCompoundBorder(BorderFactory
                    .createMatteBorder(1, 0, 0, 0, UIManager.getColor("controlShadow")),
                    BorderFactory.createEmptyBorder(1, 0, 0, 0));
            actionCountLabel.setBorder(actionCountBorder);
            actionCountLabel.setHorizontalAlignment(JLabel.CENTER);
            popup.add(actionCountLabel, BorderLayout.SOUTH);

            popup.addPopupMenuListener(new PopupMenuListener() {

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                }

                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    updateCount(0);
                    list.clearSelection();
                    ((JViewport) list.getParent()).setViewPosition(new Point(0, 0));
                }
            });
        }

        return popup;
    }

    private JList getList() {
        if (list == null) {

            list = new JList() {
                public Dimension getPreferredSize() {
                    Dimension superprefsize = super.getPreferredSize();
                    if (superprefsize == null)
                        return null;
                    return new Dimension(superprefsize.width + 30, superprefsize.height);
                }
            };

            list.setModel(new DefaultListModel());

            list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting() && e.getFirstIndex() != -1) {
                        list.setSelectionInterval(0, e.getLastIndex());
                        updateCount(e.getLastIndex() + 1);
                    }
                }
            });
            list.setVisibleRowCount(10);
            list.addMouseMotionListener(new MouseMotionListener() {
                public void mouseDragged(MouseEvent e) {
                }

                public void mouseMoved(MouseEvent e) {
                    int index = list.locationToIndex(e.getPoint());
                    list.setSelectionInterval(0, index == -1 ? 0 : index);
                    updateCount(index + 1);
                }
            });
            list.addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    int index = list.locationToIndex(e.getPoint());
                    if (index > -1) {
                        hidePopup();
                        fireAction(index);
                        // This line is to avoid double click to activate a
                        // JMenu
                        // after hiding the popup.
                        MenuSelectionManager.defaultManager().clearSelectedPath();
                    }
                }
            });
        }
        return list;
    }

    private void initValues() {
        hidePopup();
        DefaultListModel model = (DefaultListModel) getList().getModel();
        model.removeAllElements();
        Object[] commands = getValues();
        if (commands == null || commands.length == 0)
            return;
        for (int i = 0; i < commands.length; i++) {
            model.addElement(commands[i]);
        }
    }

    private void fireAction(int index) {
        if (!preventEvent) {
            UndoRedoAbstractAction action = (UndoRedoAbstractAction) getAction();
            action.doActionPerformed_Internal(index);
        }
    }

    private void updateCount(int index) {
        actionCountLabel.setText(MessageFormat.format(kActionCount, new Object[] { new Integer(
                index) }));
    }

    protected final void updateItems(String[] values) {
        hidePopup();
        setValues(values);
        initValues();
    }

    protected final PropertyChangeListener createActionPropertyChangeListener(Action a) {
        return new PropertyChangeListener() {
            public final void propertyChange(PropertyChangeEvent e) {
                String propertyName = e.getPropertyName();
                if (propertyName.equals(UndoRedoAbstractAction.COMMANDS)) {
                    preventEvent = true;
                    updateItems((String[]) e.getNewValue());
                    preventEvent = false;
                } else if (propertyName.equals("enabled")) // NOT LOCALIZABLE,
                    // property key
                    setEnabled(((Boolean) e.getNewValue()).booleanValue());
                else if (propertyName.equals(Action.SHORT_DESCRIPTION))
                    setToolTipText(e.getNewValue() == null ? "" : (String) e.getNewValue());
                else if (propertyName.equals(AbstractApplicationAction.VISIBLE)
                        || propertyName.equals(AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION)) {
                    updateVisible();
                }
            }
        };
    }

    private void updateVisible() {
        if (getAction() instanceof AbstractApplicationAction) {
            boolean newValue = true;
            AbstractApplicationAction action = (AbstractApplicationAction) getAction();
            if ((action.getVisibilityMode() & AbstractApplicationAction.VISIBILITY_ALWAYS_VISIBLE_IN_TOOLBAR) == 0)
                newValue = action.isVisible() && newValue;
            else if (!action.getToolBarVisibilityOption())
                newValue = false;
            setVisible(newValue);
            invalidate();
            repaint();
        }
    }

    protected final void configurePropertiesFromAction(Action a) {
        setToolTipText(a != null ? (String) a.getValue(Action.SHORT_DESCRIPTION) : null);
        setIcon(a != null ? (Icon) ((UndoRedoAbstractAction) a).getValue(Action.SMALL_ICON) : null);
        setEnabled(a != null ? a.isEnabled() : false);
        updateItems(a != null ? ((UndoRedoAbstractAction) a).getCommandList() : null);
        ActionHelpPropertySupport.registerHelpSupport(this, (AbstractApplicationAction) a);
        updateVisible();
    }

};
