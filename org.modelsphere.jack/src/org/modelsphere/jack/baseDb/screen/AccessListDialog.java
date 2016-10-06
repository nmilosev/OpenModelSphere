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
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.DbtLoginList;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.ExceptionHandler;

public final class AccessListDialog extends JDialog implements ActionListener,
        ListSelectionListener {

    private Db db;
    private JList list;
    private JCheckBox inheritChk = new JCheckBox(LocaleMgr.screen.getString("inheritAccess"));
    private JButton addBtn = new JButton(LocaleMgr.screen.getString("Add"));
    private JButton removeBtn = new JButton(LocaleMgr.screen.getString("Remove"));
    private JButton okBtn = new JButton(LocaleMgr.screen.getString("OK"));
    private JButton cancelBtn = new JButton(LocaleMgr.screen.getString("Cancel"));
    private boolean cancel = false;

    public static void editAccessList(Frame frame, DbSemanticalObject semObj, MetaField field) {
        try {
            Db db = semObj.getDb();
            db.beginReadTrans();
            String title = MessageFormat.format(LocaleMgr.screen.getString("0-1"), new Object[] {
                    semObj.getSemanticalName(DbObject.LONG_FORM), field.getGUIName() });
            AccessListDialog dialog = new AccessListDialog(frame, title, db, (DbtLoginList) semObj
                    .get(field));
            db.commitTrans();
            dialog.setVisible(true);
            if (!dialog.cancel) {
                db.beginWriteTrans(MessageFormat.format(LocaleMgr.screen.getString("0Update"),
                        new Object[] { field.getGUIName() }));
                semObj.set(field, dialog.getLoginList());
                db.commitTrans();
            }
        } catch (Exception ex) {
            ExceptionHandler.processUncatchedException(frame, ex);
        }
    }

    private AccessListDialog(Frame frame, String title, Db db, DbtLoginList loginList)
            throws DbException {
        super(frame, title, true);
        this.db = db;
        inheritChk.setSelected(loginList == null);
        ArrayList elems = new ArrayList();
        if (loginList != null) {
            DbLogin[] logins = loginList.getLogins(db);
            for (int i = 0; i < logins.length; i++) {
                elems.add(new DefaultComparableElement(logins[i], logins[i].getName()));
            }
            Collections.sort(elems, new CollationComparator());
        }
        list = new JList(new AccessListModel(elems));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        listPanel.add(new JScrollPane(list), BorderLayout.CENTER);
        JPanel btnPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        AwtUtil.normalizeComponentDimension(new JButton[] { addBtn, removeBtn });
        btnPanel1.add(addBtn);
        btnPanel1.add(removeBtn);
        listPanel.add(btnPanel1, BorderLayout.SOUTH);

        JPanel btnPanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel2.setBorder(BorderFactory.createEmptyBorder(12, 7, 7, 7));
        AwtUtil.normalizeComponentDimension(new JButton[] { okBtn, cancelBtn });
        btnPanel2.add(okBtn);
        btnPanel2.add(cancelBtn);

        JPanel chkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chkPanel.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
        chkPanel.add(inheritChk);
        getContentPane().add(chkPanel, BorderLayout.NORTH);
        getContentPane().add(listPanel, BorderLayout.CENTER);
        getContentPane().add(btnPanel2, BorderLayout.SOUTH);

        list.addListSelectionListener(this);
        inheritChk.addActionListener(this);
        addBtn.addActionListener(this);
        removeBtn.addActionListener(this);
        okBtn.addActionListener(this);
        cancelBtn.addActionListener(this);
        manageButtons();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getRootPane().setDefaultButton(okBtn);
        new HotKeysSupport(this, cancelBtn, null);
        setSize(300, 300);
        setLocationRelativeTo(frame);
    }

    public final void valueChanged(ListSelectionEvent e) {
        manageButtons();
    }

    public final void actionPerformed(ActionEvent e) {
        if (e.getSource() == inheritChk) {
            if (inheritChk.isSelected())
                ((AccessListModel) list.getModel()).clear();
            manageButtons();
        } else if (e.getSource() == addBtn) {
            doAdd();
        } else if (e.getSource() == removeBtn) {
            AccessListModel model = (AccessListModel) list.getModel();
            int[] indices = list.getSelectedIndices();
            for (int i = indices.length; --i >= 0;)
                model.remove(indices[i]);
            manageButtons();
        } else if (e.getSource() == okBtn) {
            dispose();
        } else if (e.getSource() == cancelBtn) {
            cancel = true;
            dispose();
        }
    }

    private void doAdd() {
        try {
            db.beginReadTrans();
            AccessListModel model = (AccessListModel) list.getModel();
            ArrayList elems = new ArrayList();
            DbEnumeration dbEnum = db.getLoginNode().getComponents().elements(DbLogin.metaClass);
            nextLogin: while (dbEnum.hasMoreElements()) {
                DbLogin login = (DbLogin) dbEnum.nextElement();
                int nb = model.getSize();
                for (int i = 0; i < nb; i++) {
                    if (login == ((DefaultComparableElement) model.getElementAt(i)).object)
                        continue nextLogin;
                }
                elems.add(new DefaultComparableElement(login, login.getName()));
            }
            dbEnum.close();
            db.commitTrans();

            CollationComparator comparator = new CollationComparator();
            Collections.sort(elems, comparator);
            int[] indices = LookupDialog.selectMany(this, DbLogin.metaClass.getGUIName(true), null,
                    elems.toArray(), -1, comparator);
            for (int i = 0; i < indices.length; i++) {
                model.add(elems.get(indices[i]));
            }
        } catch (Exception ex) {
            ExceptionHandler.processUncatchedException(this, ex);
        }
    }

    private void manageButtons() {
        boolean state = !inheritChk.isSelected();
        addBtn.setEnabled(state);
        removeBtn.setEnabled(state && !list.isSelectionEmpty());
    }

    private DbtLoginList getLoginList() throws DbException {
        if (inheritChk.isSelected())
            return null;
        AccessListModel model = (AccessListModel) list.getModel();
        DbLogin[] logins = new DbLogin[model.getSize()];
        for (int i = 0; i < logins.length; i++) {
            logins[i] = (DbLogin) ((DefaultComparableElement) model.getElementAt(i)).object;
        }
        return new DbtLoginList(logins);
    }

    private static class AccessListModel extends AbstractListModel {
        private ArrayList elems;

        AccessListModel(ArrayList elems) {
            this.elems = elems;
        }

        final void add(Object elem) {
            elems.add(elem);
            int index = elems.size() - 1;
            fireIntervalAdded(this, index, index);
        }

        final void remove(int index) {
            elems.remove(index);
            fireIntervalRemoved(this, index, index);
        }

        final void clear() {
            int nb = elems.size();
            if (nb != 0) {
                elems.clear();
                fireIntervalRemoved(this, 0, nb - 1);
            }
        }

        public final int getSize() {
            return elems.size();
        }

        public final Object getElementAt(int index) {
            return elems.get(index);
        }
    } // End of class AccessListModel

}
