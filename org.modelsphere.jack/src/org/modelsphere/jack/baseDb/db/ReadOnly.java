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
package org.modelsphere.jack.baseDb.db;

import java.awt.Container;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;

/**
 * 
 * Prevents to modify if an object is read-only. Called by DbObject.basicSet().
 * 
 */
public class ReadOnly {
    private static final String READ_ONLY_MF = "m_isReadOnly"; // NOT LOCALIZABLE
    private static final String LOCKED_MF = "m_isLocked"; // NOT LOCALIZABLE

    // Implements Singleton Pattern
    private ReadOnly() {
    }

    private static ReadOnly g_singleInstance = null;

    public static ReadOnly getSingleton() {
        if (g_singleInstance == null)
            g_singleInstance = new ReadOnly();

        return g_singleInstance;
    } // end getSingleton()

    // is readonly property
    private boolean m_enabled = true;

    public void setReadOnlyEnabled(boolean enabled) {
        m_enabled = enabled;
    }

    public boolean isReadOnlyEnabled() {
        return m_enabled;
    }

    public boolean isReadOnly(DbObject object, MetaField mf, Object value) throws DbException {
        boolean isReadOnly = false;
        String mfName = mf.getJName();

        // Read-Only can be disabled by some operations (open file, reverse
        // engineering, etc.)
        if (!m_enabled)
            return false;

        // For each composite
        DbObject baseObject = object;
        do {
            boolean locked = (mfName.equals(READ_ONLY_MF) || mfName.equals(LOCKED_MF));
            if (object.equals(baseObject) && (locked)) {
                isReadOnly = false;
            } else {
                if (!isReadOnly) {
                    MetaClass mc = object.getMetaClass();

                    MetaField lockedMf = mc.getMetaField(LOCKED_MF);
                    if (lockedMf != null) {
                        Boolean b = (Boolean) object.get(lockedMf);
                        isReadOnly = b.booleanValue();
                    } // end if

                    MetaField readOnlyMf = mc.getMetaField(READ_ONLY_MF);
                    if (readOnlyMf != null) {
                        Boolean b = (Boolean) object.get(readOnlyMf);
                        isReadOnly = b.booleanValue();
                    } // end if
                } // end if
            } // end if

            // get composite
            if (object.equals(baseObject) && mf.equals(DbObject.fComposite)) {
                object = (value != null) ? (DbObject) value : object.getComposite();
            } else {
                object = object.getComposite();
            }

            if (object == null)
                break;
        } while (true);

        return isReadOnly;
    } // end isReadOnly()

    // Public methods
    public void showWarning(Container parent, File file) throws IOException {
        String pattern = LocaleMgr.message.getString("fileReadOnly");

        String msg = MessageFormat.format(pattern, new Object[] { file.getCanonicalPath() });
        JOptionPane.showMessageDialog(parent, msg, ApplicationContext.getApplicationName(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void setReadOnly(DbProject newProject, boolean isReadOnly) {

        try {
            newProject.getDb().beginReadTrans();
            MetaField mf = newProject.getMetaClass().getMetaField(LOCKED_MF);
            String transName = (mf == null) ? "" : newProject.getMetaClass().getGUIName() + " "
                    + mf.getGUIName();
            newProject.getDb().commitTrans();

            if (mf != null) {
                newProject.getDb().beginWriteTrans(transName);
                newProject.set(mf, isReadOnly ? Boolean.TRUE : Boolean.FALSE);
                newProject.getDb().commitTrans();
            } // end if
        } catch (DbException ex) {
            // ignore
        } // end try
    } // end setReadOnly()

} // end ReadOnly
