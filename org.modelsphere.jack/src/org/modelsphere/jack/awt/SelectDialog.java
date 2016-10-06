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

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;

public class SelectDialog extends JDialog implements ActionListener, ListSelectionListener,
        MouseListener {

    public static final int ACTION_CANCEL = 0;
    public static final int ACTION_OK = 1;

    public static final int OPTION_SORT_ITEMS = 0x01;
    public static final int OPTION_SELECT_MIN_1 = 0x02;
    public static final int OPTION_SELECT_MAX_1 = 0x04;

    private JList list = new JList();
    private JButton okButton = new JButton(LocaleMgr.screen.getString("OK"));
    private JButton cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));

    private boolean cancel = true;

    private Rectangle iconRect;

    private int options = 0;

    private static class SelectDialogCellRenderer extends JCheckBox implements ListCellRenderer {
        protected static Border noFocusBorder;

        public SelectDialogCellRenderer() {
            super();
            if (noFocusBorder == null) {
                noFocusBorder = new EmptyBorder(1, 1, 1, 1);
            }
            setOpaque(true);
            setBorder(noFocusBorder);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            setComponentOrientation(list.getComponentOrientation());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            SelectDialogItem item = (SelectDialogItem) value;
            setIcon(item.icon);
            setSelected(item.selected);
            setText((value == null) ? "" : value.toString());

            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder")
                    : noFocusBorder); // NOT
            // LOCALIZABLE
            return this;
        }

        // Overrided for performance reasons
        public void validate() {
        }

        public void revalidate() {
        }

        public void repaint(long tm, int x, int y, int width, int height) {
        }

        public void repaint(Rectangle r) {
        }

        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            // Strings get interned...
            if (propertyName == "text") // NOT LOCALIZABLE
                super.firePropertyChange(propertyName, oldValue, newValue);
        }

        public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
        }

        public void firePropertyChange(String propertyName, char oldValue, char newValue) {
        }

        public void firePropertyChange(String propertyName, short oldValue, short newValue) {
        }

        public void firePropertyChange(String propertyName, int oldValue, int newValue) {
        }

        public void firePropertyChange(String propertyName, long oldValue, long newValue) {
        }

        public void firePropertyChange(String propertyName, float oldValue, float newValue) {
        }

        public void firePropertyChange(String propertyName, double oldValue, double newValue) {
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }

    }

    private SelectDialog(JFrame parent, String title, SelectDialogItem[] items, int options) {
        super(parent, title, true);
        init(items, options);
    }

    private SelectDialog(JDialog parent, String title, SelectDialogItem[] items, int options) {
        super(parent, title, true);
        init(items, options);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            // check option SELECT_MIN_1
            if ((options & SelectDialog.OPTION_SELECT_MIN_1) != 0) {
                int count = list.getModel().getSize();
                boolean oneselected = false;
                for (int i = 0; i < count; i++) {
                    SelectDialogItem item = (SelectDialogItem) list.getModel().getElementAt(i);
                    if (!item.selected)
                        continue;
                    oneselected = true;
                    break;
                }
                if (!oneselected && count > 0) {
                    JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
                            LocaleMgr.message.getString("SelectAtLeastOne"),
                            ApplicationContext.getApplicationName(), JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
            cancel = false;
            dispose();
        }
        dispose();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (iconRect == null) {
            Icon checkIcon = UIManager.getIcon("CheckBox.icon"); // NOT
            // LOCALIZABLE
            if (checkIcon != null) {
                iconRect = new Rectangle(checkIcon.getIconWidth(), checkIcon.getIconHeight());
            } else {
                iconRect = new Rectangle(16, 16);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (iconRect == null) {
            Icon checkIcon = UIManager.getIcon("CheckBox.icon"); // NOT
            // LOCALIZABLE
            if (checkIcon != null) {
                iconRect = new Rectangle(checkIcon.getIconWidth(), checkIcon.getIconHeight());
            } else {
                iconRect = new Rectangle(16, 16);
            }
        }
        int index = list.locationToIndex(e.getPoint());
        if (index == -1)
            return;

        SelectDialogItem item = (SelectDialogItem) list.getModel().getElementAt(index);
        if (iconRect != null) {
            if (iconRect.width + ((JComponent) list.getCellRenderer()).getInsets().left < e
                    .getPoint().x)
                return;
        }

        boolean selected = !item.selected; // toggle value

        // if no more than one selection allowed, unselect the other ones
        if ((options & SelectDialog.OPTION_SELECT_MAX_1) != 0) {
            int count = list.getModel().getSize();
            for (int i = 0; i < count; i++) {
                SelectDialogItem item2 = (SelectDialogItem) list.getModel().getElementAt(i);
                item2.selected = false;
            } // end for
        } // end if
        item.selected = selected;
        list.repaint();
    }

    private int getSelectedIndex() {
        int idx = -1;
        int nb = list.getModel().getSize();
        for (int i = 0; i < nb; i++) {
            SelectDialogItem item = (SelectDialogItem) list.getModel().getElementAt(i);
            if (item.selected) {
                idx = i;
                break;
            }
        } // end for

        return idx;
    } // end getSelectedIndex()

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    private void init(SelectDialogItem[] items, int options) {
        this.options = options;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel contentPane = (JPanel) this.getContentPane();
        List listItems = Arrays.asList(items);
        if ((options & SelectDialog.OPTION_SORT_ITEMS) != 0)
            Collections.sort(listItems);
        list.setListData(listItems.toArray());
        list.setCellRenderer(new SelectDialogCellRenderer());
        list.addListSelectionListener(this);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        AwtUtil.normalizeComponentDimension(new JButton[] { okButton, cancelButton });

        JPanel listPanel = new JPanel(new GridBagLayout());
        listPanel.add(new JScrollPane(list), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 12, 0), 0, 0));
        buttonPanel.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 12, 12), 0, 0));

        contentPane.setLayout(new GridBagLayout());
        contentPane
                .add(listPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.WEST, GridBagConstraints.BOTH,
                        new Insets(12, 12, 12, 12), 0, 0));
        contentPane.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        pack();
        setSize(Math.max(300, getWidth()), Math.max(400, getHeight()));
    }

    JList getList() {
        return list;
    }

    public static final int select(JFrame parent, String title, SelectDialogItem[] items,
            int options) {
        SelectDialog dialog = new SelectDialog(parent, title, items, options);
        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);
        return dialog.cancel ? ACTION_CANCEL : ACTION_OK;
    }

    public static final int select(JDialog parent, String title, SelectDialogItem[] items,
            int options) {
        SelectDialog dialog = new SelectDialog(parent, title, items, options);
        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);
        return dialog.cancel ? ACTION_CANCEL : ACTION_OK;
    }

    public static final int select(JFrame parent, String title, Object[] elems, Icon[] icons,
            int defValue, boolean atLeastOneSelection, boolean atMoreOneSelection) {
        int nb = elems.length;
        SelectDialogItem[] items = new SelectDialogItem[nb];
        for (int i = 0; i < nb; i++) {
            boolean isSelected = (i == defValue);
            Icon icon = (icons == null) ? null : icons[i];
            items[i] = new SelectDialogItem(elems[i].toString(), icon, isSelected, (Object) null);
        } // end for

        int options = 0;
        if (atLeastOneSelection)
            options += SelectDialog.OPTION_SELECT_MIN_1;

        if (atMoreOneSelection)
            options += SelectDialog.OPTION_SELECT_MAX_1;

        SelectDialog dialog = new SelectDialog(parent, title, items, options);
        Dimension minSize = new Dimension();
        dialog.pack();
        Dimension dim = dialog.getSize();
        dialog.setSize(dim.width * 3, dim.height); // was not wide enough to
        // display the whole title
        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);
        JList list = dialog.getList();
        int selected = (dialog.cancel) ? -1 : dialog.getSelectedIndex();
        return selected;
    } // end select()

    //
    // unit test
    //
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test"); // NOT LOCALIZABLE, unit test
        frame.setVisible(true);

        Object[] elems = new Object[] { "Alpha", "Beta", "Gamma" }; // NOT
        // LOCALIZABLE,
        // unit test
        String question = "Use case will be exploded in which kind of diagram ?"; // NOT
        // LOCALIZABLE,
        // unit
        // test
        int idx = SelectDialog.select(frame, question, elems, null, 1, true, true);
    } // end main ()

} // end SelectDialog()
