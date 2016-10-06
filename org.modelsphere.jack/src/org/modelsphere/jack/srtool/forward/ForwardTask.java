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

package org.modelsphere.jack.srtool.forward;

import java.util.ArrayList;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.LongTask;
import org.modelsphere.jack.awt.SwingWorker;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.text.MessageFormat;

public final class ForwardTask extends LongTask {

    private JackForwardEngineeringPlugin forward;
    private DbObject[] semObjs;
    private int[] steps;
    private ArrayList m_generatedFileList = new ArrayList();

    public ForwardTask(JackForwardEngineeringPlugin forward, DbObject[] semObjs) throws DbException {
        this.forward = forward;
        this.semObjs = semObjs;
        computeSteps();
    }

    private void computeSteps() throws DbException {
        steps = new int[semObjs.length];
        int max = 0;
        DbMultiTrans.beginTrans(Db.READ_TRANS, semObjs, null);
        for (int i = 0; i < semObjs.length; i++) {
            steps[i] = forward.getComplexity(semObjs[i]);
            max += steps[i];
        }
        DbMultiTrans.commitTrans(semObjs);
        setLengthOfTask(max);
    }

    public final void go() {
        boolean wait = true;
        SwingWorker worker = new SwingWorker(wait) {
            public Object construct() {
                performTask();
                return ForwardTask.this; // return value not used
            }
        };
        worker.start();
    }

    public final ArrayList getGeneratedFileList() {
        return m_generatedFileList;
    }

    // the actual long running task, this runs in a SwingWorker thread
    private void performTask() {
        try {
            PropertyScreenPreviewInfo info = forward.getPropertyScreenPreviewInfo();
            if (info != null) {
                String tabName = info.getTabName();
                String transName = MessageFormat.format(LocaleMgr.action.getString("generate0"),
                        new Object[] { tabName });
                int transAccess = (forward.isReadOnly() ? Db.READ_TRANS : Db.WRITE_TRANS);
                DbMultiTrans.beginTrans(transAccess, semObjs, transName);
                for (int i = 0; i < semObjs.length; i++) {
                    forward.forwardTo(semObjs[i], m_generatedFileList);
                    incrementProgress(steps[i]);
                    if (isCancel())
                        break;
                } // end for
                if (isCancel()) {
                    Db.abortAllTrans();
                    stop();
                } else {
                    DbMultiTrans.commitTrans(semObjs);
                    terminate();
                } // end if
            } // end if (info != null)
        } catch (Exception e) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), e);
            stop();
        }
    }
}
