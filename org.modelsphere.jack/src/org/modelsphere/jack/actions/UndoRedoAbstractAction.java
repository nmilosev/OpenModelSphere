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

package org.modelsphere.jack.actions;

import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.Icon;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.event.DbUndoRedoListener;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentProjectEvent;
import org.modelsphere.jack.srtool.CurrentProjectListener;
import org.modelsphere.jack.text.MessageFormat;

public abstract class UndoRedoAbstractAction extends AbstractApplicationAction implements
        CurrentProjectListener, DbUndoRedoListener {

    public static final String COMMANDS = "values"; // NOT LOCALIZABLE, property
    // key

    private String defaultName;
    private String pattern;

    protected UndoRedoAbstractAction(String defaultName, String pattern, Icon icon) {
        super(defaultName, icon);
        this.defaultName = defaultName;
        this.pattern = pattern;
        Db.addDbUndoRedoListener(this);
        ApplicationContext.getFocusManager().addCurrentProjectListener(this);
        setEnabled(false);
    }

    protected abstract String getNextCommandName(Db db, int index);

    protected abstract void perform(Db db) throws DbException;

    public final void setCommandList(String[] newValue) {
        putValue(COMMANDS, newValue);
    }

    public final String[] getCommandList() {
        String[] value = (String[]) getValue(COMMANDS);
        return value;
    }

    protected final void doActionPerformed() {
        doActionPerformed_Internal(0);
    }

    // For internal use with PopupCommandHistoryPanel
    public final void doActionPerformed_Internal(int count) {
        try {
            Db db = ApplicationContext.getFocusManager().getCurrentProject().getDb();
            for (int i = 0; i <= count; i++) {
                // if (db.getc
                perform(db);
            }
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    protected final void refresh() {
        DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
        String name = (project != null ? getNextCommandName(project.getDb(), 0) : null);
        ArrayList list = new ArrayList();
        if (name != null) {
            putValue(Action.NAME, MessageFormat.format(pattern, new Object[] { name }));
            setEnabled(true);
        } else {
            putValue(Action.NAME, defaultName);
            setEnabled(false);
        }
        int commandIndex = 1;
        if (name != null)
            list.add(name);
        while (name != null) {
            name = getNextCommandName(project.getDb(), commandIndex);
            if (name != null)
                list.add(name);
            commandIndex++;
        }
        String[] commands = new String[list.size()];
        for (int i = 0; i < commands.length; i++)
            commands[i] = (String) list.get(i);
        putValue(COMMANDS, commands);
    }

    public final void currentProjectChanged(CurrentProjectEvent cpe) throws DbException {
        refresh();
    }

    // ////////////////////////////////////
    // DbUndoRedoListener SUPPORT
    //
    public final void refresh(Db db) {
        DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
        if (project != null && project.getDb() == db)
            refresh();
    }
    //
    // End of DbUndoRedoListener SUPPORT
    // ////////////////////////////////////
}
