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

package org.modelsphere.sms.plugins.report;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.sms.plugins.report.model.ReportModel;

public class ReportGeneratorWorker extends Worker {
    ReportModel reportModel = null;

    public ReportGeneratorWorker(ReportModel reportModel) {
        this.reportModel = reportModel;
    }

    protected void runJob() throws Exception {
        if (reportModel != null) {
            reportModel.getEntryPoints()[0].getProject().getDb().beginTrans(Db.READ_TRANS);
            new HtmlGenerator(reportModel, this.getController());
            reportModel.getEntryPoints()[0].getProject().getDb().commitTrans();
        }
    }

    // Return this job's title
    protected String getJobTitle() {
        return LocaleMgr.misc.getString("reportGenerationTitle");
    }

}
