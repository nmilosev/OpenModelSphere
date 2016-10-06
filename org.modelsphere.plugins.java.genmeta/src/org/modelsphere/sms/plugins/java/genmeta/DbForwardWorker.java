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

package org.modelsphere.sms.plugins.java.genmeta;

import java.io.File;
import java.util.ArrayList;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.srtool.forward.ForwardOptions;
import org.modelsphere.jack.srtool.forward.ForwardWorker;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.sms.oo.java.db.DbJVPackage;

final class DbForwardWorker extends ForwardWorker {
    private DbForwardOptions m_options;

    public DbForwardWorker(ForwardOptions options) {
        if (options == null)
            throw new NullPointerException();
        m_options = (DbForwardOptions) options;
    }

    protected String getJobTitle() {
        return "Metamodel Generation";
    }

    // perform the forward
    public void runJob() throws Exception {
        DbForwardEngineeringPlugin forward = (DbForwardEngineeringPlugin) m_options.getForward();
        DbObject[] objects = m_options.getObjects();
        File actualDirectory = m_options.getActualDirectory();
        ArrayList generatedFiles = new ArrayList();
        DbSemanticalObject firstObj = (DbSemanticalObject) objects[0];
        Db db = firstObj.getDb();
        db.beginTrans(Db.READ_TRANS);

        Controller controller = getController();
        for (int i = 0; i < objects.length; i++) {
            forward.genPackages((DbJVPackage) objects[i], actualDirectory, controller,
                    generatedFiles);

            if (!controller.checkPoint()) { //User has cancelled the job
                break;
            } //end if
        } //end for

        db.commitTrans();
        super.terminateRunJob(forward, generatedFiles);
    } //end runJob()
} //end DbForwardWorker
