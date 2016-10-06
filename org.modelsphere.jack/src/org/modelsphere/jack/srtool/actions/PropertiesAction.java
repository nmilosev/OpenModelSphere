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

package org.modelsphere.jack.srtool.actions;

import java.awt.event.*;
import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public final class PropertiesAction extends AbstractApplicationAction implements
        SelectionActionListener {
    private static final int MAX_PROPERTIES_WINDOW = 5;

    public PropertiesAction() {
        super(LocaleMgr.action.getString("properties"), LocaleMgr.action.getImageIcon("properties"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("properties"));
        this.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.ALT_MASK));
        setEnabled(false);
    }

    protected final void doActionPerformed() {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        try {
            TerminologyUtil tm = TerminologyUtil.getInstance();
            if (tm.validateSelectionModel() == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                if (!TerminologyUtil.getShowPhysicalConcepts(objects))
                    if (!tm.isPureERSet(objects))
                        return;
            DbMultiTrans.beginTrans(Db.READ_TRANS, objects, null);
            for (int i = 0; i < objects.length; i++) {
                mainFrame.addPropertyInternalFrame(objects[i]);
            }
            DbMultiTrans.commitTrans(objects);
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mainFrame, e);
        }
    }

    public final void updateSelectionAction() throws DbException {
        DbObject[] objects = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        this.setEnabled(isSelectionValid(objects));

        if (objects.length > MAX_PROPERTIES_WINDOW) {
            this.putValue(Action.NAME, MessageFormat.format(LocaleMgr.action
                    .getString("maximumSelectionIs"),
                    new Object[] { LocaleMgr.action.getString("properties"),
                            String.valueOf(MAX_PROPERTIES_WINDOW) }));
        } else {
            this.putValue(Action.NAME, LocaleMgr.action.getString("properties"));
        }
    }

    private boolean isSelectionValid(DbObject[] objects) throws DbException {
        if (objects.length == 0 || objects.length > MAX_PROPERTIES_WINDOW)
            return false;
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        for (int i = 0; i < objects.length; i++) {
            if (!mainFrame.supportsPropertiesFrame(objects[i]))
                return false;
        }
        return true;
    }
}
