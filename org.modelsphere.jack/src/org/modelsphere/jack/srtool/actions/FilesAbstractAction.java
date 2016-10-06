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

import javax.swing.Icon;
import javax.swing.Action;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentProjectEvent;
import org.modelsphere.jack.srtool.CurrentProjectListener;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.StringUtil;

public abstract class FilesAbstractAction extends AbstractApplicationAction implements
        CurrentProjectListener, DbRefreshListener {

    private String defaultName;
    private String pattern;
    protected DbProject project = null;

    protected FilesAbstractAction(String name, String pattern, Icon icon) {
        super(name, icon);
        defaultName = name;
        this.pattern = pattern;
        setEnabled(false);
        ApplicationContext.getFocusManager().addCurrentProjectListener(this);
    }

    protected FilesAbstractAction(String name, String pattern) {
        this(name, pattern, null);
    }

    public final void refresh() {
        try {
            refreshNoCatch();
        } catch (DbException e) {
        } // will never occur
    }

    public final void refreshNoCatch() throws DbException {
        DbProject newProject = ApplicationContext.getFocusManager().getCurrentProject();
        if (newProject != null) {
            if (!appliesTo(newProject))
                newProject = null;
        }
        if (project != newProject) {
            if (project != null)
                project.removeDbRefreshListener(this);
            project = newProject;
            if (project != null)
                project.addDbRefreshListener(this);
        }
        if (project == null) {
            putValue(Action.NAME, defaultName);
            setEnabled(false);
        } else {
            String projectName = project.getRamFileName();
            if (projectName == null) {
                project.getDb().beginTrans(Db.READ_TRANS);
                projectName = project.getName();
                project.getDb().commitTrans();
            } else {
                projectName = StringUtil.truncateFileName(projectName);
            }
            setEnabled(true);
            putValue(Action.NAME, MessageFormat.format(pattern, new Object[] { projectName }));
        }
    }

    protected boolean appliesTo(DbProject project) throws DbException {
        return true;
    }

    public final void currentProjectChanged(CurrentProjectEvent cpe) throws DbException {
        refreshNoCatch();
    }

    public void refreshAfterDbUpdate(DbUpdateEvent event) throws DbException {
        if (event.metaField == DbSemanticalObject.fName)
            refreshNoCatch();
    }
}
