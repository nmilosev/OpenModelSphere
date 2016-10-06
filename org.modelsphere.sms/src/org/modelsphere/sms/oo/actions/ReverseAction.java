/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.oo.actions;

import java.util.List;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentProjectEvent;
import org.modelsphere.jack.srtool.CurrentProjectListener;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.features.reverse.JavaXMIReverseEngineeringPlugin;
import org.modelsphere.sms.features.reverse.ReverseFrameWorker;
import org.modelsphere.sms.oo.international.LocaleMgr;

public final class ReverseAction extends AbstractApplicationAction implements
        CurrentProjectListener {
    private Class reverseFrameWorkerClass = null;
    boolean init = false;
    boolean active = false;
    private boolean m_addedInJavaMenu = false;
    private boolean m_reverseListEmpty = true;

    ReverseAction() {
        super(LocaleMgr.action.getString("reverseinProject"));
        this.setMnemonic(LocaleMgr.action.getMnemonic("reverseinProject"));
        setEnabled(false);
        setVisible(true);
        ApplicationContext.getFocusManager().addCurrentProjectListener(this);
    } //end ReverseAction()

    protected final void doActionPerformed() {
        if (!init) {
            init();
        }

        if (!active)
            return;

        DbProject project = ApplicationContext.getFocusManager().getCurrentProject();
        ReverseFrameWorker.ReverseFrameWorker(project, null);
    }

    public final void currentProjectChanged(CurrentProjectEvent cpe) throws DbException {
        if (!init) {
            init();
        }

        if (active) {
            setVisible(true);
            setEnabled(cpe.getProject() != null);
        }
    }

    public boolean isReverseListEmpty() {
        if (!init) {
            init();
        }

        return m_reverseListEmpty;
    }

    protected int getFeatureSet() {
        return SMSFilter.JAVA;
    }

    //
    // private methods
    //

    private void init() {
        List reverses = getJavaXMIReverseList();
        if (reverses == null || reverses.size() == 0) {
            setEnabled(false);
            setVisible(false);
            ApplicationContext.getFocusManager().removeCurrentProjectListener(this);
            active = false;
            m_reverseListEmpty = true;
        } else {
            active = true;
            m_reverseListEmpty = false;
        }

        init = true;
    }

    // Returns the list of Reverse classes.
    private final List getJavaXMIReverseList() {
        PluginMgr mgr = PluginMgr.getSingleInstance();
        List reverseList = mgr.getPluginsRegistry().getPluginClasses(
                JavaXMIReverseEngineeringPlugin.class);
        return reverseList;
    }
}
