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
import java.awt.event.*;
import java.util.Comparator;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.international.LocaleMgr;

public class LookupDialog extends JDialog {

    public static final int SELECT_NONE = 0;
    public static final int SELECT_ONE = 1;
    public static final int SELECT_MANY = 2;

    private int initSelIndex;
    private int[] selIndices = new int[0];
    private JList list;
    private JButton selectBtn = new JButton(LocaleMgr.screen.getString("Select"));
    private Comparator comparator;
    private String matchString;
    private Timer matchTimer;

    private static final int DIALOG_MARGE = 90;

    /*
     * Ask the user to select a single object in an array of objects. Return the index of the object
     * selected, or -1 if the user hits Cancel.
     * 
     * If you have a Collection instead of an array, just pass collection.toArray().
     * 
     * The text displayed in the listbox is the toString() of the object; if the process of getting
     * this text is time-consuming, it is better to pass an array of DefaultComparableElements,
     * where the text is pre-calculated. For example, for DbObjects, we have to use
     * DefaultComparableElements, because we don't want to start a transaction for each line
     * displayed in the list.
     * 
     * If the list is sorted, the user may scroll to an element by typing the first characters of
     * the text displayed.
     */
    public static int selectOne(Component comp, String title, String message, Object[] objects,
            int selIndex, Comparator comparator) {
        LookupDialog lookup = new LookupDialog(comp, title, message, objects, selIndex, SELECT_ONE,
                comparator);
        lookup.setVisible(true);
        return lookup.getSelIndex();
    }

    /*
     * Ask the user to select any number of objects in an array of objects. Return an array of
     * selected indices; the array is empty if the user hits Cancel.
     */
    public static int[] selectMany(Component comp, String title, String message, Object[] objects,
            int selIndex, Comparator comparator) {
        LookupDialog lookup = new LookupDialog(comp, title, message, objects, selIndex,
                SELECT_MANY, comparator);
        lookup.setVisible(true);
        return lookup.getSelIndices();
    }

    public LookupDialog(Component comp, String title, String message, Object[] objects,
            int selIndex, int selMode, Comparator comparator) {
        super((comp instanceof Frame ? (Frame) comp : (Frame) SwingUtilities.getAncestorOfClass(
                Frame.class, comp)), title, true);
        initSelIndex = selIndex;
        list = new JList(objects);
        list.setSelectionMode(selMode == SELECT_ONE ? ListSelectionModel.SINGLE_SELECTION
                : ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        if (selMode != SELECT_NONE) {
            list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    manageSelectButton();
                }
            });
            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        selectBtn.doClick();
                    }
                }
            });
        }

        this.comparator = comparator;
        if (comparator != null) {
            list.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    processMatchChar(e.getKeyChar());
                }
            });
            matchString = "";
            matchTimer = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    matchString = "";
                }
            });
            matchTimer.setRepeats(false);
        }

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 11));
        listPanel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(12, 7, 6, 6));
        JButton cancelBtn = new JButton(selMode != SELECT_NONE ? LocaleMgr.screen
                .getString("Cancel") : LocaleMgr.screen.getString("OK"));
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        if (selMode != SELECT_NONE) {
            selectBtn.setEnabled(false);
            selectBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selIndices = list.getSelectedIndices();
                    dispose();
                }
            });
            AwtUtil.normalizeComponentDimension(new JButton[] { selectBtn, cancelBtn });
            btnPanel.add(selectBtn);
            btnPanel.add(cancelBtn);
        } else {
            btnPanel.add(cancelBtn);
        }

        getContentPane().add(listPanel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);

        if (message != null) {
            JLabel messLabel = new JLabel(message);
            messLabel.setBorder(BorderFactory.createEmptyBorder(7, 12, 0, 11));
            getContentPane().add(messLabel, BorderLayout.NORTH);
        }

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                list.requestFocus();
                if (initSelIndex != -1) {
                    list.setSelectedIndex(initSelIndex);
                    list.ensureIndexIsVisible(initSelIndex);
                }
            }

            public void windowClosed(WindowEvent e) {
                if (matchTimer != null)
                    matchTimer.stop();
            }
        });
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(selMode != SELECT_NONE ? selectBtn : cancelBtn);
        new HotKeysSupport(this, cancelBtn, null);

        FontMetrics fm = getFontMetrics(getFont());
        int titleWidth = fm.stringWidth(title);
        int maxWidthSize = Math.max(titleWidth + DIALOG_MARGE, Math.min(600,
                getPreferredSize().width + 20));
        setSize(maxWidthSize, 400);
        setLocationRelativeTo(comp);
    }

    private void processMatchChar(char nextChar) {
        if (Character.isISOControl(nextChar))
            return;
        matchTimer.restart();
        matchString = matchString + nextChar;
        ListModel model = list.getModel();
        int lo = -1;
        int hi = model.getSize();
        if (hi == 0)
            return;
        while (lo + 1 != hi) {
            int mid = (lo + hi) / 2;
            if (comparator.compare(matchString, model.getElementAt(mid)) > 0)
                lo = mid;
            else
                hi = mid;
        }
        if (hi == model.getSize())
            hi--;
        if (list.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION)
            list.setSelectedIndex(hi);
        list.ensureIndexIsVisible(hi);
    }

    private void manageSelectButton() {
        selectBtn.setEnabled(!list.isSelectionEmpty());
    }

    public final int getSelIndex() {
        return (selIndices.length == 0 ? -1 : selIndices[0]);
    }

    public final int[] getSelIndices() {
        return selIndices;
    }
}
