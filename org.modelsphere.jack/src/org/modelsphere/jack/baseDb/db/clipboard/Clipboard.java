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

package org.modelsphere.jack.baseDb.db.clipboard;

import java.awt.Point;

import org.modelsphere.jack.baseDb.db.DbDeadObjectException;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.SrVector;

public final class Clipboard {

    private DbObject[] copyObjects = new DbObject[0];
    private SrVector clipboardListeners = new SrVector();

    public final boolean isClipboardEmpty() {
        return (copyObjects.length == 0);
    }

    public final void emptyClipboard() {
        copyObjects = new DbObject[0];
        fireClipboardListeners();
    }

    public final void copy(DbObject[] dbos) {
        copyObjects = dbos;
        fireClipboardListeners();
    }

    public final void pasteTo(DbObject[] dbos) throws DbException {
        pasteTo(dbos, true);
    }

    public final void pasteTo(DbObject[] dbos, Point location) throws DbException {
        pasteTo(dbos, true, location);
    }

    public final void pasteTo(DbObject[] dbos, boolean prefixAllowed, Point location)
            throws DbException {
        if (!copyObjects[0].getDb().isValid()) {
            emptyClipboard();
            return;
        }
        try {
            ApplicationContext.getSemanticalModel().paste(copyObjects, dbos,
                    org.modelsphere.jack.srtool.international.LocaleMgr.action.getString("Paste"),
                    prefixAllowed, location);
        } catch (DbDeadObjectException e) {
            emptyClipboard();
            throw e;
        }
    }

    public final void pasteTo(DbObject[] dbos, boolean prefixAllowed) throws DbException {
        if (!copyObjects[0].getDb().isValid()) {
            emptyClipboard();
            return;
        }
        try {
            ApplicationContext.getSemanticalModel().paste(copyObjects, dbos,
                    org.modelsphere.jack.srtool.international.LocaleMgr.action.getString("Paste"),
                    prefixAllowed);
        } catch (DbDeadObjectException e) {
            emptyClipboard();
            throw e;
        }
    }

    public final boolean isValidForPaste(DbObject[] dbos) throws DbException {
        if (dbos.length == 0 || copyObjects.length == 0)
            return false;
        return ApplicationContext.getSemanticalModel().isValidForPaste(copyObjects, dbos);
    }

    public final boolean isValidForPasteOffline(DbObject[] dbos) {
        if (dbos.length == 0 || copyObjects.length == 0)
            return false;
        return ApplicationContext.getSemanticalModel().isValidForPasteOffline(copyObjects, dbos);
    }

    // ////////////////////////////////////
    // Clipboard Listeners Support

    public final void addClipboardListeners(ClipboardListener listener) {
        if (clipboardListeners.indexOf(listener) == -1)
            clipboardListeners.addElement(listener);
    }

    public final void removeClipboardListeners(ClipboardListener listener) {
        clipboardListeners.removeElement(listener);
    }

    private final void fireClipboardListeners() {
        int nb = clipboardListeners.size();
        while (--nb >= 0) {
            ClipboardListener listener = (ClipboardListener) clipboardListeners.elementAt(nb);
            listener.clipboardUpdated();
        }
    }

    // End of Clipboard Listeners Support
    // ////////////////////////////////////

}
